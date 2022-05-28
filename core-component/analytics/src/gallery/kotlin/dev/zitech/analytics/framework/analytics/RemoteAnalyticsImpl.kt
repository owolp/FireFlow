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

package dev.zitech.analytics.framework.analytics

import android.content.Context
import android.os.Bundle
import com.huawei.hms.analytics.HiAnalytics
import javax.inject.Inject

internal class RemoteAnalyticsImpl @Inject constructor(
    context: Context
) : RemoteAnalytics {

    private val hiAnalytics = HiAnalytics.getInstance(context)

    override fun allowPersonalizedAds(enabled: Boolean) {
        hiAnalytics.setCollectAdsIdEnabled(enabled)
    }

    override fun setCollectionEnabled(enabled: Boolean) {
        hiAnalytics.setAnalyticsEnabled(enabled)
    }

    override fun logEvent(eventName: String, eventParams: Map<String, Any?>) {
        val bundle = Bundle()
        eventParams.filter { (_, value) -> value != null }
            .forEach { (key, value) -> bundle.putString(key, value.toString()) }

        hiAnalytics.onEvent(eventName, bundle)
    }
}
