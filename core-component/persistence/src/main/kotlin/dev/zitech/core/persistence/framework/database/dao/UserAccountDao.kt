/*
 * Copyright (C) 2022 Zitech Ltd.
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

package dev.zitech.core.persistence.framework.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.zitech.core.persistence.framework.database.entity.UserAccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface UserAccountDao {

    @Query("SELECT * FROM user_accounts")
    suspend fun getUserAccounts(): List<UserAccountEntity>

    @Query("SELECT * FROM user_accounts WHERE isCurrentUserAccount = 1 ORDER BY id DESC LIMIT 1")
    fun getCurrentUserAccountFlow(): Flow<UserAccountEntity>

    @Query("SELECT * FROM user_accounts WHERE isCurrentUserAccount = 1 ORDER BY id DESC LIMIT 1")
    suspend fun getCurrentUserAccount(): UserAccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserAccount(userAccountEntity: UserAccountEntity): Long

    @Query("UPDATE user_accounts SET isCurrentUserAccount=0")
    suspend fun removeCurrentUserAccount(): Int
}
