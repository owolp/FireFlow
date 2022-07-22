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

package dev.zitech.core.persistence.domain.repository.database

import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.model.database.UserLoggedState
import kotlinx.coroutines.flow.Flow

interface UserAccountRepository {

    fun getCurrentUserAccount(): Flow<DataResult<UserAccount>>
    suspend fun getUserAccounts(): DataResult<List<UserAccount>>
    suspend fun getUserLoggedState(): UserLoggedState
    suspend fun saveUserAccount(isCurrentUserAccount: Boolean): DataResult<Long>
    suspend fun updateCurrentUserAccountTheme(theme: UserAccount.Theme): DataResult<Unit>
}
