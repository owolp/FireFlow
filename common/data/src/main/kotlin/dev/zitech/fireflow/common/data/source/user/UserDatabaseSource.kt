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

import dev.zitech.fireflow.common.data.local.database.dao.UserDao
import dev.zitech.fireflow.common.data.local.database.entity.UserEntity
import dev.zitech.fireflow.common.data.local.database.handleDb
import dev.zitech.fireflow.common.data.local.database.mapper.UserEntityMapper
import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.error.Error.Fatal.Type.DISK
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class UserDatabaseSource @Inject constructor(
    private val userDao: UserDao,
    private val userEntityMapper: UserEntityMapper
) : UserSource {

    override suspend fun checkUserExistsByIdentifier(identifier: String): OperationResult<Boolean> =
        handleDb {
            userDao.checkUserExistsByIdentifier(identifier)
        }

    override suspend fun checkUserExistsByIdentifierAndServerAddress(
        identifier: String,
        serverAddress: String
    ): OperationResult<Boolean> = handleDb {
        userDao.checkUserExistsByIdentifierAndServerAddress(identifier, serverAddress)
    }

    override suspend fun deleteUserById(userId: Long): OperationResult<Int> = handleDb {
        userDao.deleteUserById(userId)
    }

    override fun getCurrentUser(): Flow<OperationResult<User>> =
        userDao.getCurrentUser().map { userEntity ->
            if (userEntity != null) {
                Success(userEntityMapper.toDomain(userEntity))
            } else {
                Failure(Error.NullCurrentUser)
            }
        }.catch { throwable ->
            Failure<User>(Error.Fatal(throwable, DISK))
        }

    override suspend fun getUserByState(state: String): OperationResult<User> = try {
        val userEntity = userDao.getUserByState(state)
        if (userEntity != null) {
            Success(userEntityMapper.toDomain(userEntity))
        } else {
            Failure(Error.NullUserByState)
        }
    } catch (throwable: Throwable) {
        Failure(Error.Fatal(throwable, DISK))
    }

    override fun getUsers(): Flow<OperationResult<List<User>>> =
        userDao.getUsers().map { userEntities ->
            Success(userEntities.map(userEntityMapper::toDomain))
        }.catch { throwable ->
            Failure<List<User>>(Error.Fatal(throwable, DISK))
        }

    override suspend fun removeCurrentUserOrUsers(): OperationResult<Int> =
        handleDb {
            userDao.removeCurrentUserOrUsers()
        }

    override suspend fun removeUsersWithStateAndNoTokenAndIdentifier(): OperationResult<Int> =
        handleDb {
            userDao.removeUsersWithStateAndNoTokenAndIdentifier()
        }

    override suspend fun removeUsersWithStateAndTokenAndNoClientIdAndSecretAndIdentifier(): OperationResult<Int> =
        handleDb {
            userDao.removeUsersWithStateAndTokenAndNoClientIdAndSecretAndIdentifier()
        }

    override suspend fun removeUsersWithTokenAndNoIdentifier(): OperationResult<Int> = handleDb {
        userDao.removeUsersWithTokenAndNoIdentifier()
    }

    override suspend fun saveUser(
        accessToken: String?,
        clientId: String?,
        clientSecret: String?,
        connectivityNotification: Boolean,
        identifier: String?,
        isCurrentUser: Boolean,
        serverAddress: String?,
        state: String
    ): OperationResult<Long> = handleDb {
        userDao.saveUser(
            UserEntity(
                accessToken = accessToken,
                clientId = clientId,
                clientSecret = clientSecret,
                connectivityNotification = connectivityNotification,
                identifier = identifier,
                isCurrentUser = isCurrentUser,
                serverAddress = serverAddress,
                state = state
            )
        )
    }

    override suspend fun updateUser(user: User): OperationResult<Int> =
        handleDb {
            userDao.updateUser(userEntityMapper.toEntity(user))
        }

    override suspend fun updateUserCurrentStatus(userId: Long): OperationResult<Int> =
        handleDb {
            userDao.updateUserCurrentStatus(userId)
        }
}
