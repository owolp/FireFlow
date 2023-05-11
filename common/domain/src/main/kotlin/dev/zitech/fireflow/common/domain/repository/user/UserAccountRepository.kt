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

/**
 * Repository for managing user accounts.
 */
interface UserAccountRepository {

    /**
     * Retrieves the current user account as a [Flow] of [OperationResult].
     *
     * @return A [Flow] that emits the [OperationResult] of the current user account.
     */
    fun getCurrentUserAccount(): Flow<OperationResult<UserAccount>>

    /**
     * Retrieves a user account by the given state.
     *
     * @param state The state of the user account to retrieve.
     * @return The [OperationResult] of the user account matching the state.
     */
    suspend fun getUserAccountByState(state: String): OperationResult<UserAccount>

    /**
     * Retrieves all user accounts as a [Flow] of [OperationResult] list.
     *
     * @return A [Flow] that emits the [OperationResult] list of user accounts.
     */
    fun getUserAccounts(): Flow<OperationResult<List<UserAccount>>>

    /**
     * Removes user accounts with the specified state and no token.
     *
     * @return The [OperationResult] indicating the success or failure of the operation.
     */
    suspend fun removeUserAccountsWithStateAndNoToken(): OperationResult<Unit>

    /**
     * Removes user accounts with the specified state and token, but no client ID and secret.
     *
     * @return The [OperationResult] indicating the success or failure of the operation.
     */
    suspend fun removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret(): OperationResult<Unit>

    /**
     * Saves a new user account with the provided details.
     *
     * @param accessToken The access token of the user account.
     * @param clientId The client ID of the user account.
     * @param clientSecret The client secret of the user account.
     * @param isCurrentUserAccount Indicates whether the user account is the current user account.
     * @param serverAddress The server address of the user account.
     * @param state The state of the user account.
     * @return The [OperationResult] containing the ID of the saved user account.
     */
    suspend fun saveUserAccount(
        accessToken: String?,
        clientId: String?,
        clientSecret: String?,
        isCurrentUserAccount: Boolean,
        serverAddress: String,
        state: String
    ): OperationResult<Long>

    /**
     * Updates an existing user account with the provided details.
     *
     * @param userAccount The updated user account.
     * @return The [OperationResult] indicating the success or failure of the operation.
     */
    suspend fun updateUserAccount(userAccount: UserAccount): OperationResult<Unit>
}
