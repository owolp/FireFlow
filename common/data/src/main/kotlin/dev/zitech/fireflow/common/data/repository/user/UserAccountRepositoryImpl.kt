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

package dev.zitech.fireflow.common.data.repository.user

import dev.zitech.fireflow.common.data.memory.cache.InMemoryCache
import dev.zitech.fireflow.common.data.source.user.UserAccountSource
import dev.zitech.fireflow.common.domain.model.user.UserAccount
import dev.zitech.fireflow.common.domain.repository.user.UserAccountRepository
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.work.OperationResult
import dev.zitech.fireflow.core.work.OperationResult.Failure
import dev.zitech.fireflow.core.work.OperationResult.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserAccountRepositoryImpl @Inject constructor(
    private val networkDetailsInMemoryCache: InMemoryCache<NetworkDetails>,
    private val userAccountDatabaseSource: UserAccountSource
) : UserAccountRepository {

    override fun getCurrentUserAccount(): Flow<OperationResult<UserAccount>> =
        userAccountDatabaseSource.getCurrentUserAccount()
            .map { userAccountResult ->
                when (userAccountResult) {
                    is Success -> {
                        networkDetailsInMemoryCache.data = NetworkDetails(
                            userId = userAccountResult.data.userId,
                            serverAddress = userAccountResult.data.serverAddress
                        )
                    }

                    is Failure -> {
                        // NO_OP
                    }
                }
                userAccountResult
            }

    override suspend fun getUserAccountByState(state: String): OperationResult<UserAccount> =
        userAccountDatabaseSource.getUserAccountByState(state)

    override fun getUserAccounts(): Flow<OperationResult<List<UserAccount>>> =
        userAccountDatabaseSource.getUserAccounts()

    override suspend fun removeUserAccountsWithStateAndNoToken(): OperationResult<Unit> =
        userAccountDatabaseSource.removeUserAccountsWithStateAndNoToken()

    override suspend fun removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret(): OperationResult<Unit> =
        userAccountDatabaseSource.removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret()

    override suspend fun saveUserAccount(
        accessToken: String?,
        clientId: String?,
        clientSecret: String?,
        isCurrentUserAccount: Boolean,
        serverAddress: String,
        state: String
    ): OperationResult<Long> {
        val saveUserAccountResult = userAccountDatabaseSource.saveUserAccount(
            accessToken = accessToken,
            clientId = clientId,
            clientSecret = clientSecret,
            isCurrentUserAccount = isCurrentUserAccount,
            serverAddress = serverAddress,
            state = state
        )
        when (saveUserAccountResult) {
            is Success -> {
                networkDetailsInMemoryCache.data = NetworkDetails(
                    userId = saveUserAccountResult.data,
                    serverAddress = serverAddress
                )
            }

            is Failure -> {
                // NO_OP
            }
        }

        return saveUserAccountResult
    }

    override suspend fun updateUserAccount(userAccount: UserAccount): OperationResult<Unit> =
        when (
            val updateUserAccountResult = userAccountDatabaseSource.updateUserAccount(
                userAccount
            )
        ) {
            is Success -> {
                if (updateUserAccountResult.data != NO_WORKER_UPDATED_RESULT) {
                    Success(Unit)
                } else {
                    Failure(Error.NullUserAccount)
                }
            }

            is Failure -> Failure(updateUserAccountResult.error)
        }

    private companion object {
        const val NO_WORKER_UPDATED_RESULT = 0
    }
}
