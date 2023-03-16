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

package dev.zitech.authenticator.framework.remote.authenticator

import dev.zitech.authenticator.domain.usecase.GetRefreshedTokenUseCase
import dev.zitech.authenticator.framework.remote.HEADER_AUTHORIZATION_KEY
import dev.zitech.authenticator.framework.remote.HEADER_AUTHORIZATION_VALUE
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.WorkError
import dev.zitech.core.common.domain.model.WorkSuccess
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

internal class RefreshTokenAuthenticator @Inject constructor(
    private val getCurrentUserAccountUseCase: dagger.Lazy<GetCurrentUserAccountUseCase>,
    private val getRefreshedTokenUseCase: dagger.Lazy<GetRefreshedTokenUseCase>
) : Authenticator {

    private companion object {
        const val UNAUTHENTICATED_CODE = 401
    }

    private val tag = Logger.tag(this::class.java)

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        return@runBlocking when (
            val currentUserAccountResult = getCurrentUserAccountUseCase
                .get().invoke().first()
        ) {
            is WorkSuccess -> checkAuthenticationType(
                currentUserAccountResult.data.authenticationType,
                response
            )
            is WorkError -> null
        }
    }

    private suspend fun checkAuthenticationType(
        authenticationType: UserAccount.AuthenticationType?,
        response: Response
    ): Request? = if (authenticationType is UserAccount.AuthenticationType.OAuth) {
        if (response.code() == UNAUTHENTICATED_CODE) {
            getRefreshedToken(response)
        } else {
            Logger.e(tag, message = "Response code not handled=${response.code()}")
            null
        }
    } else {
        null
    }

    private suspend fun getRefreshedToken(response: Response): Request? {
        Logger.i(tag, "Token has expired, refreshing...")
        return when (
            val refreshedTokenResult = getRefreshedTokenUseCase.get().invoke()
        ) {
            is WorkSuccess -> {
                val accessToken = refreshedTokenResult.data.accessToken
                @Suppress("SENSELESS_COMPARISON")
                if (accessToken != null) {
                    response.request().newBuilder()
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
            is WorkError -> {
                Logger.e(tag, message = refreshedTokenResult.error.debugText)
                null
            }
        }
    }
}
