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

package dev.zitech.fireflow.common.data.source.user

import dev.zitech.fireflow.common.domain.model.user.UserAccount
import dev.zitech.fireflow.core.result.OperationResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface for accessing and managing user accounts.
 */
internal interface UserAccountSource {

    /**
     * Retrieves the current user account as a flow.
     *
     * @return A flow that emits the [OperationResult] representing the current user account.
     */
    fun getCurrentUserAccount(): Flow<OperationResult<UserAccount>>

    /**
     * Retrieves a user account based on the specified state.
     *
     * @param state The state associated with the user account.
     * @return An [OperationResult] that represents the result of the operation,
     *         containing the user account if found, or an error if not found.
     */
    suspend fun getUserAccountByState(state: String): OperationResult<UserAccount>

    /**
     * Retrieves all user accounts as a flow.
     *
     * @return A flow that emits the [OperationResult] representing the list of user accounts.
     */
    fun getUserAccounts(): Flow<OperationResult<List<UserAccount>>>

    /**
     * Removes user accounts that have the specified state and no access token.
     *
     * @return An [OperationResult] representing the result of the removal operation.
     */
    suspend fun removeUserAccountsWithStateAndNoToken(): OperationResult<Unit>

    /**
     * Removes user accounts that have the specified state, access token, and no client ID and secret.
     *
     * @return An [OperationResult] representing the result of the removal operation.
     */
    suspend fun removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret(): OperationResult<Unit>

    /**
     * Saves a new user account or updates an existing user account.
     *
     * @param accessToken The access token associated with the user account.
     * @param clientId The OAuth client ID associated with the user account.
     * @param clientSecret The OAuth client secret associated with the user account.
     * @param isCurrentUserAccount Indicates whether the user account is the current user account.
     * @param serverAddress The server address associated with the user account.
     * @param state The state associated with the user account.
     * @return An [OperationResult] representing the result of the save/update operation,
     *         containing the ID of the saved/updated user account if successful, or an error if unsuccessful.
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
     * Updates an existing user account.
     *
     * @param userAccount The updated user account.
     * @return An [OperationResult] representing the result of the update operation,
     *         containing the number of affected rows if successful, or an error if unsuccessful.
     */
    suspend fun updateUserAccount(userAccount: UserAccount): OperationResult<Int>
}
