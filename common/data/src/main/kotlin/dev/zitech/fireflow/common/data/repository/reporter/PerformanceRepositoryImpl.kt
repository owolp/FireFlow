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

package dev.zitech.fireflow.common.data.repository.reporter

import dev.zitech.fireflow.common.data.reporter.performance.PerformanceReporter
import dev.zitech.fireflow.common.data.source.preferences.PreferencesDataSource
import dev.zitech.fireflow.common.domain.model.preferences.BooleanPreference
import dev.zitech.fireflow.common.domain.repository.reporter.PerformanceRepository
import dev.zitech.fireflow.core.applicationconfig.AppConfigProvider
import dev.zitech.fireflow.core.applicationconfig.BuildFlavor
import dev.zitech.fireflow.core.applicationconfig.BuildMode
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class PerformanceRepositoryImpl @Inject constructor(
    private val appConfigProvider: AppConfigProvider,
    private val performanceReporter: PerformanceReporter,
    private val preferencesDataSource: PreferencesDataSource
) : PerformanceRepository {

    override fun getCollectionEnabled(): Flow<Boolean> =
        preferencesDataSource.getBoolean(
            BooleanPreference.PERFORMANCE_COLLECTION.key,
            BooleanPreference.PERFORMANCE_COLLECTION.defaultValue
        )

    override suspend fun setCollectionEnabled(enabled: Boolean) {
        when {
            appConfigProvider.buildMode == BuildMode.RELEASE || appConfigProvider.buildFlavor == BuildFlavor.DEV -> {
                performanceReporter.setCollectionEnabled(enabled)
                preferencesDataSource.saveBoolean(
                    BooleanPreference.PERFORMANCE_COLLECTION.key,
                    enabled
                )
            }
            else -> {
                // When debug on any other flavor, we don't want to have collection enabled
            }
        }
    }
}