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

import dev.zitech.fireflow.common.data.reporter.analytics.AnalyticsReporter
import dev.zitech.fireflow.common.data.source.preferences.PreferencesDataSource
import dev.zitech.fireflow.common.domain.model.analytics.AnalyticsEvent
import dev.zitech.fireflow.common.domain.model.analytics.AnalyticsProvider
import dev.zitech.fireflow.common.domain.model.preferences.BooleanPreference
import dev.zitech.fireflow.common.domain.repository.reporter.AnalyticsRepository
import dev.zitech.fireflow.core.applicationconfig.AppConfigProvider
import dev.zitech.fireflow.core.applicationconfig.BuildFlavor
import dev.zitech.fireflow.core.applicationconfig.BuildMode
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class AnalyticsRepositoryImpl @Inject constructor(
    private val appConfigProvider: AppConfigProvider,
    private val analyticsReporter: AnalyticsReporter,
    private val preferencesDataSource: PreferencesDataSource
) : AnalyticsRepository {

    override fun getAllowPersonalizedAds(): Flow<Boolean> =
        preferencesDataSource.getBoolean(
            BooleanPreference.PERSONALIZED_ADS.key,
            BooleanPreference.PERSONALIZED_ADS.defaultValue
        )

    override fun getCollectionEnabled(): Flow<Boolean> =
        preferencesDataSource.getBoolean(
            BooleanPreference.ANALYTICS_COLLECTION.key,
            BooleanPreference.ANALYTICS_COLLECTION.defaultValue
        )

    override suspend fun setAllowPersonalizedAds(enabled: Boolean) {
        analyticsReporter.allowPersonalizedAds(enabled)
        preferencesDataSource.saveBoolean(
            BooleanPreference.PERSONALIZED_ADS.key,
            enabled
        )
    }

    override suspend fun setCollectionEnabled(enabled: Boolean) {
        when {
            appConfigProvider.buildMode == BuildMode.RELEASE || appConfigProvider.buildFlavor == BuildFlavor.DEV -> {
                analyticsReporter.setCollectionEnabled(enabled)
                preferencesDataSource.saveBoolean(
                    BooleanPreference.ANALYTICS_COLLECTION.key,
                    enabled
                )
            }
            else -> {
                // When debug on any other flavor, we don't want to have collection enabled
            }
        }
    }

    override fun logEvent(analyticsEvent: AnalyticsEvent) {
        if (isValidAnalyticsProvider(analyticsEvent)) {
            analyticsReporter.logEvent(analyticsEvent.name, analyticsEvent.params)
        }
    }

    private fun isValidAnalyticsProvider(
        analyticsEvent: AnalyticsEvent
    ): Boolean = (
        analyticsEvent.providers.contains(AnalyticsProvider.HUAWEI) &&
            appConfigProvider.buildFlavor == BuildFlavor.GALLERY
        ) || (
        analyticsEvent.providers.contains(AnalyticsProvider.FIREBASE) &&
            appConfigProvider.buildFlavor == BuildFlavor.PLAY
        )
}