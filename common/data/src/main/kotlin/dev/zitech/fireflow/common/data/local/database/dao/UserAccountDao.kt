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
import dev.zitech.fireflow.common.data.local.database.entity.UserAccountEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) interface for accessing user account data.
 */
@Dao
internal interface UserAccountDao {

    /**
     * Retrieves the current user account as a flow of [UserAccountEntity].
     *
     * @return A flow of the current user account entity, or null if not found.
     */
    @Query("SELECT * FROM user_accounts WHERE isCurrentUserAccount=1 ORDER BY id DESC LIMIT 1")
    fun getCurrentUserAccount(): Flow<UserAccountEntity?>

    /**
     * Retrieves a user account by its state value.
     *
     * @param state The state value of the user account.
     * @return The user account entity matching the specified state, or null if not found.
     */
    @Query("SELECT * FROM user_accounts WHERE state=:state")
    suspend fun getUserAccountByState(state: String): UserAccountEntity?

    /**
     * Retrieves all user accounts as a flow of a list of [UserAccountEntity].
     *
     * @return A flow of a list of all user account entities.
     */
    @Query("SELECT * FROM user_accounts")
    fun getUserAccounts(): Flow<List<UserAccountEntity>>

    /**
     * Removes the current user account by setting the `isCurrentUserAccount` flag to false.
     *
     * @return The number of user accounts updated.
     */
    @Query("UPDATE user_accounts SET isCurrentUserAccount=0")
    suspend fun removeCurrentUserAccount(): Int

    /**
     * Removes user accounts that have a state and access token, but no client ID and client secret.
     */
    @Query(
        "DELETE FROM user_accounts WHERE state IS NOT NULL AND accessToken IS NOT" +
            " NULL AND clientId IS NULL AND clientSecret IS NULL"
    )
    suspend fun removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret()

    /**
     * Removes user accounts that have a state but no access token.
     */
    @Query("DELETE FROM user_accounts WHERE state IS NOT NULL AND accessToken IS NULL")
    suspend fun removeUserAccountsWithStateAndNoToken()

    /**
     * Inserts or replaces a user account entity.
     *
     * @param userAccountEntity The user account entity to be saved.
     * @return The ID of the saved user account entity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserAccount(userAccountEntity: UserAccountEntity): Long

    /**
     * Updates a user account entity.
     *
     * @param userAccountEntity The user account entity to be updated.
     * @return The number of user accounts updated.
     */
    @Update
    suspend fun updateUserAccount(userAccountEntity: UserAccountEntity): Int
}
