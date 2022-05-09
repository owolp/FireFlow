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

package dev.zitech.core.persistence.domain.model.preferences

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class BooleanPreferenceTest {

    @Test
    @DisplayName("Each Boolean Preference is filled correctly")
    fun booleanConfigs() {
        // Assert
        BooleanPreference.values().forEach {
            assertThat(it.key).isNotEmpty()
            assertThat(it.title).isNotEmpty()
            assertThat(it.explanation).isNotEmpty()
        }
    }

    @Nested
    @DisplayName("Default value for")
    inner class DefaultValue {

        @Test
        @DisplayName("ANALYTICS_COLLECTION")
        fun analyticsCollection() {
            assertThat(BooleanPreference.ANALYTICS_COLLECTION.defaultValue).isFalse()
        }

        @Test
        @DisplayName("CRASH_REPORTER_COLLECTION")
        fun crashReporterCollection() {
            assertThat(BooleanPreference.CRASH_REPORTER_COLLECTION.defaultValue).isFalse()
        }

        @Test
        @DisplayName("PERSONALIZED_ADS")
        fun personalizeAds() {
            assertThat(BooleanPreference.PERSONALIZED_ADS.defaultValue).isFalse()
        }
    }
}