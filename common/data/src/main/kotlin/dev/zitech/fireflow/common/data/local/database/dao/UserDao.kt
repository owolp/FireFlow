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
     * Retrieves the current users as a flow of [UserEntity].
     *
     * @return A flow of the current user entity, or null if not found.
     */
    @Query("SELECT * FROM users WHERE isCurrentUser=1 ORDER BY id DESC LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    /**
     * Retrieves a user by its state value.
     *
     * @param state The state value of the user.
     * @return The user entity matching the specified state, or null if not found.
     */
    @Query("SELECT * FROM users WHERE state=:state")
    suspend fun getUserByState(state: String): UserEntity?

    /**
     * Retrieves all users as a flow of a list of [UserEntity].
     *
     * @return A flow of a list of all user entities.
     */
    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserEntity>>

    /**
     * Removes the current user by setting the `isCurrentUser` flag to false.
     *
     * @return The number of users updated.
     */
    @Query("UPDATE users SET isCurrentUser=0")
    suspend fun removeCurrentUser(): Int

    /**
     * Removes users that have a state and access token, but no client ID and client secret.
     */
    @Query(
        "DELETE FROM users WHERE state IS NOT NULL AND accessToken IS NOT" +
            " NULL AND clientId IS NULL AND clientSecret IS NULL"
    )
    suspend fun removeUsersWithStateAndTokenAndNoClientIdAndSecret()

    /**
     * Removes users that have a state but no access token.
     */
    @Query("DELETE FROM users WHERE state IS NOT NULL AND accessToken IS NULL")
    suspend fun removeUsersWithStateAndNoToken()

    /**
     * Inserts or replaces a user entity.
     *
     * @param userEntity The user entity to be saved.
     * @return The ID of the saved user entity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(userEntity: UserEntity): Long

    /**
     * Updates a user entity.
     *
     * @param userEntity The user entity to be updated.
     * @return The number of users updated.
     */
    @Update
    suspend fun updateUser(userEntity: UserEntity): Int
}
