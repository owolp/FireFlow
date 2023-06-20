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

package dev.zitech.fireflow.authentication.domain.usecase

import dev.zitech.fireflow.common.domain.repository.user.UserRepository
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

/**
 * Use case for setting a new current user.
 *
 * This use case is responsible for setting a new current user by updating the current user status
 * in the user repository. It encapsulates the logic for updating the current user status by invoking
 * the [userRepository.updateUserCurrentStatus] method.
 *
 * @param userRepository The repository for user-related operations.
 */
internal class SetNewCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    /**
     * Sets a new current user by the specified user ID.
     *
     * This function sets a new current user by invoking the [userRepository.updateUserCurrentStatus]
     * method with the provided [userId]. It returns an [OperationResult] that represents the result
     * of the operation, which contains either a success status with [Unit] or a failure status with
     * an associated error.
     *
     * @param userId The ID of the user to be set as the new current user.
     * @return An [OperationResult] representing the result of the operation. If setting the new current
     *         user was successful and one or more rows were affected, it returns [Success] with [Unit].
     *         If setting the new current user failed or no rows were affected, it returns [Failure]
     *         with the associated error as a [Error].
     */
    suspend operator fun invoke(userId: Long): OperationResult<Unit> =
        when (val result = userRepository.updateUserCurrentStatus(userId)) {
            is OperationResult.Failure -> OperationResult.Failure(result.error)
            is OperationResult.Success -> {
                if (result.data > NO_AFFECTED_ROWS) {
                    OperationResult.Success(Unit)
                } else {
                    OperationResult.Failure(Error.NullUser)
                }
            }
        }

    private companion object {
        const val NO_AFFECTED_ROWS = 0
    }
}
