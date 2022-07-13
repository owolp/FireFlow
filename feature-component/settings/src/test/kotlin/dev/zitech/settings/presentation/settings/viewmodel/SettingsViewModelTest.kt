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
import dev.zitech.core.common.framework.applicationconfig.FakeAppConfigProvider
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsAnalyticsCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsCrashReporterCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.error.SettingsErrorProvider
import dev.zitech.settings.presentation.test.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MainDispatcherRule::class)
internal class SettingsViewModelTest {

    private val settingsStateHolder = SettingsStateHolder()
    private val settingsAnalyticsCollectionStates = mockk<SettingsAnalyticsCollectionStates>()
    private val settingsCrashReporterCollectionStates = mockk<SettingsCrashReporterCollectionStates>()
    private val settingsErrorProvider = mockk<SettingsErrorProvider>()
    private val appConfigProvider = FakeAppConfigProvider()

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is true and collection is true THEN crashReporter is true`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returnsMany listOf(defaultIsCrashReporterEnabled, true)

        val isAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val checked = true
        coEvery { settingsCrashReporterCollectionStates.setCrashReporterCollection(checked) } just Runs

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
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
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returnsMany listOf(defaultIsCrashReporterEnabled, false)

        val isAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val checked = false
        coEvery { settingsCrashReporterCollectionStates.setCrashReporterCollection(checked) } just Runs

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
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
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returnsMany listOf(defaultIsCrashReporterEnabled, false)

        val isAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val checked = true
        coEvery { settingsCrashReporterCollectionStates.setCrashReporterCollection(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsErrorProvider.getCrashReporterError() } returns Error(
            message,
            action
        )

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
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
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returnsMany listOf(defaultIsAnalyticsEnabled, true)

        val isCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns isCrashReporterEnabled

        val checked = true
        coEvery { settingsAnalyticsCollectionStates.setAnalyticsCollection(checked) } just Runs

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
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
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returnsMany listOf(defaultIsAnalyticsEnabled, false)

        val isCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns isCrashReporterEnabled

        val checked = false
        coEvery { settingsAnalyticsCollectionStates.setAnalyticsCollection(checked) } just Runs

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
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
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returnsMany listOf(defaultIsAnalyticsEnabled, false)

        val isCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns isCrashReporterEnabled

        val checked = false
        coEvery { settingsAnalyticsCollectionStates.setAnalyticsCollection(checked) } just Runs

        appConfigProvider.setBuildFlavor(BuildFlavor.FOSS)

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
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
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returnsMany listOf(defaultIsAnalyticsEnabled, false)

        val isCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns isCrashReporterEnabled

        val checked = true
        coEvery { settingsAnalyticsCollectionStates.setAnalyticsCollection(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsErrorProvider.getTelemetryError() } returns Error(
            message,
            action
        )

        settingsStateHolder.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHolder,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
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