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

import DialogRadioItemBuilder
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.common.domain.model.BuildFlavor
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.framework.applicationconfig.FakeAppConfigProvider
import dev.zitech.core.persistence.domain.usecase.database.UpdateCurrentUserAccountUseCase
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsAnalyticsCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsCrashReporterCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.error.SettingsErrorProvider
import dev.zitech.settings.presentation.settings.viewmodel.theme.SettingsThemeProvider
import dev.zitech.settings.presentation.test.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
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

    private val settingsStateHandler = SettingsStateHandler()
    private val updateCurrentUserAccountUseCase = mockk<UpdateCurrentUserAccountUseCase>()
    private val settingsAnalyticsCollectionStates = mockk<SettingsAnalyticsCollectionStates>()
    private val settingsCrashReporterCollectionStates = mockk<SettingsCrashReporterCollectionStates>()
    private val settingsErrorProvider = mockk<SettingsErrorProvider>()
    private val settingsThemeProvider = mockk<SettingsThemeProvider>()
    private val appConfigProvider = FakeAppConfigProvider()

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is true and collection is true THEN crashReporter is true`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        val collection = true
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returnsMany listOf(defaultIsCrashReporterEnabled, collection)

        val isAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val isPersonalizedAdsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returns isPersonalizedAdsEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns currentUserTheme

        val checked = true
        coEvery { settingsCrashReporterCollectionStates.setCrashReporterCollection(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnCrashReporterCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(isPersonalizedAdsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().crashReporter).isEqualTo(checked)
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsCrashReporterCollectionStates.setCrashReporterCollection(checked)
        }
    }

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is false and collection is false THEN show only loading`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        val collection = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returnsMany listOf(defaultIsCrashReporterEnabled, collection)

        val isAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val isPersonalizedAdsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returns isPersonalizedAdsEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns currentUserTheme

        val checked = false
        coEvery { settingsCrashReporterCollectionStates.setCrashReporterCollection(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnCrashReporterCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(isPersonalizedAdsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsCrashReporterCollectionStates.setCrashReporterCollection(checked)
        }
    }

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is true and collection is false THEN event Error`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        val collection = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returnsMany listOf(defaultIsCrashReporterEnabled, collection)

        val defaultIsAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns currentUserTheme

        val checked = true
        coEvery { settingsCrashReporterCollectionStates.setCrashReporterCollection(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsErrorProvider.getCrashReporterError() } returns Error(
            message,
            action
        )

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnCrashReporterCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            with(awaitItem().event as Error) {
                assertThat(this.message).isEqualTo(message)
                assertThat(this.action).isEqualTo(action)
            }
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsCrashReporterCollectionStates.setCrashReporterCollection(checked)
        }
    }

    @Test
    fun `WHEN OnTelemetryCheck GIVEN checked is true and collection is true THEN telemetry and personalisedAds are true`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        val collection = true
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returnsMany listOf(defaultIsAnalyticsEnabled, collection)

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returnsMany listOf(defaultIsAnalyticsEnabled, true)

        val isCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns isCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns currentUserTheme

        val checked = true
        coEvery { settingsAnalyticsCollectionStates.setAnalyticsCollection(checked) } just Runs
        coEvery { settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnTelemetryCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().telemetry).isEqualTo(checked)
            assertThat(awaitItem().personalizedAds).isEqualTo(checked)
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsAnalyticsCollectionStates.setAnalyticsCollection(checked)
            settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(checked)
        }
    }

    @Test
    fun `WHEN OnTelemetryCheck GIVEN checked is false and collection is false THEN telemetry and personalisedAds are false`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        val collection = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returnsMany listOf(defaultIsAnalyticsEnabled, collection)

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsAnalyticsEnabled

        val defaultIsCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns currentUserTheme

        val checked = false
        coEvery { settingsAnalyticsCollectionStates.setAnalyticsCollection(checked) } just Runs
        coEvery { settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnTelemetryCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsAnalyticsCollectionStates.setAnalyticsCollection(checked)
            settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(checked)
        }
    }

    @Test
    fun `WHEN OnTelemetryCheck GIVEN build config is FOSS THEN show only loading`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns currentUserTheme

        val checked = DataFactory.createRandomBoolean()

        appConfigProvider.setBuildFlavor(BuildFlavor.FOSS)

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnTelemetryCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify(exactly = 0) {
            settingsAnalyticsCollectionStates.setAnalyticsCollection(true)
            settingsAnalyticsCollectionStates.setAnalyticsCollection(false)
            settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(true)
            settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(false)
        }
    }

    @Test
    fun `WHEN OnTelemetryCheck GIVEN checked is true and collection is false THEN event Error`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        val collection = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returnsMany listOf(defaultIsAnalyticsEnabled, collection)

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultsCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns defaultsCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns currentUserTheme

        val checked = true
        coEvery { settingsAnalyticsCollectionStates.setAnalyticsCollection(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsErrorProvider.getTelemetryError() } returns Error(
            message,
            action
        )

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnTelemetryCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            with(awaitItem().event as Error) {
                assertThat(this.message).isEqualTo(message)
                assertThat(this.action).isEqualTo(action)
            }
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify(exactly = 0) {
            settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(true)
            settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(false)
        }
    }

    @Test
    fun `WHEN OnPersonalizedAdsCheck GIVEN checked is true and collection is true THEN personalizedAds is true`() = runTest {
        // Arrange
        val defaultPersonalizedAdsEnabled = false
        val collection = true
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returnsMany listOf(defaultPersonalizedAdsEnabled, collection)

        val isAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val isCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns isCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns currentUserTheme

        val checked = true
        coEvery { settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnPersonalizedAdsCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(isCrashReporterEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().personalizedAds).isEqualTo(checked)
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(checked)
        }
    }

    @Test
    fun `WHEN OnPersonalizedAdsCheck GIVEN checked is false and collection is false THEN show only loading`() = runTest {
        // Arrange
        val defaultPersonalizedAdsEnabled = false
        val collection = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returnsMany listOf(defaultPersonalizedAdsEnabled, collection)

        val isAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val isCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns isCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns currentUserTheme

        val checked = false
        coEvery { settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnPersonalizedAdsCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(isCrashReporterEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(checked)
        }
    }

    @Test
    fun `WHEN OnPersonalizedAdsCheck GIVEN checked is true and collection is false THEN event Error`() = runTest {
        // Arrange
        val defaultIsPersonalizedAdsEnabled = false
        val collection = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returnsMany listOf(defaultIsPersonalizedAdsEnabled, collection)

        val defaultIsAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns currentUserTheme

        val checked = true
        coEvery { settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsErrorProvider.getPersonalizedAdsError() } returns Error(
            message,
            action
        )

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnPersonalizedAdsCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            with(awaitItem().event as Error) {
                assertThat(this.message).isEqualTo(message)
                assertThat(this.action).isEqualTo(action)
            }
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(checked)
        }
    }

    @Test
    fun `WHEN OnPersonalizedAdsCheck GIVEN build config is FOSS THEN show only loading`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsCrashReporterEnabled = false
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns currentUserTheme

        val checked = DataFactory.createRandomBoolean()

        appConfigProvider.setBuildFlavor(BuildFlavor.FOSS)

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnPersonalizedAdsCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify(exactly = 0) {
            settingsAnalyticsCollectionStates.setAnalyticsCollection(true)
            settingsAnalyticsCollectionStates.setAnalyticsCollection(false)
            settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(true)
            settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(false)
        }
    }

    @Test
    fun `WHEN OnThemeSelect GIVEN theme id THEN update user theme and update theme and reset events`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsCrashReporterEnabled = true
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val defaultUserTheme = ApplicationTheme.SYSTEM

        val currentUserTheme = ApplicationTheme.DARK
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns currentUserTheme

        val themeId = ApplicationTheme.LIGHT.id

        coEvery { updateCurrentUserAccountUseCase(theme = ApplicationTheme.LIGHT) } returns DataResult.Success(Unit)

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnThemeSelect(themeId))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().theme).isEqualTo(defaultUserTheme)
            assertThat(awaitItem().theme).isEqualTo(currentUserTheme)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().theme).isEqualTo(ApplicationTheme.LIGHT)
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
            coVerify { updateCurrentUserAccountUseCase(theme = ApplicationTheme.LIGHT) }
        }
    }

    @Test
    fun `WHEN OnThemePreferenceClick THEN send SelectTheme event`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsCrashReporterEnabled = true
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val defaultUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns defaultUserTheme

        val dialogTitle = DataFactory.createRandomString()
        every { settingsThemeProvider.getDialogThemeTitle() } returns dialogTitle

        val dialogThemes = listOf(
            DialogRadioItemBuilder().setId(DataFactory.createRandomInt()).build(),
            DialogRadioItemBuilder().setId(DataFactory.createRandomInt()).build(),
            DialogRadioItemBuilder().setId(DataFactory.createRandomInt()).build()
        )
        coEvery { settingsThemeProvider.getDialogThemes() } returns dialogThemes

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnThemePreferenceClick)

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().theme).isEqualTo(defaultUserTheme)
            assertThat(awaitItem().isLoading).isFalse()
            with(awaitItem().event as SelectTheme) {
                assertThat(title).isEqualTo(dialogTitle)
                assertThat(themes).isEqualTo(dialogThemes)
            }
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `WHEN OnThemeDismiss THEN send Idle event`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsCrashReporterEnabled = true
        coEvery { settingsCrashReporterCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val defaultUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsThemeProvider.getCurrentUserTheme() } returns defaultUserTheme

        settingsStateHandler.state.test {
            // Act
            val sut = SettingsViewModel(
                settingsStateHandler,
                updateCurrentUserAccountUseCase,
                settingsAnalyticsCollectionStates,
                settingsCrashReporterCollectionStates,
                settingsErrorProvider,
                settingsThemeProvider,
                appConfigProvider
            )

            sut.sendIntent(OnThemeDismiss)

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().telemetry).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().theme).isEqualTo(defaultUserTheme)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }
}
