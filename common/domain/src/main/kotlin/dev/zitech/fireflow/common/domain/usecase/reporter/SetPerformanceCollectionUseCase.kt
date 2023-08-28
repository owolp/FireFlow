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

import dev.zitech.fireflow.common.domain.model.configurator.BooleanConfig
import dev.zitech.fireflow.common.domain.model.user.UserLoggedState
import dev.zitech.fireflow.common.domain.repository.reporter.PerformanceRepository
import dev.zitech.fireflow.common.domain.usecase.configurator.GetBooleanConfigValueUseCase
import dev.zitech.fireflow.common.domain.usecase.user.GetUserLoggedStateUseCase
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.getResultOrDefault
import javax.inject.Inject

/**
 * Use case for setting the value of the performance collection setting.
 *
 * @property getBooleanConfigValueUseCase The use case for retrieving the value of a boolean configuration.
 * @property getPerformanceCollectionValueUseCase The use case for retrieving the value of the performance collection
 * setting.
 * @property getUserLoggedStateUseCase The use case for retrieving the logged-in state of the user.
 * @property performanceRepository The repository for managing performance settings.
 */
class SetPerformanceCollectionUseCase @Inject constructor(
    private val getBooleanConfigValueUseCase: GetBooleanConfigValueUseCase,
    private val getPerformanceCollectionValueUseCase: GetPerformanceCollectionValueUseCase,
    private val getUserLoggedStateUseCase: GetUserLoggedStateUseCase,
    private val performanceRepository: PerformanceRepository
) {
    /**
     * Invokes the use case to set the value of the performance collection setting.
     *
     * @param enabled The new value of the performance collection setting. If null, the value will be determined based
     * on the user's logged-in state and the configuration value.
     */
    suspend operator fun invoke(enabled: Boolean? = null): OperationResult<Unit> =
        (
            enabled ?: when (getUserLoggedStateUseCase()) {
                UserLoggedState.LOGGED_IN -> {
                    getPerformanceCollectionValueUseCase().getResultOrDefault(false) &&
                        getBooleanConfigValueUseCase(BooleanConfig.PERFORMANCE_COLLECTION_ENABLED)
                }
                UserLoggedState.LOGGED_OUT -> false
            }
            ).run {
            performanceRepository.setCollectionEnabled(this)
        }
}
