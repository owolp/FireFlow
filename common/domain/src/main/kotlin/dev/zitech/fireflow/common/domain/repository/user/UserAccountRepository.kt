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

package dev.zitech.fireflow.common.domain.repository.user

import dev.zitech.fireflow.common.domain.model.user.UserAccount
import dev.zitech.fireflow.core.result.OperationResult
import kotlinx.coroutines.flow.Flow

interface UserAccountRepository {

    fun getCurrentUserAccount(): Flow<OperationResult<UserAccount>>
    suspend fun getUserAccountByState(state: String): OperationResult<UserAccount>
    fun getUserAccounts(): Flow<OperationResult<List<UserAccount>>>
    suspend fun removeUserAccountsWithStateAndNoToken(): OperationResult<Unit>
    suspend fun removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret(): OperationResult<Unit>
    suspend fun saveUserAccount(
        accessToken: String?,
        clientId: String?,
        clientSecret: String?,
        isCurrentUserAccount: Boolean,
        serverAddress: String,
        state: String
    ): OperationResult<Long>

    suspend fun updateUserAccount(userAccount: UserAccount): OperationResult<Unit>
}
