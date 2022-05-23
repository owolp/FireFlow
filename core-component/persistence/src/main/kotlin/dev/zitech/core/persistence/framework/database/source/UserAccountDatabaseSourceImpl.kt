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

package dev.zitech.core.persistence.framework.database.source

import androidx.room.Transaction
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.source.database.UserAccountDatabaseSource
import dev.zitech.core.persistence.framework.database.dao.UserAccountDao
import dev.zitech.core.persistence.framework.database.entity.UserAccountEntity
import dev.zitech.core.persistence.framework.database.mapper.UserAccountMapper
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
