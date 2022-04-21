/*
 * Copyright (C) 2022 Zitech
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

package dev.zitech.core.storage.framework.database.user

import androidx.room.Transaction
import dev.zitech.core.storage.data.database.dao.UserAccountDao
import dev.zitech.core.storage.data.database.entity.UserAccountEntity
import dev.zitech.core.storage.data.database.mapper.UserAccountMapper
import dev.zitech.core.storage.domain.model.UserAccount
import javax.inject.Inject

internal class UserAccountDatabaseSourceImpl @Inject constructor(
    private val userAccountDao: UserAccountDao,
    private val userAccountMapper: UserAccountMapper,
) : UserAccountDatabaseSource {

    override suspend fun getUserAccounts(): List<UserAccount> =
        userAccountDao.getUserAccounts().map { userAccountEntity ->
            userAccountMapper(userAccountEntity)
        }

    override suspend fun getCurrentUserAccount(): UserAccount? =
        userAccountDao.getCurrentUserAccount()?.let {
            userAccountMapper(it)
        }

    @Transaction
    override suspend fun saveUserAccount(isCurrentUserAccount: Boolean): Long {
        userAccountDao.removeCurrentUserAccount()
        return userAccountDao.saveUserAccount(
            UserAccountEntity(
                isCurrentUserAccount = isCurrentUserAccount
            )
        )
    }
}
