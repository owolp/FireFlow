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
import dev.zitech.core.common.domain.code.StatusCode
import dev.zitech.core.common.domain.model.DataError
import dev.zitech.core.common.domain.model.DataException
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.domain.model.DataSuccess
import dev.zitech.core.common.domain.model.LegacyDataResult
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.UpdateUserAccountUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.first

internal class GetRefreshedTokenUseCase @Inject constructor(
    private val getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
    private val updateUserAccountUseCase: UpdateUserAccountUseCase,
    private val tokenRepository: TokenRepository
) {

    suspend operator fun invoke(): DataResult<Token> =
        when (val currentUserAccountResult = getCurrentUserAccountUseCase().first()) {
            is LegacyDataResult.Success -> {
                val currentUser = currentUserAccountResult.value
                when (
                    val refreshTokenResult = tokenRepository.getRefreshedToken(
                        clientId = currentUser.clientId,
                        clientSecret = currentUser.clientSecret,
                        refreshToken = currentUser.refreshToken.orEmpty()
                    )
                ) {
                    is DataSuccess -> {
                        val refreshedToken = refreshTokenResult.data
                        updateUserAccountUseCase(
                            currentUser.copy(
                                accessToken = refreshedToken.accessToken,
                                refreshToken = refreshedToken.refreshToken
                            )
                        )
                        DataSuccess(refreshedToken)
                    }
                    is DataError -> refreshTokenResult
                    is DataException -> refreshTokenResult
                }
            }
            is LegacyDataResult.Error -> DataError(StatusCode.Unknown, "")
        }
}
