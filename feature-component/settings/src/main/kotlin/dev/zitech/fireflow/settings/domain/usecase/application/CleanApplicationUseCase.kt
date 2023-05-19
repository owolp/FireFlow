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

package dev.zitech.fireflow.settings.domain.usecase.application

import dev.zitech.fireflow.common.domain.repository.application.ApplicationRepository
import dev.zitech.fireflow.common.domain.repository.cache.CacheRepository
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import javax.inject.Inject

/**
 * Use case for cleaning the application by clearing application storage and invalidating caches.
 *
 * This use case is responsible for performing the following operations:
 * 1. Clears the application storage by invoking the `clearApplicationStorage()` function of [ApplicationRepository].
 * 2. Invalidates caches by invoking the `invalidateCaches()` function of [CacheRepository].
 *
 * @param applicationRepository The repository for accessing and modifying application-related data.
 * @param cacheRepository The repository for managing cache-related operations.
 */
internal class CleanApplicationUseCase @Inject constructor(
    private val applicationRepository: ApplicationRepository,
    private val cacheRepository: CacheRepository
) {

    /**
     * Cleans the application by clearing application storage and invalidating caches.
     *
     * @return An [OperationResult] representing the result of the operation,
     *         containing [Unit] on success or an error on failure.
     */
    suspend operator fun invoke(): OperationResult<Unit> =
        when (val result = applicationRepository.clearApplicationStorage()) {
            is Failure -> result
            is Success -> {
                cacheRepository.invalidateCaches()
                Success(Unit)
            }
        }
}
