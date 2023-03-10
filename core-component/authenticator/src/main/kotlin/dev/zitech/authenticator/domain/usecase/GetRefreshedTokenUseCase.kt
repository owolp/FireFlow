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
import dev.zitech.core.common.domain.exception.FireFlowException
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
                getRefreshedToken(currentUser)
            }
            is WorkError -> WorkError(currentUserAccountResult.fireFlowException)
        }

    private suspend fun getRefreshedToken(currentUser: UserAccount) =
        when (
            val refreshTokenResult = tokenRepository.getRefreshedToken(
                clientId = currentUser.clientId,
                clientSecret = currentUser.clientSecret,
                refreshToken = currentUser.refreshToken.orEmpty()
            )
        ) {
            is WorkSuccess -> {
                val refreshedToken = refreshTokenResult.data
                updateUserAccountUseCase(
                    currentUser.copy(
                        accessToken = refreshedToken.accessToken,
                        refreshToken = refreshedToken.refreshToken
                    )
                ).onError { exception ->
                    when (exception) {
                        is FireFlowException.Fatal -> {
                            Logger.e(tag, throwable = exception.throwable)
                        }
                        else -> Logger.e(tag, exception.text)
                    }
                }
                WorkSuccess(refreshedToken)
            }
            is WorkError -> refreshTokenResult
        }
}
