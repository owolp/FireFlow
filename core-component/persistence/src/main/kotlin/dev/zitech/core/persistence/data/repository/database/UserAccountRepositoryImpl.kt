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

import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.core.persistence.R
import dev.zitech.core.persistence.domain.model.cache.InMemoryCache
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.model.database.UserLoggedState
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import dev.zitech.core.persistence.domain.source.database.UserAccountDatabaseSource
import javax.inject.Inject

internal class UserAccountRepositoryImpl @Inject constructor(
    private val userAccountInMemoryCache: InMemoryCache<UserAccount>,
    private val userAccountDatabaseSource: UserAccountDatabaseSource,
    private val stringsProvider: StringsProvider
) : UserAccountRepository {

    companion object {
        private const val TAG = "UserAccountRepository"
    }

    override suspend fun getUserAccounts(): DataResult<List<UserAccount>> =
        try {
            val userAccounts = userAccountDatabaseSource.getUserAccounts()
            DataResult.Success(userAccounts)
        } catch (exception: Exception) {
            DataResult.Error(cause = exception)
        }

    override suspend fun getCurrentUserAccount(): DataResult<UserAccount> {
        val currentUserAccount = userAccountInMemoryCache.data

        return if (currentUserAccount != null) {
            DataResult.Success(currentUserAccount)
        } else {
            try {
                val userAccount = userAccountDatabaseSource.getCurrentUserAccount()
                if (userAccount != null) {
                    userAccountInMemoryCache.data = userAccount
                    Logger.d(TAG, "db")
                    DataResult.Success(userAccount)
                } else {
                    Logger.e(TAG, message = "Current user is null")
                    DataResult.Error(
                        message = stringsProvider(R.string.error_message_current_user_null)
                    )
                }
            } catch (exception: Exception) {
                DataResult.Error(cause = exception)
            }
        }
    }

    @Suppress("SwallowedException")
    override suspend fun getUserLoggedState(): UserLoggedState =
        try {
            userAccountDatabaseSource.isUserLoggedIn()
            if (userAccountDatabaseSource.isUserLoggedIn()) {
                UserLoggedState.LOGGED_IN
            } else {
                UserLoggedState.LOGGED_OUT
            }
        } catch (exception: Exception) {
            UserLoggedState.LOGGED_OUT
        }

    override suspend fun saveUserAccount(isCurrentUserAccount: Boolean): DataResult<Long> =
        try {
            val id = userAccountDatabaseSource.saveUserAccount(isCurrentUserAccount)
            DataResult.Success(id)
        } catch (exception: Exception) {
            DataResult.Error(cause = exception)
        }
}
