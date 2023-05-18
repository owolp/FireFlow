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

package dev.zitech.fireflow.common.domain.model.preferences

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class BooleanPreferenceTest {

    @Test
    fun `BooleanPreference properties should have correct values`() {
        val analyticsCollection: Preference<Boolean> = BooleanPreference.ANALYTICS_COLLECTION
        val crashReporterCollection: Preference<Boolean> =
            BooleanPreference.CRASH_REPORTER_COLLECTION
        val performanceCollection: Preference<Boolean> = BooleanPreference.PERFORMANCE_COLLECTION
        val personalizedAds: Preference<Boolean> = BooleanPreference.PERSONALIZED_ADS

        assertThat(analyticsCollection.key).isEqualTo("analytics_collection")
        assertThat(analyticsCollection.title).isEqualTo("Analytics data collection")
        assertThat(analyticsCollection.explanation).isEqualTo("In some cases, you may wish to temporarily or permanently disable collection of Analytics data, such as to collect end-user consent or to fulfill legal obligations.")
        assertThat(analyticsCollection.defaultValue).isFalse()

        assertThat(crashReporterCollection.key).isEqualTo("crash_reporter_collection")
        assertThat(crashReporterCollection.title).isEqualTo("Crash data collection")
        assertThat(crashReporterCollection.explanation).isEqualTo("Automatically post crash reports to a report server")
        assertThat(crashReporterCollection.defaultValue).isFalse()

        assertThat(performanceCollection.key).isEqualTo("performance_collection")
        assertThat(performanceCollection.title).isEqualTo("Application performance data collection")
        assertThat(performanceCollection.explanation).isEqualTo("In some cases, you may wish to temporarily or permanently disable collection of Application performance data, such as to collect end-user consent or to fulfill legal obligations.")
        assertThat(performanceCollection.defaultValue).isFalse()

        assertThat(personalizedAds.key).isEqualTo("personalized_ads")
        assertThat(personalizedAds.title).isEqualTo("Analytics personalized advertising features")
        assertThat(personalizedAds.explanation).isEqualTo("To programmatically control whether a user's Analytics data should be used for personalized advertising, set the appropriate default behavior")
        assertThat(personalizedAds.defaultValue).isFalse()
    }
}
