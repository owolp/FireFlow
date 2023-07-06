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
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

/**
 * Use case for checking if a user already exists.
 *
 * This use case is responsible for checking if a user with the given identifier
 * and server address already exists. It interacts with the [UserRepository] to
 * perform the necessary checks.
 *
 * @property userRepository The repository for managing users.
 */
internal class CheckUserAlreadyExistsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    /**
     * Invokes the use case to check if a user already exists.
     *
     * This suspend function is invoked to check if a user with the specified
     * [identifier] and [serverAddress] already exists. It delegates the check
     * operation to the [UserRepository] and returns an [OperationResult] that
     * indicates whether the user exists or not.
     *
     * @param identifier The identifier of the user.
     * @param serverAddress The server address of the user.
     * @return An [OperationResult] representing the result of the operation,
     *         containing a boolean value indicating if the user already exists.
     */
    suspend operator fun invoke(identifier: String, serverAddress: String): OperationResult<Boolean> =
        userRepository.checkUserExistsByIdentifierAndServerAddress(identifier, serverAddress)
}
