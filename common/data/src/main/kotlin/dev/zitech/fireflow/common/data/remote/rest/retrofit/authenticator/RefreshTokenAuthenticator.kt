/*
 * Copyright (C) 2023 Zitech Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.zitech.fireflow.common.data.remote.rest.retrofit.authenticator

import dev.zitech.fireflow.common.data.remote.rest.HEADER_AUTHORIZATION_KEY
import dev.zitech.fireflow.common.data.remote.rest.HEADER_AUTHORIZATION_VALUE
import dev.zitech.fireflow.common.domain.model.user.UserAuthenticationType
import dev.zitech.fireflow.common.domain.usecase.token.GetRefreshedTokenUseCase
import dev.zitech.fireflow.common.domain.usecase.user.GetCurrentUserAccountUseCase
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * [Authenticator] implementation responsible for handling authentication challenges and refreshing access tokens.
 *
 * @property getCurrentUserAccountUseCase Lazy injection of [GetCurrentUserAccountUseCase] for retrieving the current user account.
 * @property getRefreshedTokenUseCase Lazy injection of [GetRefreshedTokenUseCase] for obtaining a refreshed access token.
 */
internal class RefreshTokenAuthenticator @Inject constructor(
    private val getCurrentUserAccountUseCase: dagger.Lazy<GetCurrentUserAccountUseCase>,
    private val getRefreshedTokenUseCase: dagger.Lazy<GetRefreshedTokenUseCase>
) : Authenticator {

    private val tag = Logger.tag(this::class.java)

    /**
     * Authenticates the request by handling authentication challenges.
     * If the user account is authenticated with OAuth and the response is unauthenticated, a refreshed token is obtained.
     *
     * @param route The route being accessed.
     * @param response The response received.
     * @return A new request with updated authentication headers, or null if no action is needed.
     */
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        return@runBlocking when (
            val currentUserAccountResult = getCurrentUserAccountUseCase
                .get().invoke().first()
        ) {
            is Success -> checkAuthenticationType(
                currentUserAccountResult.data.authenticationType,
                response
            )

            is Failure -> null
        }
    }

    /**
     * Checks the authentication type and handles the response accordingly.
     * If the authentication type is OAuth and the response is unauthenticated, a refreshed token is obtained.
     *
     * @param authenticationType The authentication type of the current user account.
     * @param response The response received.
     * @return A new request with updated authentication headers, or null if no action is needed.
     */
    private suspend fun checkAuthenticationType(
        authenticationType: UserAuthenticationType?,
        response: Response
    ): Request? = if (authenticationType is UserAuthenticationType.OAuth) {
        if (response.code == UNAUTHENTICATED_CODE) {
            getRefreshedToken(response)
        } else {
            Logger.e(tag, message = "Response code not handled=${response.code}")
            null
        }
    } else {
        null
    }

    /**
     * Obtains a refreshed token and builds a new request with updated authentication headers.
     *
     * @param response The response received.
     * @return A new request with updated authentication headers, or null if an error occurs.
     */
    private suspend fun getRefreshedToken(response: Response): Request? {
        Logger.i(tag, "Token has expired, refreshing...")
        return when (
            val refreshedTokenResult = getRefreshedTokenUseCase.get().invoke()
        ) {
            is Success -> {
                val accessToken = refreshedTokenResult.data.accessToken
                @Suppress("SENSELESS_COMPARISON")
                if (accessToken != null) {
                    response.request.newBuilder()
                        .removeHeader(HEADER_AUTHORIZATION_KEY)
                        .addHeader(
                            HEADER_AUTHORIZATION_KEY,
                            "$HEADER_AUTHORIZATION_VALUE $accessToken"
                        )
                        .build()
                } else {
                    Logger.e(tag, message = "Refreshed accessToken is null")
                    null
                }
            }

            is Failure -> {
                Logger.e(tag, message = refreshedTokenResult.error.debugText)
                null
            }
        }
    }

    private companion object {
        const val UNAUTHENTICATED_CODE = 401
    }
}
