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

package dev.zitech.fireflow.common.domain.usecase.reporter

import dev.zitech.fireflow.common.domain.repository.reporter.PerformanceRepository
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject
import kotlinx.coroutines.flow.first

/**
 * Use case for retrieving the value of the performance collection setting.
 *
 * @property performanceRepository The repository for accessing performance data.
 */
class GetPerformanceCollectionValueUseCase @Inject constructor(
    private val performanceRepository: PerformanceRepository
) {

    /**
     * Invokes the use case to retrieve the value of the performance collection setting.
     *
     * @return The value of the performance collection setting.
     */
    suspend operator fun invoke(): OperationResult<Boolean> =
        performanceRepository.getCollectionEnabled().first()
}
