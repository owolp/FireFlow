/*
 * Copyright (C) 2022 Zitech Ltd.
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

package dev.zitech.core.reporter.performance.domain.usecase

import dev.zitech.core.persistence.domain.model.database.UserLoggedState.LOGGED_IN
import dev.zitech.core.persistence.domain.model.database.UserLoggedState.LOGGED_OUT
import dev.zitech.core.persistence.domain.usecase.database.GetUserLoggedStateUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetPerformanceCollectionValueUseCase
import dev.zitech.core.remoteconfig.domain.model.BooleanConfig
import dev.zitech.core.remoteconfig.domain.usecase.GetBooleanConfigValueUseCase
import dev.zitech.core.reporter.performance.domain.repository.PerformanceRepository
import javax.inject.Inject

class SetPerformanceCollectionUseCase @Inject constructor(
    private val performanceRepository: PerformanceRepository,
    private val getUserLoggedStateUseCase: GetUserLoggedStateUseCase,
    private val getPerformanceCollectionValueUseCase: GetPerformanceCollectionValueUseCase,
    private val getBooleanConfigValueUseCase: GetBooleanConfigValueUseCase
) {

    suspend operator fun invoke(enabled: Boolean? = null) =
        enabled ?: when (getUserLoggedStateUseCase()) {
            LOGGED_IN -> {
                getPerformanceCollectionValueUseCase() &&
                    getBooleanConfigValueUseCase(BooleanConfig.PERFORMANCE_COLLECTION_ENABLED)
            }
            LOGGED_OUT -> false
        }.let { performanceRepository.setCollectionEnabled(it) }
}
