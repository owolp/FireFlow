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
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

/**
 * Use case for saving a user.
 *
 * @property userRepository The repository for managing users.
 */
class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Invokes the use case to save a user.
     *
     * @param accessToken The access token of the user.
     * @param clientId The client ID of the user.
     * @param clientSecret The client secret of the user.
     * @param isCurrentUser Flag indicating if the user is the current user.
     * @param serverAddress The server address of the user.
     * @param state The state of the user.
     *
     * @return The result of the operation containing the ID of the saved user.
     */
    suspend operator fun invoke(
        accessToken: String? = null,
        clientId: String? = null,
        clientSecret: String? = null,
        isCurrentUser: Boolean,
        serverAddress: String,
        state: String
    ): OperationResult<Long> =
        userRepository.saveUser(
            clientId = clientId,
            clientSecret = clientSecret,
            isCurrentUser = isCurrentUser,
            accessToken = accessToken,
            serverAddress = serverAddress,
            state = state
        )
}
