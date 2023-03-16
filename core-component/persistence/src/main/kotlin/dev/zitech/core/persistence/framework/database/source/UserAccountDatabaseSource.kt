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

import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.domain.error.Error.Fatal.Type.DISK
import dev.zitech.core.common.domain.model.Work
import dev.zitech.core.common.domain.model.WorkError
import dev.zitech.core.common.domain.model.WorkSuccess
import dev.zitech.core.persistence.data.source.UserAccountSource
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.framework.database.dao.UserAccountDao
import dev.zitech.core.persistence.framework.database.entity.UserAccountEntity
import dev.zitech.core.persistence.framework.database.handleDb
import dev.zitech.core.persistence.framework.database.mapper.UserAccountMapper
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class UserAccountDatabaseSource @Inject constructor(
    private val userAccountDao: UserAccountDao,
    private val userAccountMapper: UserAccountMapper
) : UserAccountSource {

    override fun getCurrentUserAccount(): Flow<Work<UserAccount>> =
        userAccountDao.getCurrentUserAccount().map { userAccountEntity ->
            if (userAccountEntity != null) {
                WorkSuccess(userAccountMapper.toDomain(userAccountEntity))
            } else {
                WorkError(Error.NullCurrentUserAccount)
            }
        }.catch { throwable ->
            WorkError<UserAccount>(Error.Fatal(throwable, DISK))
        }

    override suspend fun getUserAccountByState(state: String): Work<UserAccount> = try {
        val userAccountEntity = userAccountDao.getUserAccountByState(state)
        if (userAccountEntity != null) {
            WorkSuccess(userAccountMapper.toDomain(userAccountEntity))
        } else {
            WorkError(Error.NullUserAccountByState)
        }
    } catch (throwable: Throwable) {
        WorkError(Error.Fatal(throwable, DISK))
    }

    override fun getUserAccounts(): Flow<Work<List<UserAccount>>> =
        userAccountDao.getUserAccounts().map { userAccountEntities ->
            WorkSuccess(userAccountEntities.map(userAccountMapper::toDomain))
        }.catch { throwable ->
            WorkError<List<UserAccount>>(Error.Fatal(throwable, DISK))
        }

    override suspend fun removeUserAccountsWithStateAndNoToken(): Work<Unit> =
        handleDb {
            userAccountDao.removeUserAccountsWithStateAndNoToken()
        }

    override suspend fun removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret(): Work<Unit> =
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
    ): Work<Long> = handleDb {
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

    override suspend fun updateUserAccount(userAccount: UserAccount): Work<Int> =
        handleDb {
            userAccountDao.updateUserAccount(userAccountMapper.toEntity(userAccount))
        }
}
