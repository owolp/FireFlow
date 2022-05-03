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

package dev.zitech.analytics.framework.source

import dev.zitech.analytics.framework.analytics.RemoteAnalytics
import javax.inject.Inject

internal class AnalyticsProviderSourceImpl @Inject constructor(
    private val remoteAnalytics: RemoteAnalytics
) : AnalyticsProviderSource {

    override fun allowPersonalizedAds(enabled: Boolean) =
        remoteAnalytics.allowPersonalizedAds(enabled)

    override fun setCollectionEnabled(enabled: Boolean) =
        remoteAnalytics.setCollectionEnabled(enabled)
}
