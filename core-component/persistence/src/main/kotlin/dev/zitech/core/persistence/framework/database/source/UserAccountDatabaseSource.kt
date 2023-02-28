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

package dev.zitech.core.persistence.framework.database.source

import dev.zitech.core.common.domain.exception.FireFlowException
import dev.zitech.core.common.domain.model.DataError
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.domain.model.DataSuccess
import dev.zitech.core.persistence.data.source.UserAccountSource
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.framework.database.dao.UserAccountDao
import dev.zitech.core.persistence.framework.database.entity.UserAccountEntity
import dev.zitech.core.persistence.framework.database.mapper.UserAccountMapper
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class UserAccountDatabaseSource @Inject constructor(
    private val userAccountDao: UserAccountDao,
    private val userAccountMapper: UserAccountMapper
) : UserAccountSource {

    override suspend fun getUserAccountByState(state: String): DataResult<UserAccount> = try {
        val userAccountEntity = userAccountDao.getUserAccountByState(state)
        if (userAccountEntity != null) {
            DataSuccess(userAccountMapper.toDomain(userAccountEntity))
        } else {
            DataError(FireFlowException.NullUserAccountByState)
        }
    } catch (throwable: Throwable) {
        DataError(FireFlowException.DataException(throwable))
    }

    override fun getUserAccounts(): Flow<DataResult<List<UserAccount>>> =
        userAccountDao.getUserAccounts().map { userAccountEntities ->
            DataSuccess(userAccountEntities.map(userAccountMapper::toDomain))
        }.catch { throwable ->
            DataError<List<UserAccount>>(FireFlowException.DataException(throwable))
        }

    override fun getCurrentUserAccount(): Flow<DataResult<UserAccount>> =
        userAccountDao.getCurrentUserAccount().map { userAccountEntity ->
            if (userAccountEntity != null) {
                DataSuccess(userAccountMapper.toDomain(userAccountEntity))
            } else {
                DataError(FireFlowException.NullCurrentUserAccount)
            }
        }.catch { throwable ->
            DataError<UserAccount>(FireFlowException.DataException(throwable))
        }

    override suspend fun saveUserAccount(
        clientId: String,
        clientSecret: String,
        isCurrentUserAccount: Boolean,
        serverAddress: String,
        state: String
    ): DataResult<Long> = try {
        val userAccountId = userAccountDao.saveUserAccount(
            UserAccountEntity(
                clientId = clientId,
                clientSecret = clientSecret,
                isCurrentUserAccount = isCurrentUserAccount,
                serverAddress = serverAddress,
                state = state
            )
        )
        DataSuccess(userAccountId)
    } catch (throwable: Throwable) {
        DataError(FireFlowException.DataException(throwable))
    }

    override suspend fun removeUserAccountsWithStateAndWithoutAccessToken() =
        userAccountDao.removeUserAccountsWithStateAndWithoutAccessToken()

    override suspend fun updateUserAccount(userAccount: UserAccount): Int =
        userAccountDao.updateUserAccount(userAccountMapper.toEntity(userAccount))
}
