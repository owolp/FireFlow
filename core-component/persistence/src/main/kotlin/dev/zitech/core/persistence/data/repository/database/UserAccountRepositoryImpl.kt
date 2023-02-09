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

package dev.zitech.core.persistence.data.repository.database

import dev.zitech.core.common.domain.cache.InMemoryCache
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.model.exception.NullCurrentUserAccountException
import dev.zitech.core.persistence.domain.model.exception.NullUserAccountException
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import dev.zitech.core.persistence.domain.source.database.UserAccountDatabaseSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserAccountRepositoryImpl @Inject constructor(
    private val userAccountDatabaseSource: UserAccountDatabaseSource,
    private val currentUserServerAddressInMemoryCache: InMemoryCache<String>
) : UserAccountRepository {

    override suspend fun getUserAccountByState(state: String): DataResult<UserAccount> =
        userAccountDatabaseSource.getUserAccountByStateOrNull(state)?.let { userAccount ->
            DataResult.Success(userAccount)
        } ?: DataResult.Error(cause = NullUserAccountException)

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

    override suspend fun saveUserAccount(
        clientId: String,
        clientSecret: String,
        isCurrentUserAccount: Boolean,
        oauthCode: String?,
        serverAddress: String,
        state: String?,
        userId: Long?
    ): DataResult<Long> =
        try {
            val resultId = userAccountDatabaseSource.saveUserAccount(
                clientId = clientId,
                clientSecret = clientSecret,
                isCurrentUserAccount = isCurrentUserAccount,
                oauthCode = oauthCode,
                serverAddress = serverAddress,
                state = state,
                userId = userId
            )
            currentUserServerAddressInMemoryCache.data = serverAddress
            DataResult.Success(resultId)
        } catch (exception: Exception) {
            DataResult.Error(cause = exception)
        }
}
