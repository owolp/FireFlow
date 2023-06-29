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

import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.core.result.OperationResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing users.
 */
interface UserRepository {

    /**
     * Deletes a user based on the specified user ID.
     *
     * @param userId The ID of the user to be deleted.
     * @return An [OperationResult] representing the result of the update operation,
     *         containing the number of affected rows if successful, or an error if unsuccessful.
     */
    suspend fun deleteUserById(userId: Long): OperationResult<Int>

    /**
     * Retrieves the current user as a [Flow] of [OperationResult].
     *
     * @return A [Flow] that emits the [OperationResult] of the current user.
     */
    fun getCurrentUser(): Flow<OperationResult<User>>

    /**
     * Retrieves a user by the given state.
     *
     * @param state The state of the user to retrieve.
     * @return The [OperationResult] of the user matching the state.
     */
    suspend fun getUserByState(state: String): OperationResult<User>

    /**
     * Retrieves all users as a [Flow] of [OperationResult] list.
     *
     * @return A [Flow] that emits the [OperationResult] list of users.
     */
    fun getUsers(): Flow<OperationResult<List<User>>>

    /**
     * Removes users with the specified state and no token.
     *
     * @return An [OperationResult] representing the result of the update operation,
     *         containing the number of affected rows if successful, or an error if unsuccessful.
     */
    suspend fun removeUsersWithStateAndNoTokenAndEmail(): OperationResult<Int>

    /**
     * Removes users with the specified state and token, but no client ID and secret.
     *
     * @return An [OperationResult] representing the result of the update operation,
     *         containing the number of affected rows if successful, or an error if unsuccessful.
     */
    suspend fun removeUsersWithStateAndTokenAndNoClientIdAndSecretAndEmail(): OperationResult<Int>

    /**
     * Saves a new user with the provided details.
     *
     * @param accessToken The access token of the user.
     * @param clientId The client ID of the user.
     * @param clientSecret The client secret of the user.
     * @param connectivityNotification Indicates whether the user has enabled connectivity checks.
     * @param isCurrentUser Indicates whether the user is the current user.
     * @param serverAddress The server address of the user. If the value is null, it indicates that the user is local.
     * @param state The state of the user.
     * @return An [OperationResult] representing the result of the save/update operation,
     *         containing the ID of the saved/updated user if successful, or an error if unsuccessful.
     */
    suspend fun saveUser(
        accessToken: String?,
        clientId: String?,
        clientSecret: String?,
        connectivityNotification: Boolean,
        email: String?,
        isCurrentUser: Boolean,
        serverAddress: String?,
        state: String
    ): OperationResult<Long>

    /**
     * Updates an existing user with the provided details.
     *
     * @param user The updated user.
     * @return The [OperationResult] indicating the success or failure of the operation.
     */
    suspend fun updateUser(user: User): OperationResult<Unit>

    /**
     * Updates the current user status for the specified user ID.
     *
     * @param userId The ID of the user to update.
     * @return An [OperationResult] representing the result of the update operation,
     *         containing the number of affected rows if successful, or an error if unsuccessful.
     */
    suspend fun updateUserCurrentStatus(userId: Long): OperationResult<Int>
}
