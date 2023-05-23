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

package dev.zitech.fireflow.common.domain.usecase.token

import dev.zitech.fireflow.common.domain.model.authentication.Token
import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.common.domain.model.user.UserAuthenticationType
import dev.zitech.fireflow.common.domain.repository.authentication.TokenRepository
import dev.zitech.fireflow.common.domain.usecase.user.GetCurrentUserUseCase
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserUseCase
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserUseCase.AuthenticationType
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.first

/**
 * Use case for getting a refreshed OAuth token for the current user.
 *
 * @property getCurrentUserUseCase Use case for getting the current user.
 * @property tokenRepository Repository for refreshing tokens.
 * @property updateCurrentUserUseCase Use case for updating the current user with a new token.
 */
class GetRefreshedTokenUseCase @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val tokenRepository: TokenRepository,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase
) {

    private val tag = Logger.tag(this::class.java)

    /**
     * Gets a refreshed token for the current user.
     *
     * @return A [OperationResult] object containing the refreshed token, or an error if the operation failed.
     */
    suspend operator fun invoke(): OperationResult<Token> =
        when (val currentUserResult = getCurrentUserUseCase().first()) {
            is Success -> {
                when (val currentUser = currentUserResult.data) {
                    is User.Local -> Failure(Error.LocalUserTypeNotSupported)
                    is User.Remote -> {
                        val authenticationType = currentUser.authenticationType
                        if (authenticationType is UserAuthenticationType.OAuth) {
                            getRefreshedToken(
                                clientId = authenticationType.clientId,
                                clientSecret = authenticationType.clientSecret,
                                refreshToken = authenticationType.refreshToken.orEmpty(),
                                oauthCode = authenticationType.oauthCode.orEmpty()
                            )
                        } else {
                            Failure(Error.AuthenticationProblem)
                        }
                    }
                }
            }

            is Failure -> Failure(currentUserResult.error)
        }

    private suspend fun getRefreshedToken(
        clientId: String,
        clientSecret: String,
        refreshToken: String,
        oauthCode: String
    ): OperationResult<Token> = when (
        val refreshTokenResult = tokenRepository.getRefreshedToken(
            clientId = clientId,
            clientSecret = clientSecret,
            refreshToken = refreshToken
        )
    ) {
        is Success -> {
            val refreshedToken = refreshTokenResult.data
            when (
                val result = updateCurrentUserUseCase(
                    AuthenticationType(
                        UserAuthenticationType.OAuth(
                            accessToken = refreshedToken.accessToken,
                            clientId = clientId,
                            clientSecret = clientSecret,
                            oauthCode = oauthCode,
                            refreshToken = refreshedToken.refreshToken
                        )
                    )
                )
            ) {
                is Failure -> {
                    when (val error = result.error) {
                        is Error.Fatal -> {
                            Logger.e(tag, throwable = error.throwable)
                        }

                        else -> Logger.e(tag, error.debugText)
                    }
                    Failure(result.error)
                }

                is Success -> {
                    Success(refreshedToken)
                }
            }
        }

        is Failure -> refreshTokenResult
    }
}
