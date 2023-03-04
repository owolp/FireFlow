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
import dev.zitech.core.common.domain.exception.FireFlowException
import dev.zitech.core.common.domain.model.DataError
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.domain.model.DataSuccess
import dev.zitech.core.persistence.data.source.UserAccountSource
import dev.zitech.core.persistence.domain.model.cache.NetworkDetails
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserAccountRepositoryImpl @Inject constructor(
    private val userAccountDatabaseSource: UserAccountSource,
    private val networkDetailsInMemoryCache: InMemoryCache<NetworkDetails>
) : UserAccountRepository {

    private companion object {
        const val NO_WORKER_UPDATED_RESULT = 0
    }

    override suspend fun getUserAccountByState(state: String): DataResult<UserAccount> =
        userAccountDatabaseSource.getUserAccountByState(state)

    override fun getUserAccounts(): Flow<DataResult<List<UserAccount>>> =
        userAccountDatabaseSource.getUserAccounts()

    override fun getCurrentUserAccount(): Flow<DataResult<UserAccount>> =
        userAccountDatabaseSource.getCurrentUserAccount()
            .map { userAccountResult ->
                when (userAccountResult) {
                    is DataSuccess -> {
                        networkDetailsInMemoryCache.data = NetworkDetails(
                            userId = userAccountResult.data.userId,
                            serverAddress = userAccountResult.data.serverAddress
                        )
                    }
                    is DataError -> {
                        // NO_OP
                    }
                }
                userAccountResult
            }

    override suspend fun saveUserAccount(
        clientId: String,
        clientSecret: String,
        isCurrentUserAccount: Boolean,
        serverAddress: String,
        state: String
    ): DataResult<Long> {
        val saveUserAccountResult = userAccountDatabaseSource.saveUserAccount(
            clientId = clientId,
            clientSecret = clientSecret,
            isCurrentUserAccount = isCurrentUserAccount,
            serverAddress = serverAddress,
            state = state
        )
        when (saveUserAccountResult) {
            is DataSuccess -> {
                networkDetailsInMemoryCache.data = NetworkDetails(
                    userId = saveUserAccountResult.data,
                    serverAddress = serverAddress
                )
            }
            is DataError -> {
                // NO_OP
            }
        }

        return saveUserAccountResult
    }

    override suspend fun removeStaleUserAccounts(): DataResult<Unit> =
        userAccountDatabaseSource.removeUserAccountsWithStateAndWithoutAccessToken()

    override suspend fun updateUserAccount(userAccount: UserAccount): DataResult<Unit> =
        when (
            val updateUserAccountResult = userAccountDatabaseSource.updateUserAccount(
                userAccount
            )
        ) {
            is DataSuccess -> {
                if (updateUserAccountResult.data != NO_WORKER_UPDATED_RESULT) {
                    DataSuccess(Unit)
                } else {
                    DataError(FireFlowException.NullUserAccount)
                }
            }
            is DataError -> DataError(updateUserAccountResult.fireFlowException)
        }
}
