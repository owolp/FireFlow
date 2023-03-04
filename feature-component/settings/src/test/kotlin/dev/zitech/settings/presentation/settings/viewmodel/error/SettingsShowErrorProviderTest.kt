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

package dev.zitech.settings.presentation.settings.viewmodel.error

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.settings.R
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private val action = DataFactory.createRandomString()

internal class SettingsShowErrorProviderTest {

    private val stringsProvider = mockk<StringsProvider>()

    private lateinit var sut: SettingsShowErrorProvider

    @BeforeEach
    fun setup() {
        every { stringsProvider(R.string.action_restart) } returns action

        sut = SettingsShowErrorProvider(
            stringsProvider
        )
    }

    @Test
    fun getAnalyticsError() {
        // Act
        val result = sut.analyticsError

        // Assert
        assertThat(result.messageResId).isEqualTo(R.string.data_choices_analytics_error)
        assertThat(result.action).isEqualTo(action)
    }

    @Test
    fun getCrashReporterError() {
        // Act
        val result = sut.crashReporterError

        // Assert
        assertThat(result.messageResId).isEqualTo(R.string.data_choices_crash_reporter_error)
        assertThat(result.action).isEqualTo(action)
    }

    @Test
    fun getPersonalizedAdsError() {
        // Act
        val result = sut.personalizedAdsError

        // Assert
        assertThat(result.messageResId).isEqualTo(R.string.data_choices_personalized_ads_error)
        assertThat(result.action).isEqualTo(action)
    }

    @Test
    fun getPerformanceError() {
        // Act
        val result = sut.performanceError

        // Assert
        assertThat(result.messageResId).isEqualTo(R.string.data_choices_performance_error)
        assertThat(result.action).isEqualTo(action)
    }
}
