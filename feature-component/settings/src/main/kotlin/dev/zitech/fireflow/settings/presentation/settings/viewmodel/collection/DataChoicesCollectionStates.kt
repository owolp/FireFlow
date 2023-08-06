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

package dev.zitech.fireflow.settings.presentation.settings.viewmodel.collection

import dev.zitech.fireflow.common.domain.usecase.reporter.GetAllowPersonalizedAdsValueUseCase
import dev.zitech.fireflow.common.domain.usecase.reporter.GetAnalyticsCollectionValueUseCase
import dev.zitech.fireflow.common.domain.usecase.reporter.GetCrashReporterCollectionValueUseCase
import dev.zitech.fireflow.common.domain.usecase.reporter.GetPerformanceCollectionValueUseCase
import dev.zitech.fireflow.common.domain.usecase.reporter.SetAllowPersonalizedAdsUseCase
import dev.zitech.fireflow.common.domain.usecase.reporter.SetAnalyticsCollectionUseCase
import dev.zitech.fireflow.common.domain.usecase.reporter.SetCrashReporterCollectionUseCase
import dev.zitech.fireflow.common.domain.usecase.reporter.SetPerformanceCollectionUseCase
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

internal class DataChoicesCollectionStates @Inject constructor(
    private val getAllowPersonalizedAdsValueUseCase: GetAllowPersonalizedAdsValueUseCase,
    private val getAnalyticsCollectionValueUseCase: GetAnalyticsCollectionValueUseCase,
    private val getCrashReporterCollectionValueUseCase: GetCrashReporterCollectionValueUseCase,
    private val getPerformanceCollectionValueUseCase: GetPerformanceCollectionValueUseCase,
    private val setAllowPersonalizedAdsUseCase: SetAllowPersonalizedAdsUseCase,
    private val setAnalyticsCollectionUseCase: SetAnalyticsCollectionUseCase,
    private val setCrashReporterCollectionUseCase: SetCrashReporterCollectionUseCase,
    private val setPerformanceCollectionUseCase: SetPerformanceCollectionUseCase
) {

    suspend fun getAllowPersonalizedAdsValue(): OperationResult<Boolean> =
        getAllowPersonalizedAdsValueUseCase()

    suspend fun getAnalyticsCollectionValue(): OperationResult<Boolean> =
        getAnalyticsCollectionValueUseCase()

    suspend fun getCrashReporterCollectionValue(): OperationResult<Boolean> =
        getCrashReporterCollectionValueUseCase()

    suspend fun getPerformanceCollectionValue(): OperationResult<Boolean> =
        getPerformanceCollectionValueUseCase()

    suspend fun setAllowPersonalizedAdsValue(checked: Boolean) {
        setAllowPersonalizedAdsUseCase(checked)
    }

    suspend fun setAnalyticsCollection(checked: Boolean) {
        setAnalyticsCollectionUseCase(checked)
    }

    suspend fun setCrashReporterCollection(checked: Boolean) {
        setCrashReporterCollectionUseCase(checked)
    }

    suspend fun setPerformanceCollection(checked: Boolean) {
        setPerformanceCollectionUseCase(checked)
    }
}
