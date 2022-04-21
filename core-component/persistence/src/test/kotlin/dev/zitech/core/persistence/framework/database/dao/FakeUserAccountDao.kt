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

import dev.zitech.core.persistence.data.database.dao.UserAccountDao
import dev.zitech.core.persistence.data.database.entity.UserAccountEntity

internal class FakeUserAccountDao : UserAccountDao {

    private val userAccountEntityList = mutableListOf<UserAccountEntity>()

    override suspend fun getUserAccounts(): List<UserAccountEntity> = userAccountEntityList

    override suspend fun getCurrentUserAccount(): UserAccountEntity? =
        userAccountEntityList.firstOrNull { it.isCurrentUserAccount }

    override suspend fun saveUserAccount(userAccountEntity: UserAccountEntity): Long {
        userAccountEntityList.add(userAccountEntity)
        return 1
    }

    override suspend fun removeCurrentUserAccount(): Int {
        val currentUserAccount = getCurrentUserAccount()
        val isRemoved = userAccountEntityList.remove(currentUserAccount)
        return if (isRemoved) {
            1
        } else {
            -1
        }
    }

    fun containsCurrentUserAccountEntity(): Boolean =
        userAccountEntityList.firstOrNull { it.isCurrentUserAccount } != null
}
