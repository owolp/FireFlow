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
import dev.zitech.fireflow.common.data.source.user.UserSource
import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.common.domain.repository.user.UserRepository
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserRepositoryImpl @Inject constructor(
    private val networkDetailsInMemoryCache: InMemoryCache<NetworkDetails>,
    private val userDatabaseSource: UserSource
) : UserRepository {

    override suspend fun checkUserExistsByIdentifier(identifier: String): OperationResult<Boolean> =
        userDatabaseSource.checkUserExistsByIdentifier(identifier)

    override suspend fun checkUserExistsByIdentifierAndServerAddress(
        identifier: String,
        serverAddress: String
    ): OperationResult<Boolean> =
        userDatabaseSource.checkUserExistsByIdentifierAndServerAddress(identifier, serverAddress)

    override suspend fun deleteUserById(userId: Long): OperationResult<Int> =
        userDatabaseSource.deleteUserById(userId)

    override fun getCurrentUser(): Flow<OperationResult<User>> =
        userDatabaseSource.getCurrentUser()
            .map { userResult ->
                when (userResult) {
                    is Success -> {
                        when (val user = userResult.data) {
                            is User.Local -> {
                                // NO_OP
                            }
                            is User.Remote -> {
                                networkDetailsInMemoryCache.data = NetworkDetails(
                                    userId = user.id,
                                    serverAddress = user.serverAddress
                                )
                            }
                        }
                    }
                    is Failure -> {
                        // NO_OP
                    }
                }
                userResult
            }

    override suspend fun getUserByState(state: String): OperationResult<User> =
        userDatabaseSource.getUserByState(state)

    override fun getUsers(): Flow<OperationResult<List<User>>> =
        userDatabaseSource.getUsers()

    override suspend fun removeCurrentUsers(): OperationResult<Int> =
        userDatabaseSource.removeCurrentUserOrUsers()

    override suspend fun removeUsersWithStateAndNoTokenAndIdentifier(): OperationResult<Int> =
        userDatabaseSource.removeUsersWithStateAndNoTokenAndIdentifier()

    override suspend fun removeUsersWithStateAndTokenAndNoClientIdAndSecretAndIdentifier(): OperationResult<Int> =
        userDatabaseSource.removeUsersWithStateAndTokenAndNoClientIdAndSecretAndIdentifier()

    override suspend fun removeUsersWithTokenAndNoIdentifier(): OperationResult<Int> =
        userDatabaseSource.removeUsersWithTokenAndNoIdentifier()

    override suspend fun saveUser(
        accessToken: String?,
        clientId: String?,
        clientSecret: String?,
        connectivityNotification: Boolean,
        identifier: String?,
        isCurrentUser: Boolean,
        serverAddress: String?,
        state: String
    ): OperationResult<Long> {
        val saveUserResult = userDatabaseSource.saveUser(
            accessToken = accessToken,
            clientId = clientId,
            clientSecret = clientSecret,
            connectivityNotification = connectivityNotification,
            identifier = identifier,
            isCurrentUser = isCurrentUser,
            serverAddress = serverAddress,
            state = state
        )
        when (saveUserResult) {
            is Success -> {
                if (serverAddress != null) {
                    networkDetailsInMemoryCache.data = NetworkDetails(
                        userId = saveUserResult.data,
                        serverAddress = serverAddress
                    )
                }
            }
            is Failure -> {
                // NO_OP
            }
        }

        return saveUserResult
    }

    override suspend fun updateUser(user: User): OperationResult<Unit> =
        when (
            val updateUserResult = userDatabaseSource.updateUser(
                user
            )
        ) {
            is Success -> {
                if (updateUserResult.data != NO_WORKER_UPDATED_RESULT) {
                    Success(Unit)
                } else {
                    Failure(Error.NullUser)
                }
            }

            is Failure -> Failure(updateUserResult.error)
        }

    override suspend fun updateUserCurrentStatus(userId: Long): OperationResult<Int> =
        userDatabaseSource.updateUserCurrentStatus(userId)

    private companion object {
        const val NO_WORKER_UPDATED_RESULT = 0
    }
}
