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

package dev.zitech.fireflow.common.domain.usecase.user

import dev.zitech.fireflow.common.domain.repository.user.UserRepository
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

/**
 * Use case for saving a user.
 *
 * This use case is responsible for saving a user. It interacts with the [UserRepository] to perform
 * the necessary operations.
 *
 * @property userRepository The repository for managing users.
 */
class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    /**
     * Invokes the use case to save a user.
     *
     * This suspend function is invoked to save a user with the provided details. It checks if a user
     * with the specified identifier and server address already exists. If an existing user is found,
     * the current users are removed and a new user is saved. If no existing user is found, an error
     * is returned indicating that a user with the given server address already exists.
     *
     * @param accessToken The access token of the user.
     * @param clientId The client ID of the user.
     * @param clientSecret The client secret of the user.
     * @param connectivityNotification Indicates whether the user has enabled connectivity notification.
     * @param identifier The identifier of the user.
     * @param isCurrentUser Flag indicating if the user is the current user.
     * @param serverAddress The server address of the user. If the value is null, it indicates that the user is local.
     * @param state The state of the user.
     *
     * @return An [OperationResult] representing the result of the save operation,
     *         containing the ID of the saved user if successful, or an error if unsuccessful.
     */
    suspend operator fun invoke(
        accessToken: String? = null,
        clientId: String? = null,
        clientSecret: String? = null,
        connectivityNotification: Boolean,
        identifier: String? = null,
        isCurrentUser: Boolean,
        serverAddress: String? = null,
        state: String
    ): OperationResult<Long> = if (identifier != null && serverAddress != null) {
        when (
            val result = userRepository.checkUserExistsByIdentifierAndServerAddress(
                identifier,
                serverAddress
            )
        ) {
            is OperationResult.Success -> {
                if (result.data) {
                    removeCurrentUsers(
                        clientId,
                        clientSecret,
                        connectivityNotification,
                        identifier,
                        isCurrentUser,
                        accessToken,
                        serverAddress,
                        state
                    )
                } else {
                    OperationResult.Failure(Error.UserWithServerAddressAlreadyExists(identifier, serverAddress))
                }
            }
            is OperationResult.Failure -> OperationResult.Failure(result.error)
        }
    } else {
        removeCurrentUsers(
            clientId,
            clientSecret,
            connectivityNotification,
            identifier,
            isCurrentUser,
            accessToken,
            serverAddress,
            state
        )
    }

    private suspend fun removeCurrentUsers(
        clientId: String?,
        clientSecret: String?,
        connectivityNotification: Boolean,
        identifier: String?,
        isCurrentUser: Boolean,
        accessToken: String?,
        serverAddress: String?,
        state: String
    ) = when (val result = userRepository.removeCurrentUsers()) {
        is OperationResult.Success -> {
            userRepository.saveUser(
                clientId = clientId,
                clientSecret = clientSecret,
                connectivityNotification = connectivityNotification,
                identifier = identifier,
                isCurrentUser = isCurrentUser,
                accessToken = accessToken,
                serverAddress = serverAddress,
                state = state
            )
        }
        is OperationResult.Failure -> OperationResult.Failure(
            result.error
        )
    }
}
