/*
 * Copyright (C) 2022 Zitech Ltd.
 *
 * This program is free software= mockk<you can redistribute it and/or modify
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

package dev.zitech.settings.presentation.settings.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.model.BuildFlavor
import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.core.common.framework.applicationconfig.FakeAppConfigProvider
import dev.zitech.core.persistence.domain.usecase.preferences.GetAnalyticsCollectionValueUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetCrashReporterCollectionValueUseCase
import dev.zitech.core.reporter.analytics.domain.usecase.SetAnalyticsCollectionUseCase
import dev.zitech.core.reporter.crash.domain.usecase.SetCrashReporterCollectionUseCase
import dev.zitech.settings.R
import dev.zitech.settings.presentation.test.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val settingsStateHolder = SettingsStateHolder()
    private val getAnalyticsCollectionValueUseCase = mockk<GetAnalyticsCollectionValueUseCase>()
    private val setAnalyticsCollectionUseCase = mockk<SetAnalyticsCollectionUseCase>()
    private val getCrashReporterCollectionValueUseCase = mockk<GetCrashReporterCollectionValueUseCase>()
    private val setCrashReporterCollectionUseCase = mockk<SetCrashReporterCollectionUseCase>()
    private val stringsProvider = mockk<StringsProvider>()
    private val appConfigProvider = FakeAppConfigProvider()

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is true and collection is true THEN crashReporter is true`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        coEvery { getCrashReporterCollectionValueUseCase() } returnsMany listOf(defaultIsCrashReporterEnabled, true)

        val isAnalyticsEnabled = false
        coEvery { getAnalyticsCollectionValueUseCase() } returns isAnalyticsEnabled

        val checked = true
        coEvery { setCrashReporterCollectionUseCase(checked) } just Runs

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                getAnalyticsCollectionValueUseCase,
                setAnalyticsCollectionUseCase,
                getCrashReporterCollectionValueUseCase,
                setCrashReporterCollectionUseCase,
                stringsProvider,
                appConfigProvider
            )

            sut.sendIntent(OnCrashReporterCheck(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().crashReporter).isEqualTo(checked)
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is false and collection is false THEN show only loading`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        coEvery { getCrashReporterCollectionValueUseCase() } returnsMany listOf(defaultIsCrashReporterEnabled, false)

        val isAnalyticsEnabled = false
        coEvery { getAnalyticsCollectionValueUseCase() } returns isAnalyticsEnabled

        val checked = false
        coEvery { setCrashReporterCollectionUseCase(checked) } just Runs

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                getAnalyticsCollectionValueUseCase,
                setAnalyticsCollectionUseCase,
                getCrashReporterCollectionValueUseCase,
                setCrashReporterCollectionUseCase,
                stringsProvider,
                appConfigProvider
            )

            sut.sendIntent(OnCrashReporterCheck(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is true and collection is false THEN event Error`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        coEvery { getCrashReporterCollectionValueUseCase() } returnsMany listOf(defaultIsCrashReporterEnabled, false)

        val isAnalyticsEnabled = false
        coEvery { getAnalyticsCollectionValueUseCase() } returns isAnalyticsEnabled

        val checked = true
        coEvery { setCrashReporterCollectionUseCase(checked) } just Runs

        val message = DataFactory.createRandomString()
        every { stringsProvider(R.string.data_choices_crash_reporter_error) } returns message
        val action = DataFactory.createRandomString()
        every { stringsProvider(R.string.action_restart) } returns action

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                getAnalyticsCollectionValueUseCase,
                setAnalyticsCollectionUseCase,
                getCrashReporterCollectionValueUseCase,
                setCrashReporterCollectionUseCase,
                stringsProvider,
                appConfigProvider
            )

            sut.sendIntent(OnCrashReporterCheck(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            with(awaitItem().event as Error) {
                assertThat(this.message).isEqualTo(message)
                assertThat(this.action).isEqualTo(action)
            }
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `WHEN OnTelemetryCheck GIVEN checked is true and collection is true THEN telemetry is true`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { getAnalyticsCollectionValueUseCase() } returnsMany listOf(defaultIsAnalyticsEnabled, true)

        val isCrashReporterEnabled = false
        coEvery { getCrashReporterCollectionValueUseCase() } returns isCrashReporterEnabled

        val checked = true
        coEvery { setAnalyticsCollectionUseCase(checked) } just Runs

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                getAnalyticsCollectionValueUseCase,
                setAnalyticsCollectionUseCase,
                getCrashReporterCollectionValueUseCase,
                setCrashReporterCollectionUseCase,
                stringsProvider,
                appConfigProvider
            )

            sut.sendIntent(OnTelemetryCheck(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().telemetry).isEqualTo(checked)
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `WHEN OnTelemetryCheck GIVEN checked is false and collection is false THEN telemetry is false`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { getAnalyticsCollectionValueUseCase() } returnsMany listOf(defaultIsAnalyticsEnabled, false)

        val isCrashReporterEnabled = false
        coEvery { getCrashReporterCollectionValueUseCase() } returns isCrashReporterEnabled

        val checked = false
        coEvery { setAnalyticsCollectionUseCase(checked) } just Runs

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                getAnalyticsCollectionValueUseCase,
                setAnalyticsCollectionUseCase,
                getCrashReporterCollectionValueUseCase,
                setCrashReporterCollectionUseCase,
                stringsProvider,
                appConfigProvider
            )

            sut.sendIntent(OnTelemetryCheck(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `WHEN OnTelemetryCheck GIVEN build config is FOSS THEN show only loading`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { getAnalyticsCollectionValueUseCase() } returnsMany listOf(defaultIsAnalyticsEnabled, false)

        val isCrashReporterEnabled = false
        coEvery { getCrashReporterCollectionValueUseCase() } returns isCrashReporterEnabled

        val checked = false
        coEvery { setAnalyticsCollectionUseCase(checked) } just Runs

        appConfigProvider.setBuildFlavor(BuildFlavor.FOSS)

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                getAnalyticsCollectionValueUseCase,
                setAnalyticsCollectionUseCase,
                getCrashReporterCollectionValueUseCase,
                setCrashReporterCollectionUseCase,
                stringsProvider,
                appConfigProvider
            )

            sut.sendIntent(OnTelemetryCheck(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `WHEN OnTelemetryCheck GIVEN checked is true and collection is false THEN event Error`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { getAnalyticsCollectionValueUseCase() } returnsMany listOf(defaultIsAnalyticsEnabled, false)

        val isCrashReporterEnabled = false
        coEvery { getCrashReporterCollectionValueUseCase() } returns isCrashReporterEnabled

        val checked = true
        coEvery { setAnalyticsCollectionUseCase(checked) } just Runs

        val message = DataFactory.createRandomString()
        every { stringsProvider(R.string.data_choices_telemetry_error) } returns message
        val action = DataFactory.createRandomString()
        every { stringsProvider(R.string.action_restart) } returns action

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                getAnalyticsCollectionValueUseCase,
                setAnalyticsCollectionUseCase,
                getCrashReporterCollectionValueUseCase,
                setCrashReporterCollectionUseCase,
                stringsProvider,
                appConfigProvider
            )

            sut.sendIntent(OnTelemetryCheck(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            with(awaitItem().event as Error) {
                assertThat(this.message).isEqualTo(message)
                assertThat(this.action).isEqualTo(action)
            }
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }
}