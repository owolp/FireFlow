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

package dev.zitech.core.reporter.performance.data.repository

import dev.zitech.core.common.domain.applicationconfig.AppConfigProvider
import dev.zitech.core.common.domain.model.BuildMode
import dev.zitech.core.reporter.performance.domain.reporter.PerformanceReporter
import dev.zitech.core.reporter.performance.domain.repository.PerformanceRepository
import javax.inject.Inject

@Deprecated("Modules")
internal class PerformanceRepositoryImpl @Inject constructor(
    private val appConfigProvider: AppConfigProvider,
    private val performanceReporter: PerformanceReporter
) : PerformanceRepository {

    override fun setCollectionEnabled(enabled: Boolean) {
        performanceReporter.setCollectionEnabled(
            when (appConfigProvider.buildMode) {
                BuildMode.RELEASE -> enabled
                BuildMode.DEBUG -> false
            }
        )
    }
}
