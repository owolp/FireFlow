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
import dev.zitech.fireflow.common.domain.model.user.UserAuthenticationType
import dev.zitech.fireflow.common.domain.repository.authentication.TokenRepository
import dev.zitech.fireflow.common.domain.usecase.user.GetCurrentUserAccountUseCase
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserAccountUseCase
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserAccountUseCase.AuthenticationType
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.core.work.OperationResult
import dev.zitech.fireflow.core.work.OperationResult.Failure
import dev.zitech.fireflow.core.work.OperationResult.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.first

/**
 * Use case for getting a refreshed OAuth token for the current user.
 *
 * @property getCurrentUserAccountUseCase Use case for getting the current user account.
 * @property tokenRepository Repository for refreshing tokens.
 * @property updateCurrentUserAccountUseCase Use case for updating the current user account with a new token.
 */
class GetRefreshedTokenUseCase @Inject constructor(
    private val getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
    private val tokenRepository: TokenRepository,
    private val updateCurrentUserAccountUseCase: UpdateCurrentUserAccountUseCase
) {

    private val tag = Logger.tag(this::class.java)

    /**
     * Gets a refreshed token for the current user.
     *
     * @return A [OperationResult] object containing the refreshed token, or an error if the operation failed.
     */
    suspend operator fun invoke(): OperationResult<Token> =
        when (val currentUserAccountResult = getCurrentUserAccountUseCase().first()) {
            is Success -> {
                val currentUser = currentUserAccountResult.data
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

            is Failure -> Failure(currentUserAccountResult.error)
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
                val result = updateCurrentUserAccountUseCase(
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
