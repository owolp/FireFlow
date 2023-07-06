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

package dev.zitech.fireflow.common.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.zitech.fireflow.common.data.local.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) interface for accessing user data.
 */
@Dao
internal interface UserDao {

    /**
     * Checks if a user with the specified identifier exists in the "users" table.
     *
     * This function performs a query to check if a user with the given [identifier] exists in the "users" table.
     *
     * @param identifier The identifier of the user to check for existence.
     * @return `true` if a user with the specified identifier exists in the "users" table, `false` otherwise.
     */
    @Query("SELECT EXISTS (SELECT 1 FROM users WHERE identifier=:identifier) AS result")
    suspend fun checkUserExistsByIdentifier(identifier: String): Boolean

    /**
     * Checks if a user with the specified identifier and server address exists in the "users" table.
     *
     * This function performs a query to check if a user with the given [identifier] and [serverAddress]
     * exists in the "users" table.
     *
     * @param identifier The identifier of the user to check for existence.
     * @param serverAddress The server address associated with the user.
     * @return `true` if a user with the specified identifier and server address exists in the "users" table,
     *         `false` otherwise.
     */
    @Query(
        "SELECT EXISTS " +
            "(SELECT 1 FROM users WHERE identifier=:identifier AND serverAddress=:serverAddress) " +
            "AS result"
    )
    suspend fun checkUserExistsByIdentifierAndServerAddress(
        identifier: String,
        serverAddress: String
    ): Boolean

    /**
     * Deletes a user entity from the "users" table based on the specified user ID.
     *
     * This function deletes the user entity with the given [userId] from the "users" table.
     *
     * @param userId The ID of the user to be deleted.
     * @return An [Int] representing the number of rows affected by the update operation. If the
     *         update was successful, the return value will be the number of rows modified (usually 1).
     *         If no rows were modified, the return value will be 0.
     */
    @Query("DELETE FROM users WHERE id=:userId")
    suspend fun deleteUserById(userId: Long): Int

    /**
     * Retrieves the current user as a flow of [UserEntity].
     *
     * This function queries the "users" table and returns a flow of the current user entity.
     * The current user is determined by the `isCurrentUser` flag being set to 1.
     * The query orders the results by ID in descending order and limits the result to one entity.
     *
     * @return A flow of the current user entity, or null if not found.
     */
    @Query("SELECT * FROM users WHERE isCurrentUser=1 ORDER BY id DESC LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    /**
     * Retrieves a user by its state value.
     *
     * This function queries the "users" table and returns the user entity that matches the specified state.
     * If no user is found with the given state, it returns null.
     *
     * @param state The state value of the user.
     * @return The user entity matching the specified state, or null if not found.
     */
    @Query("SELECT * FROM users WHERE state=:state")
    suspend fun getUserByState(state: String): UserEntity?

    /**
     * Retrieves all users from the "users" table, sorted by identifier.
     *
     * This function returns a flow of lists of [UserEntity] objects representing all the users
     * in the "users" table, ordered by their identifier.
     *
     * @return A [Flow] emitting a list of [UserEntity] objects representing all the users
     *         in the "users" table, sorted by their identifier.
     */
    @Query("SELECT * FROM users ORDER BY identifier")
    fun getUsers(): Flow<List<UserEntity>>

    /**
     * Updates the "isCurrentUser" field of all users in the "users" table to 0.
     *
     * This function performs an update query to set the "isCurrentUser" field of all users
     * in the "users" table to 0, indicating that they are not the current user.
     *
     * @return An [Int] representing the number of rows affected by the update operation. If the
     *         update was successful, the return value will be the number of rows modified.
     */
    @Query("UPDATE users SET isCurrentUser=0")
    suspend fun removeCurrentUserOrUsers(): Int

    /**
     * Removes users that have a state but no access token.
     *
     * This function removes user entities from the "users" table that meet the following conditions:
     * - The "state" column is not null.
     * - The "accessToken" column is null.
     *
     * @return An [Int] representing the number of users removed.
     */
    @Query("DELETE FROM users WHERE state IS NOT NULL AND accessToken IS NULL AND identifier IS NULL")
    suspend fun removeUsersWithStateAndNoTokenAndIdentifier(): Int

    /**
     * Removes users that have an access token but no identifier.
     *
     * This function removes user entities from the "users" table that meet the following conditions:
     * - The "accessToken" column is not null.
     * - The "identifier" column is null.
     *
     * @return An [Int] representing the number of users removed.
     */
    @Query("DELETE FROM users WHERE accessToken IS NOT NULL AND identifier IS NULL")
    suspend fun removeUsersWithTokenAndNoIdentifier(): Int

    /**
     * Removes users that have a state and access token, but no client ID and client secret.
     *
     * This function removes user entities from the "users" table that meet the following conditions:
     * - The "state" column is not null.
     * - The "accessToken" column is not null.
     * - The "clientId" column is null.
     * - The "clientSecret" column is null.
     *
     * @return An [Int] representing the number of users removed.
     */
    @Query(
        "DELETE FROM users WHERE state IS NOT NULL AND accessToken IS NOT" +
            " NULL AND clientId IS NULL AND clientSecret IS NULL AND identifier IS NULL"
    )
    suspend fun removeUsersWithStateAndTokenAndNoClientIdAndSecretAndIdentifier(): Int

    /**
     * Inserts or replaces a user entity.
     *
     * This function saves the specified [userEntity] in the "users" table. It uses the
     * @Insert annotation with the onConflict strategy set to REPLACE, which means that if a
     * conflicting user already exists (based on primary key), it will be replaced with the new
     * user entity.
     *
     * @param userEntity The user entity to be saved.
     * @return A [Long] representing the ID of the saved user entity. If the insertion was
     *         successful, the return value will be the ID of the saved user. If the user entity
     *         was replaced, the return value will also be the ID of the saved user. If the
     *         operation failed, the return value will be -1.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(userEntity: UserEntity): Long

    /**
     * Updates a user entity.
     *
     * This function updates the specified [userEntity] in the "users" table. It uses the
     * @Update annotation to perform the update operation.
     *
     * @param userEntity The user entity to be updated.
     * @return An [Int] representing the number of users updated. If the update was successful,
     *         the return value will be 1, indicating that one user was updated. If no user was
     *         updated, the return value will be 0.
     */
    @Update
    suspend fun updateUser(userEntity: UserEntity): Int

    /**
     * Updates the current user status for the specified user ID.
     *
     * This query updates the "isCurrentUser" field in the "users" table, setting it to 1 (true)
     * for the user with the specified ID, and to 0 (false) for all other users.
     *
     * @param userId The ID of the user to update.
     * @return An [Int] representing the number of rows affected by the update operation. If the
     *         update was successful, the return value will be the number of rows modified (usually 1).
     *         If no rows were modified, the return value will be 0.
     */
    @Query("UPDATE users SET isCurrentUser = CASE WHEN id=:userId THEN 1 ELSE 0 END")
    suspend fun updateUserCurrentStatus(userId: Long): Int
}
