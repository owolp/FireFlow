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

import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.core.result.OperationResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface for accessing and managing users.
 */
internal interface UserSource {

    /**
     * Retrieves the current user as a flow.
     *
     * @return A flow that emits the [OperationResult] representing the current user.
     */
    fun getCurrentUser(): Flow<OperationResult<User>>

    /**
     * Retrieves a user based on the specified state.
     *
     * @param state The state associated with the user.
     * @return An [OperationResult] that represents the result of the operation,
     *         containing the user if found, or an error if not found.
     */
    suspend fun getUserByState(state: String): OperationResult<User>

    /**
     * Retrieves all users as a flow.
     *
     * @return A flow that emits the [OperationResult] representing the list of users.
     */
    fun getUsers(): Flow<OperationResult<List<User>>>

    /**
     * Removes users that have the specified state and no access token.
     *
     * @return An [OperationResult] representing the result of the removal operation.
     */
    suspend fun removeUsersWithStateAndNoToken(): OperationResult<Unit>

    /**
     * Removes users that have the specified state, access token, and no client ID and secret.
     *
     * @return An [OperationResult] representing the result of the removal operation.
     */
    suspend fun removeUsersWithStateAndTokenAndNoClientIdAndSecret(): OperationResult<Unit>

    /**
     * Saves a new user or updates an existing user.
     *
     * @param accessToken The access token associated with the user.
     * @param clientId The OAuth client ID associated with the user.
     * @param clientSecret The OAuth client secret associated with the user.
     * @param connectivityNotification Indicates whether the user has enabled connectivity checks.
     * @param isCurrentUser Indicates whether the user is the current user.
     * @param serverAddress The server address of the user. If the value is null, it indicates that the user is local.
     * @param state The state associated with the user.
     * @return An [OperationResult] representing the result of the save/update operation,
     *         containing the ID of the saved/updated user if successful, or an error if unsuccessful.
     */
    suspend fun saveUser(
        accessToken: String?,
        clientId: String?,
        clientSecret: String?,
        connectivityNotification: Boolean,
        isCurrentUser: Boolean,
        serverAddress: String?,
        state: String
    ): OperationResult<Long>

    /**
     * Updates an existing user.
     *
     * @param user The updated user.
     * @return An [OperationResult] representing the result of the update operation,
     *         containing the number of affected rows if successful, or an error if unsuccessful.
     */
    suspend fun updateUser(user: User): OperationResult<Int>
}
