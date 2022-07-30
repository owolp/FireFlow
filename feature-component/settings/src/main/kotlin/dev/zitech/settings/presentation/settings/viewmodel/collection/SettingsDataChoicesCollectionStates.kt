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

package dev.zitech.settings.presentation.settings.viewmodel.collection

import dev.zitech.core.persistence.domain.usecase.preferences.GetAllowPersonalizedAdsValueUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetAnalyticsCollectionValueUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetCrashReporterCollectionValueUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetPerformanceCollectionValueUseCase
import dev.zitech.core.reporter.analytics.domain.usecase.AllowPersonalizedAdsUseCase
import dev.zitech.core.reporter.analytics.domain.usecase.SetAnalyticsCollectionUseCase
import dev.zitech.core.reporter.crash.domain.usecase.SetCrashReporterCollectionUseCase
import dev.zitech.core.reporter.performance.domain.usecase.SetPerformanceCollectionUseCase
import javax.inject.Inject

class SettingsDataChoicesCollectionStates @Inject constructor(
    private val getAnalyticsCollectionValueUseCase: GetAnalyticsCollectionValueUseCase,
    private val setAnalyticsCollectionUseCase: SetAnalyticsCollectionUseCase,
    private val getAllowPersonalizedAdsValueUseCase: GetAllowPersonalizedAdsValueUseCase,
    private val allowPersonalizedAdsUseCase: AllowPersonalizedAdsUseCase,
    private val getCrashReporterCollectionValueUseCase: GetCrashReporterCollectionValueUseCase,
    private val setCrashReporterCollectionUseCase: SetCrashReporterCollectionUseCase,
    private val getPerformanceCollectionValueUseCase: GetPerformanceCollectionValueUseCase,
    private val setPerformanceCollectionUseCase: SetPerformanceCollectionUseCase
) {

    internal suspend fun setAnalyticsCollection(checked: Boolean) {
        setAnalyticsCollectionUseCase(checked)
    }

    internal suspend fun getAnalyticsCollectionValue(): Boolean =
        getAnalyticsCollectionValueUseCase()

    internal suspend fun setAllowPersonalizedAdsValue(checked: Boolean) {
        allowPersonalizedAdsUseCase(checked)
    }

    internal suspend fun getAllowPersonalizedAdsValue(): Boolean =
        getAllowPersonalizedAdsValueUseCase()

    internal suspend fun setCrashReporterCollection(checked: Boolean) {
        setCrashReporterCollectionUseCase(checked)
    }

    internal suspend fun getCrashReporterCollectionValue(): Boolean =
        getCrashReporterCollectionValueUseCase()

    internal suspend fun setPerformanceCollection(checked: Boolean) {
        setPerformanceCollectionUseCase(checked)
    }

    internal suspend fun getPerformanceCollectionValue(): Boolean =
        getPerformanceCollectionValueUseCase()
}
