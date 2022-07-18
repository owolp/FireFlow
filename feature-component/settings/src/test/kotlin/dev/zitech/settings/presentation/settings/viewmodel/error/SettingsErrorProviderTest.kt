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

package dev.zitech.settings.presentation.settings.viewmodel.error

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.settings.R
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SettingsErrorProviderTest {

    private val stringsProvider = mockk<StringsProvider>()

    private lateinit var sut: SettingsErrorProvider

    @BeforeEach
    fun setup() {
        sut = SettingsErrorProvider(
            stringsProvider
        )
    }

    @Test
    fun getTelemetryError() {
        // Arrange
        val message = DataFactory.createRandomString()
        every { stringsProvider(R.string.data_choices_telemetry_error) } returns message
        val action = DataFactory.createRandomString()
        every { stringsProvider(R.string.action_restart) } returns action

        // Act
        val result = sut.getTelemetryError()

        // Assert
        assertThat(result.message).isEqualTo(message)
        assertThat(result.action).isEqualTo(action)
    }

    @Test
    fun getCrashReporterError() {
        // Arrange
        val message = DataFactory.createRandomString()
        every { stringsProvider(R.string.data_choices_crash_reporter_error) } returns message
        val action = DataFactory.createRandomString()
        every { stringsProvider(R.string.action_restart) } returns action

        // Act
        val result = sut.getCrashReporterError()

        // Assert
        assertThat(result.message).isEqualTo(message)
        assertThat(result.action).isEqualTo(action)
    }

    @Test
    fun getPersonalizedAdsError() {
        // Arrange
        val message = DataFactory.createRandomString()
        every { stringsProvider(R.string.data_choices_personalized_ads_error) } returns message
        val action = DataFactory.createRandomString()
        every { stringsProvider(R.string.action_restart) } returns action

        // Act
        val result = sut.getPersonalizedAdsError()

        // Assert
        assertThat(result.message).isEqualTo(message)
        assertThat(result.action).isEqualTo(action)
    }
}
