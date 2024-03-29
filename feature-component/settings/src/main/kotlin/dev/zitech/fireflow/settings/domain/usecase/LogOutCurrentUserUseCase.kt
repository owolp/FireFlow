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

package dev.zitech.fireflow.settings.domain.usecase

import dev.zitech.fireflow.common.domain.repository.cache.CacheRepository
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserUseCase
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserUseCase.IsCurrentUser
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import javax.inject.Inject

/**
 * Use case responsible for logging out the current user.
 *
 * @param updateCurrentUserUseCase The use case for updating the current user.
 * @param cacheRepository The repository for managing cache data.
 */
internal class LogOutCurrentUserUseCase @Inject constructor(
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    private val cacheRepository: CacheRepository
) {

    /**
     * Logs out the current user.
     *
     * @return An [OperationResult] indicating the result of the logout operation.
     */
    suspend operator fun invoke(): OperationResult<Unit> =
        when (
            val result = updateCurrentUserUseCase(
                IsCurrentUser(false)
            )
        ) {
            is Failure -> Failure(result.error)
            is Success -> {
                cacheRepository.invalidateCaches()
                Success(Unit)
            }
        }
}
