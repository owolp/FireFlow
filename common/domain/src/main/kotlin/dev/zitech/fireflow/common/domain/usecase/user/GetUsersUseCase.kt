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
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving all users.
 *
 * @property userRepository The repository for managing users.
 */
class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Invokes the use case to retrieve all users.
     *
     * @return A [Flow] emitting an [OperationResult] with the list of users.
     */
    operator fun invoke(): Flow<OperationResult<List<User>>> =
        userRepository.getUsers()
}
