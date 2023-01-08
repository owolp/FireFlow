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

package dev.zitech.core.persistence.data.repository.database

import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.model.exception.NullCurrentUserAccountException
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import dev.zitech.core.persistence.domain.source.database.UserAccountDatabaseSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserAccountRepositoryImpl @Inject constructor(
    private val userAccountDatabaseSource: UserAccountDatabaseSource
) : UserAccountRepository {

    override fun getUserAccounts(): Flow<DataResult<List<UserAccount>>> =
        userAccountDatabaseSource.getUserAccounts()
            .map { userAccounts ->
                DataResult.Success(userAccounts)
            }

    override fun getCurrentUserAccount(): Flow<DataResult<UserAccount>> =
        userAccountDatabaseSource.getCurrentUserAccountOrNull()
            .map { userAccount ->
                if (userAccount != null) {
                    DataResult.Success(userAccount)
                } else {
                    DataResult.Error(cause = NullCurrentUserAccountException)
                }
            }

    override suspend fun saveUserAccount(isCurrentUserAccount: Boolean): DataResult<Long> =
        try {
            val id = userAccountDatabaseSource.saveUserAccount(isCurrentUserAccount)
            DataResult.Success(id)
        } catch (exception: Exception) {
            DataResult.Error(cause = exception)
        }
}
