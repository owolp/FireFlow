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

package dev.zitech.core.persistence.data.source

import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.persistence.domain.model.database.UserAccount
import kotlinx.coroutines.flow.Flow

internal interface UserAccountSource {

    suspend fun getUserAccountByState(state: String): DataResult<out UserAccount>
    fun getUserAccounts(): Flow<List<UserAccount>>
    fun getCurrentUserAccountOrNull(): Flow<UserAccount?>
    suspend fun removeUserAccountsWithStateAndWithoutAccessToken()
    suspend fun saveUserAccount(
        clientId: String,
        clientSecret: String,
        isCurrentUserAccount: Boolean,
        serverAddress: String,
        state: String
    ): Long

    suspend fun updateUserAccount(userAccount: UserAccount): Int
}
