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
import dev.zitech.core.common.domain.model.onError
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.UpdateUserAccountUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.first

internal class GetRefreshedTokenUseCase @Inject constructor(
    private val getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
    private val updateUserAccountUseCase: UpdateUserAccountUseCase,
    private val tokenRepository: TokenRepository
) {

    private val tag = Logger.tag(this::class.java)

    suspend operator fun invoke(): Work<Token> =
        when (val currentUserAccountResult = getCurrentUserAccountUseCase().first()) {
            is WorkSuccess -> {
                val currentUser = currentUserAccountResult.data

                val authenticationType = currentUser.authenticationType
                if (authenticationType is UserAccount.AuthenticationType.OAuth) {
                    getRefreshedToken(
                        currentUser = currentUser,
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
        currentUser: UserAccount,
        clientId: String,
        clientSecret: String,
        refreshToken: String,
        oauthCode: String
    ) =
        when (
            val refreshTokenResult = tokenRepository.getRefreshedToken(
                clientId = clientId,
                clientSecret = clientSecret,
                refreshToken = refreshToken
            )
        ) {
            is WorkSuccess -> {
                val refreshedToken = refreshTokenResult.data
                updateUserAccountUseCase(
                    currentUser.copy(
                        authenticationType = UserAccount.AuthenticationType.OAuth(
                            accessToken = refreshedToken.accessToken,
                            clientId = clientId,
                            clientSecret = clientSecret,
                            oauthCode = oauthCode,
                            refreshToken = refreshedToken.refreshToken
                        )
                    )
                ).onError { error ->
                    when (error) {
                        is Error.Fatal -> {
                            Logger.e(tag, throwable = error.throwable)
                        }
                        else -> Logger.e(tag, error.debugText)
                    }
                }
                WorkSuccess(refreshedToken)
            }
            is WorkError -> refreshTokenResult
        }
}
