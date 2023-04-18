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

@Dao
internal interface UserAccountDao {

    @Query("SELECT * FROM user_accounts WHERE isCurrentUserAccount=1 ORDER BY id DESC LIMIT 1")
    fun getCurrentUserAccount(): Flow<UserAccountEntity?>

    @Query("SELECT * FROM user_accounts WHERE state=:state")
    suspend fun getUserAccountByState(state: String): UserAccountEntity?

    @Query("SELECT * FROM user_accounts")
    fun getUserAccounts(): Flow<List<UserAccountEntity>>

    @Query("UPDATE user_accounts SET isCurrentUserAccount=0")
    suspend fun removeCurrentUserAccount(): Int

    @Query(
        "DELETE FROM user_accounts WHERE state IS NOT NULL AND accessToken IS NOT" +
            " NULL AND clientId IS NULL AND clientSecret IS NULL"
    )
    suspend fun removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret()

    @Query("DELETE FROM user_accounts WHERE state IS NOT NULL AND accessToken IS NULL")
    suspend fun removeUserAccountsWithStateAndNoToken()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserAccount(userAccountEntity: UserAccountEntity): Long

    @Update
    suspend fun updateUserAccount(userAccountEntity: UserAccountEntity): Int
}
