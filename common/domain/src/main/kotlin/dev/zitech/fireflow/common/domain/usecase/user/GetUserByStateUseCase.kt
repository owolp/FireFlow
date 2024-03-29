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

import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.common.domain.repository.user.UserRepository
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

/**
 * Use case for retrieving a user by its state.
 *
 * @property userRepository The repository for managing users.
 */
class GetUserByStateUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Invokes the use case to retrieve a user by its state.
     *
     * @param state The state of the user.
     * @return An [OperationResult] with the user corresponding to the state.
     */
    suspend operator fun invoke(state: String): OperationResult<User> =
        userRepository.getUserByState(state)
}
