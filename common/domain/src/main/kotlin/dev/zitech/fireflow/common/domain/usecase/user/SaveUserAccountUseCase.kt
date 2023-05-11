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

package dev.zitech.fireflow.common.domain.usecase.user

import dev.zitech.fireflow.common.domain.repository.user.UserAccountRepository
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

/**
 * Use case for saving a user account.
 *
 * @property userAccountRepository The repository for managing user accounts.
 */
class SaveUserAccountUseCase @Inject constructor(
    private val userAccountRepository: UserAccountRepository
) {
    /**
     * Invokes the use case to save a user account.
     *
     * @param accessToken The access token of the user account.
     * @param clientId The client ID of the user account.
     * @param clientSecret The client secret of the user account.
     * @param isCurrentUserAccount Flag indicating if the user account is the current user account.
     * @param serverAddress The server address of the user account.
     * @param state The state of the user account.
     *
     * @return The result of the operation containing the ID of the saved user account.
     */
    suspend operator fun invoke(
        accessToken: String? = null,
        clientId: String? = null,
        clientSecret: String? = null,
        isCurrentUserAccount: Boolean,
        serverAddress: String,
        state: String
    ): OperationResult<Long> =
        userAccountRepository.saveUserAccount(
            clientId = clientId,
            clientSecret = clientSecret,
            isCurrentUserAccount = isCurrentUserAccount,
            accessToken = accessToken,
            serverAddress = serverAddress,
            state = state
        )
}
