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

import dev.zitech.fireflow.common.domain.repository.cache.CacheRepository
import dev.zitech.fireflow.common.domain.repository.user.UserRepository
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import javax.inject.Inject

/**
 * Use case for deleting a user.
 *
 * This use case is responsible for deleting a user by invoking the [userRepository.deleteUserById]
 * method with the provided [userId]. It encapsulates the logic for deleting a user and invalidating
 * the caches in the cache repository.
 *
 * @param cacheRepository The repository for cache-related operations.
 * @param userRepository The repository for user-related operations.
 */
internal class DeleteUserUseCase @Inject constructor(
    private val cacheRepository: CacheRepository,
    private val userRepository: UserRepository
) {

    /**
     * Deletes a user by the specified user ID.
     *
     * This function deletes the user with the given [userId] by invoking the [userRepository.deleteUserById]
     * method. It returns an [OperationResult] that represents the result of the deletion operation. If the
     * deletion was successful and one or more rows were affected, it returns [Success] with [Unit]. If the
     * deletion failed or no rows were affected, it returns [Failure] with the associated error as a [Throwable].
     * Additionally, the function invalidates the caches in the cache repository.
     *
     * @param userId The ID of the user to be deleted.
     * @return An [OperationResult] representing the result of the deletion operation. If the deletion was
     *         successful and one or more rows were affected, it returns [Success] with [Unit]. If the deletion
     *         failed or no rows were affected, it returns [Failure] with the associated error as a [Throwable].
     */
    suspend operator fun invoke(userId: Long): OperationResult<Unit> =
        when (
            val result = userRepository.deleteUserById(userId)
        ) {
            is Failure -> Failure(result.error)
            is Success -> {
                if (result.data > NO_AFFECTED_ROWS) {
                    Success(Unit)
                } else {
                    Failure(Error.NullUser)
                }
                cacheRepository.invalidateCaches()
                Success(Unit)
            }
        }

    private companion object {
        const val NO_AFFECTED_ROWS = 0
    }
}
