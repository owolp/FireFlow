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

package dev.zitech.analytics.data.repository

import dev.zitech.analytics.domain.model.AnalyticsEvent
import dev.zitech.analytics.domain.model.AnalyticsProvider
import dev.zitech.analytics.domain.repository.AnalyticsRepository
import dev.zitech.analytics.domain.source.AnalyticsProviderSource
import dev.zitech.core.common.domain.applicationconfig.AppConfigProvider
import dev.zitech.core.common.domain.model.BuildFlavor
import javax.inject.Inject

internal class AnalyticsRepositoryImpl @Inject constructor(
    private val appConfigProvider: AppConfigProvider,
    private val analyticsProviderSource: AnalyticsProviderSource
) : AnalyticsRepository {

    override fun allowPersonalizedAds(enabled: Boolean) =
        analyticsProviderSource.allowPersonalizedAds(enabled)

    override fun setCollectionEnabled(enabled: Boolean) =
        analyticsProviderSource.setCollectionEnabled(enabled)

    override fun logEvent(analyticsEvent: AnalyticsEvent) {
        if (isValidAnalyticsProvider(analyticsEvent)) {
            analyticsProviderSource.logEvent(analyticsEvent.name, analyticsEvent.params)
        }
    }

    private fun isValidAnalyticsProvider(
        analyticsEvent: AnalyticsEvent
    ): Boolean = (
        analyticsEvent.providers.contains(AnalyticsProvider.HUAWEI) &&
            appConfigProvider.buildFlavor == BuildFlavor.GALLERY
        ) ||
        (
            analyticsEvent.providers.contains(AnalyticsProvider.FIREBASE) &&
                appConfigProvider.buildFlavor == BuildFlavor.PLAY
            )
}
