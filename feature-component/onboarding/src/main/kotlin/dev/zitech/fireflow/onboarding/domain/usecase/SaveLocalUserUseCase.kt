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

package dev.zitech.fireflow.onboarding.domain.usecase

import dev.zitech.fireflow.common.domain.repository.user.UserRepository
import dev.zitech.fireflow.common.domain.usecase.user.SaveUserUseCase
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

/**
 * Use case class for saving a local user.
 *
 * This class handles the logic for saving a local user by generating a random username,
 * checking if the username already exists, and then saving the user if it doesn't.
 *
 * @property getRandomUsernameUseCase The use case for generating a random username.
 * @property saveUserUseCase The use case for saving a user.
 * @property userRepository The repository for user data.
 */
internal class SaveLocalUserUseCase @Inject constructor(
    private val getRandomUsernameUseCase: GetRandomUsernameUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val userRepository: UserRepository
) {

    /**
     * Invokes the save local user use case.
     *
     * @return An [OperationResult] indicating the success or failure of the operation.
     */
    suspend operator fun invoke(): OperationResult<Unit> =
        when (val result = createRandomUsername()) {
            is OperationResult.Success -> handleCreateUsernameSuccess(result.data)
            is OperationResult.Failure -> OperationResult.Failure(result.error)
        }

    /**
     * Creates a random username.
     *
     * @return An [OperationResult] containing the randomly generated username
     *         or an error in case of failure.
     */
    private suspend fun createRandomUsername(): OperationResult<String> =
        when (val usernameResult = getRandomUsernameUseCase()) {
            is OperationResult.Success -> handleRandomUsernameSuccess(usernameResult.data)
            is OperationResult.Failure -> OperationResult.Failure(usernameResult.error)
        }

    /**
     * Handles the success case of creating a username.
     *
     * @param username The generated username.
     * @return An [OperationResult] indicating the success or failure of the operation.
     */
    private suspend fun handleCreateUsernameSuccess(username: String): OperationResult<Unit> =
        when (
            val saveUserResult = saveUserUseCase(
                connectivityNotification = false,
                isCurrentUser = true,
                identifier = username,
                state = ""
            )
        ) {
            is OperationResult.Success -> OperationResult.Success(Unit)
            is OperationResult.Failure -> OperationResult.Failure(saveUserResult.error)
        }

    /**
     * Handles the success case of generating a random username.
     *
     * @param username The generated username.
     * @return An [OperationResult] containing the username if it doesn't already exist,
     *         or creates a new random username if it exists, or an error in case of failure.
     */
    private suspend fun handleRandomUsernameSuccess(username: String): OperationResult<String> =
        when (val result = userRepository.checkUserExistsByIdentifier(username)) {
            is OperationResult.Success -> {
                if (result.data) {
                    createRandomUsername()
                } else {
                    OperationResult.Success(username)
                }
            }
            is OperationResult.Failure -> OperationResult.Failure(result.error)
        }
}
