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

package dev.zitech.core.reporter.analytics.framework

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics.UserProperty.ALLOW_AD_PERSONALIZATION_SIGNALS
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dev.zitech.core.reporter.analytics.domain.reporter.AnalyticsReporter
import javax.inject.Inject

@Deprecated("Modules")
@Suppress("UnusedPrivateMember", "UNUSED_PARAMETER")
internal class AnalyticsReporterImpl @Inject constructor(
    context: Context
) : AnalyticsReporter {

    private val firebaseAnalytics = Firebase.analytics

    override fun allowPersonalizedAds(enabled: Boolean) {
        firebaseAnalytics.setUserProperty(
            ALLOW_AD_PERSONALIZATION_SIGNALS,
            "true"
        )
    }

    override fun setCollectionEnabled(enabled: Boolean) {
        firebaseAnalytics.setAnalyticsCollectionEnabled(enabled)
    }

    override fun logEvent(eventName: String, eventParams: Map<String, Any?>) {
        val bundle = Bundle()
        eventParams.filter { (_, value) -> value != null }
            .forEach { (key, value) -> bundle.putString(key, value.toString()) }

        firebaseAnalytics.logEvent(eventName, bundle)
    }
}
