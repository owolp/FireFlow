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
import dev.zitech.fireflow.core.work.Work
import dev.zitech.fireflow.core.work.WorkError
import dev.zitech.fireflow.core.work.WorkSuccess
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserAccountRepositoryImpl @Inject constructor(
    private val networkDetailsInMemoryCache: InMemoryCache<NetworkDetails>,
    private val userAccountDatabaseSource: UserAccountSource
) : UserAccountRepository {

    override fun getCurrentUserAccount(): Flow<Work<UserAccount>> =
        userAccountDatabaseSource.getCurrentUserAccount()
            .map { userAccountResult ->
                when (userAccountResult) {
                    is WorkSuccess -> {
                        networkDetailsInMemoryCache.data = NetworkDetails(
                            userId = userAccountResult.data.userId,
                            serverAddress = userAccountResult.data.serverAddress
                        )
                    }

                    is WorkError -> {
                        // NO_OP
                    }
                }
                userAccountResult
            }

    override suspend fun getUserAccountByState(state: String): Work<UserAccount> =
        userAccountDatabaseSource.getUserAccountByState(state)

    override fun getUserAccounts(): Flow<Work<List<UserAccount>>> =
        userAccountDatabaseSource.getUserAccounts()

    override suspend fun removeUserAccountsWithStateAndNoToken(): Work<Unit> =
        userAccountDatabaseSource.removeUserAccountsWithStateAndNoToken()

    override suspend fun removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret(): Work<Unit> =
        userAccountDatabaseSource.removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret()

    override suspend fun saveUserAccount(
        accessToken: String?,
        clientId: String?,
        clientSecret: String?,
        isCurrentUserAccount: Boolean,
        serverAddress: String,
        state: String
    ): Work<Long> {
        val saveUserAccountResult = userAccountDatabaseSource.saveUserAccount(
            accessToken = accessToken,
            clientId = clientId,
            clientSecret = clientSecret,
            isCurrentUserAccount = isCurrentUserAccount,
            serverAddress = serverAddress,
            state = state
        )
        when (saveUserAccountResult) {
            is WorkSuccess -> {
                networkDetailsInMemoryCache.data = NetworkDetails(
                    userId = saveUserAccountResult.data,
                    serverAddress = serverAddress
                )
            }

            is WorkError -> {
                // NO_OP
            }
        }

        return saveUserAccountResult
    }

    override suspend fun updateUserAccount(userAccount: UserAccount): Work<Unit> =
        when (
            val updateUserAccountResult = userAccountDatabaseSource.updateUserAccount(
                userAccount
            )
        ) {
            is WorkSuccess -> {
                if (updateUserAccountResult.data != NO_WORKER_UPDATED_RESULT) {
                    WorkSuccess(Unit)
                } else {
                    WorkError(Error.NullUserAccount)
                }
            }

            is WorkError -> WorkError(updateUserAccountResult.error)
        }

    private companion object {
        const val NO_WORKER_UPDATED_RESULT = 0
    }
}
