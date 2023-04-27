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
import javax.inject.Inject

class SetPerformanceCollectionUseCase @Inject constructor(
    private val getBooleanConfigValueUseCase: GetBooleanConfigValueUseCase,
    private val getPerformanceCollectionValueUseCase: GetPerformanceCollectionValueUseCase,
    private val getUserLoggedStateUseCase: GetUserLoggedStateUseCase,
    private val performanceRepository: PerformanceRepository
) {

    suspend operator fun invoke(enabled: Boolean? = null) =
        (
            enabled ?: when (getUserLoggedStateUseCase()) {
                UserLoggedState.LOGGED_IN -> {
                    getPerformanceCollectionValueUseCase() &&
                        getBooleanConfigValueUseCase(BooleanConfig.PERFORMANCE_COLLECTION_ENABLED)
                }
                UserLoggedState.LOGGED_OUT -> false
            }
            ).run {
            performanceRepository.setCollectionEnabled(this)
        }
}
