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

package dev.zitech.authenticator.domain.usecase

import dev.zitech.authenticator.domain.model.Token
import dev.zitech.authenticator.domain.repository.TokenRepository
import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.Work
import dev.zitech.core.common.domain.model.WorkError
import dev.zitech.core.common.domain.model.WorkSuccess
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.UpdateCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.UpdateCurrentUserAccountUseCase.AuthenticationType
import javax.inject.Inject
import kotlinx.coroutines.flow.first

/**
 * Use case for getting a refreshed OAuth token for the current user.
 *
 * @property getCurrentUserAccountUseCase Use case for getting the current user account.
 * @property tokenRepository Repository for refreshing tokens.
 * @property updateCurrentUserAccountUseCase Use case for updating the current user account with a new token.
 */
internal class GetRefreshedTokenUseCase @Inject constructor(
    private val getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
    private val tokenRepository: TokenRepository,
    private val updateCurrentUserAccountUseCase: UpdateCurrentUserAccountUseCase
) {

    private val tag = Logger.tag(this::class.java)

    /**
     * Gets a refreshed token for the current user.
     *
     * @return A [Work] object containing the refreshed token, or an error if the operation failed.
     */
    suspend operator fun invoke(): Work<Token> =
        when (val currentUserAccountResult = getCurrentUserAccountUseCase().first()) {
            is WorkSuccess -> {
                val currentUser = currentUserAccountResult.data
                val authenticationType = currentUser.authenticationType
                if (authenticationType is UserAccount.AuthenticationType.OAuth) {
                    getRefreshedToken(
                        clientId = authenticationType.clientId,
                        clientSecret = authenticationType.clientSecret,
                        refreshToken = authenticationType.refreshToken.orEmpty(),
                        oauthCode = authenticationType.oauthCode.orEmpty()
                    )
                } else {
                    WorkError(Error.AuthenticationProblem)
                }
            }
            is WorkError -> WorkError(currentUserAccountResult.error)
        }

    private suspend fun getRefreshedToken(
        clientId: String,
        clientSecret: String,
        refreshToken: String,
        oauthCode: String
    ): Work<Token> = when (
        val refreshTokenResult = tokenRepository.getRefreshedToken(
            clientId = clientId,
            clientSecret = clientSecret,
            refreshToken = refreshToken
        )
    ) {
        is WorkSuccess -> {
            val refreshedToken = refreshTokenResult.data
            when (
                val result = updateCurrentUserAccountUseCase(
                    AuthenticationType(
                        UserAccount.AuthenticationType.OAuth(
                            accessToken = refreshedToken.accessToken,
                            clientId = clientId,
                            clientSecret = clientSecret,
                            oauthCode = oauthCode,
                            refreshToken = refreshedToken.refreshToken
                        )
                    )
                )
            ) {
                is WorkError -> {
                    when (val error = result.error) {
                        is Error.Fatal -> {
                            Logger.e(tag, throwable = error.throwable)
                        }
                        else -> Logger.e(tag, error.debugText)
                    }
                    WorkError(result.error)
                }
                is WorkSuccess -> {
                    WorkSuccess(refreshedToken)
                }
            }
        }
        is WorkError -> refreshTokenResult
    }
}
