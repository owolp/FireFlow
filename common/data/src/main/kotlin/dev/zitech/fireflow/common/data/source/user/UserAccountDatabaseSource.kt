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

package dev.zitech.fireflow.common.data.source.user

import dev.zitech.fireflow.common.data.local.database.dao.UserAccountDao
import dev.zitech.fireflow.common.data.local.database.entity.UserAccountEntity
import dev.zitech.fireflow.common.data.local.database.handleDb
import dev.zitech.fireflow.common.data.local.database.mapper.UserAccountEntityMapper
import dev.zitech.fireflow.common.domain.model.user.UserAccount
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.error.Error.Fatal.Type.DISK
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class UserAccountDatabaseSource @Inject constructor(
    private val userAccountDao: UserAccountDao,
    private val userAccountEntityMapper: UserAccountEntityMapper
) : UserAccountSource {

    override fun getCurrentUserAccount(): Flow<OperationResult<UserAccount>> =
        userAccountDao.getCurrentUserAccount().map { userAccountEntity ->
            if (userAccountEntity != null) {
                Success(userAccountEntityMapper.toDomain(userAccountEntity))
            } else {
                Failure(Error.NullCurrentUserAccount)
            }
        }.catch { throwable ->
            Failure<UserAccount>(Error.Fatal(throwable, DISK))
        }

    override suspend fun getUserAccountByState(state: String): OperationResult<UserAccount> = try {
        val userAccountEntity = userAccountDao.getUserAccountByState(state)
        if (userAccountEntity != null) {
            Success(userAccountEntityMapper.toDomain(userAccountEntity))
        } else {
            Failure(Error.NullUserAccountByState)
        }
    } catch (throwable: Throwable) {
        Failure(Error.Fatal(throwable, DISK))
    }

    override fun getUserAccounts(): Flow<OperationResult<List<UserAccount>>> =
        userAccountDao.getUserAccounts().map { userAccountEntities ->
            Success(userAccountEntities.map(userAccountEntityMapper::toDomain))
        }.catch { throwable ->
            Failure<List<UserAccount>>(Error.Fatal(throwable, DISK))
        }

    override suspend fun removeUserAccountsWithStateAndNoToken(): OperationResult<Unit> =
        handleDb {
            userAccountDao.removeUserAccountsWithStateAndNoToken()
        }

    override suspend fun removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret(): OperationResult<Unit> =
        handleDb {
            userAccountDao.removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret()
        }

    override suspend fun saveUserAccount(
        accessToken: String?,
        clientId: String?,
        clientSecret: String?,
        isCurrentUserAccount: Boolean,
        serverAddress: String,
        state: String
    ): OperationResult<Long> = handleDb {
        userAccountDao.saveUserAccount(
            UserAccountEntity(
                accessToken = accessToken,
                clientId = clientId,
                clientSecret = clientSecret,
                isCurrentUserAccount = isCurrentUserAccount,
                serverAddress = serverAddress,
                state = state
            )
        )
    }

    override suspend fun updateUserAccount(userAccount: UserAccount): OperationResult<Int> =
        handleDb {
            userAccountDao.updateUserAccount(userAccountEntityMapper.toEntity(userAccount))
        }
}
