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
import dev.zitech.core.common.framework.applicationconfig.FakeAppConfigProvider
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsAppearanceCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsDataChoicesCollectionStates
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
    private val settingsAppearanceCollectionStates = mockk<SettingsAppearanceCollectionStates>()
    private val settingsDataChoicesCollectionStates = mockk<SettingsDataChoicesCollectionStates>()
    private val settingsErrorProvider = mockk<SettingsErrorProvider>()
    private val settingsThemeProvider = mockk<SettingsThemeProvider>()
    private val appConfigProvider = FakeAppConfigProvider()

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is true and collection is true THEN crashReporter is true`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        val collection = true
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returnsMany listOf(defaultIsCrashReporterEnabled, collection)

        val isAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val isPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns isPersonalizedAdsEnabled

        val isPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns isPerformanceEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setCrashReporterCollection(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnCrashReporterCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(isPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(isPerformanceEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().crashReporter).isEqualTo(checked)
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsDataChoicesCollectionStates.setCrashReporterCollection(checked)
        }
    }

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is false and collection is false THEN show only loading`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returnsMany listOf(defaultIsCrashReporterEnabled, collection)

        val isAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val isPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns isPersonalizedAdsEnabled

        val isPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns isPerformanceEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = false
        coEvery { settingsDataChoicesCollectionStates.setCrashReporterCollection(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnCrashReporterCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(isPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(isPerformanceEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsDataChoicesCollectionStates.setCrashReporterCollection(checked)
        }
    }

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is true and collection is false THEN event Error`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returnsMany listOf(defaultIsCrashReporterEnabled, collection)

        val defaultIsAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns defaultIsPerformanceEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setCrashReporterCollection(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsErrorProvider.getCrashReporterError() } returns Error(
            message,
            action
        )

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnCrashReporterCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(defaultIsPerformanceEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            with(awaitItem().event as Error) {
                assertThat(this.message).isEqualTo(message)
                assertThat(this.action).isEqualTo(action)
            }
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsDataChoicesCollectionStates.setCrashReporterCollection(checked)
        }
    }

    @Test
    fun `WHEN OnAnalyticsCheck GIVEN checked is true and collection is true THEN analytics and personalisedAds and performance are true`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        val collection = true
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returnsMany listOf(defaultIsAnalyticsEnabled, collection)

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returnsMany listOf(defaultIsAnalyticsEnabled, true)

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returnsMany listOf(defaultIsPerformanceEnabled, true)

        val isCrashReporterEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns isCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setAnalyticsCollection(checked) } just Runs
        coEvery { settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs
        coEvery { settingsDataChoicesCollectionStates.setPerformanceCollection(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnAnalyticsCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(defaultIsPerformanceEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().analytics).isEqualTo(checked)
            assertThat(awaitItem().personalizedAds).isEqualTo(checked)
            assertThat(awaitItem().performance).isEqualTo(checked)
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(checked)
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
        }
    }

    @Test
    fun `WHEN OnAnalyticsCheck GIVEN checked is false and collection is false THEN analytics and personalisedAds and performance are false`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returnsMany listOf(defaultIsAnalyticsEnabled, collection)

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns defaultIsPerformanceEnabled

        val defaultIsCrashReporterEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = false
        coEvery { settingsDataChoicesCollectionStates.setAnalyticsCollection(checked) } just Runs
        coEvery { settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs
        coEvery { settingsDataChoicesCollectionStates.setPerformanceCollection(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnAnalyticsCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().analytics).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().analytics).isEqualTo(defaultIsPerformanceEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(checked)
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
            settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
        }
    }

    @Test
    fun `WHEN OnAnalyticsCheck GIVEN build config is FOSS THEN show only loading`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns defaultIsPerformanceEnabled

        val defaultIsCrashReporterEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = DataFactory.createRandomBoolean()

        appConfigProvider.setBuildFlavor(BuildFlavor.FOSS)

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnAnalyticsCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify(exactly = 0) {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(any())
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(any())
            settingsDataChoicesCollectionStates.setPerformanceCollection(any())
        }
    }

    @Test
    fun `WHEN OnAnalyticsCheck GIVEN checked is true and collection is false THEN event Error`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returnsMany listOf(defaultIsAnalyticsEnabled, collection)

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns defaultIsPerformanceEnabled

        val defaultsCrashReporterEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns defaultsCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setAnalyticsCollection(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsErrorProvider.getAnalyticsError() } returns Error(
            message,
            action
        )

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnAnalyticsCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().analytics).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().analytics).isEqualTo(defaultIsPerformanceEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            with(awaitItem().event as Error) {
                assertThat(this.message).isEqualTo(message)
                assertThat(this.action).isEqualTo(action)
            }
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify(exactly = 0) {
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(any())
            settingsDataChoicesCollectionStates.setPerformanceCollection(any())
        }
    }

    @Test
    fun `WHEN OnPersonalizedAdsCheck GIVEN checked is true and collection is true THEN personalizedAds is true`() = runTest {
        // Arrange
        val defaultPersonalizedAdsEnabled = false
        val collection = true
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returnsMany listOf(defaultPersonalizedAdsEnabled, collection)

        val isAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val isPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns isPerformanceEnabled

        val isCrashReporterEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns isCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnPersonalizedAdsCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(isPerformanceEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().personalizedAds).isEqualTo(checked)
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
        }
    }

    @Test
    fun `WHEN OnPersonalizedAdsCheck GIVEN checked is false and collection is false THEN show only loading`() = runTest {
        // Arrange
        val defaultPersonalizedAdsEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returnsMany listOf(defaultPersonalizedAdsEnabled, collection)

        val isAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val isPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns isPerformanceEnabled

        val isCrashReporterEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns isCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = false
        coEvery { settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnPersonalizedAdsCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(isPerformanceEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
        }
    }

    @Test
    fun `WHEN OnPersonalizedAdsCheck GIVEN checked is true and collection is false THEN event Error`() = runTest {
        // Arrange
        val defaultIsPersonalizedAdsEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returnsMany listOf(defaultIsPersonalizedAdsEnabled, collection)

        val defaultIsAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns defaultIsPerformanceEnabled

        val defaultIsCrashReporterEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsErrorProvider.getPersonalizedAdsError() } returns Error(
            message,
            action
        )

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnPersonalizedAdsCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(defaultIsPerformanceEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            with(awaitItem().event as Error) {
                assertThat(this.message).isEqualTo(message)
                assertThat(this.action).isEqualTo(action)
            }
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
        }
    }

    @Test
    fun `WHEN OnPersonalizedAdsCheck GIVEN build config is FOSS THEN show only loading`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns defaultIsPerformanceEnabled

        val defaultIsCrashReporterEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = DataFactory.createRandomBoolean()

        appConfigProvider.setBuildFlavor(BuildFlavor.FOSS)

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnPersonalizedAdsCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify(exactly = 0) {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(any())
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(any())
            settingsDataChoicesCollectionStates.setPerformanceCollection(any())
        }
    }

    @Test
    fun `WHEN OnThemeSelect GIVEN theme id THEN update user theme and update theme and reset events`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns defaultIsPerformanceEnabled

        val defaultIsCrashReporterEnabled = true
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val defaultUserTheme = ApplicationTheme.SYSTEM

        val currentUserTheme = ApplicationTheme.DARK
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val themeId = ApplicationTheme.LIGHT.id

        coEvery { settingsAppearanceCollectionStates.saveApplicationThemeValue(ApplicationTheme.LIGHT) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnThemeSelect(themeId))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(defaultIsPerformanceEnabled)
            assertThat(awaitItem().theme).isEqualTo(defaultUserTheme)
            assertThat(awaitItem().theme).isEqualTo(currentUserTheme)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().theme).isEqualTo(ApplicationTheme.LIGHT)
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
            coVerify { settingsAppearanceCollectionStates.saveApplicationThemeValue(ApplicationTheme.LIGHT) }
        }
    }

    @Test
    fun `WHEN OnThemePreferenceClick THEN send SelectTheme event`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns defaultIsPerformanceEnabled

        val defaultIsCrashReporterEnabled = true
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val defaultUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns defaultUserTheme

        val dialogTitle = DataFactory.createRandomString()
        every { settingsThemeProvider.getDialogThemeTitle() } returns dialogTitle

        val dialogThemes = listOf(
            DialogRadioItemBuilder().setId(DataFactory.createRandomInt()).build(),
            DialogRadioItemBuilder().setId(DataFactory.createRandomInt()).build(),
            DialogRadioItemBuilder().setId(DataFactory.createRandomInt()).build()
        )
        coEvery { settingsThemeProvider.getDialogThemes(defaultUserTheme) } returns dialogThemes

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnThemePreferenceClick)

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(defaultIsPerformanceEnabled)
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
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns defaultIsPerformanceEnabled

        val defaultIsCrashReporterEnabled = true
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val defaultUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns defaultUserTheme

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnThemeDismiss)

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(defaultIsPerformanceEnabled)
            assertThat(awaitItem().theme).isEqualTo(defaultUserTheme)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `WHEN OnPerformanceCheckChange GIVEN checked is true and collection is true THEN performance is true`() = runTest {
        // Arrange
        val defaultPerformanceEnabled = false
        val collection = true
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returnsMany listOf(defaultPerformanceEnabled, collection)

        val isAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val isPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns isPersonalizedAdsEnabled

        val isCrashReporterEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns isCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setPerformanceCollection(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnPerformanceCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(isPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(defaultPerformanceEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().performance).isEqualTo(checked)
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
        }
    }

    @Test
    fun `WHEN OnPerformanceCheckChange GIVEN checked is false and collection is false THEN show only loading`() = runTest {
        // Arrange
        val defaultPerformanceEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returnsMany listOf(defaultPerformanceEnabled, collection)

        val isAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns isAnalyticsEnabled

        val isPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns isPersonalizedAdsEnabled

        val isCrashReporterEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns isCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = false
        coEvery { settingsDataChoicesCollectionStates.setPerformanceCollection(checked) } just Runs

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnPerformanceCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(isAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(isPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(defaultPerformanceEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
        }
    }

    @Test
    fun `WHEN OnPerformanceCheckChange GIVEN checked is true and collection is false THEN event Error`() = runTest {
        // Arrange
        val defaultIsPerformanceEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returnsMany listOf(defaultIsPerformanceEnabled, collection)

        val defaultIsAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsCrashReporterEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setPerformanceCollection(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsErrorProvider.getPerformanceError() } returns Error(
            message,
            action
        )

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnPerformanceCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().analytics).isEqualTo(defaultIsAnalyticsEnabled)
            assertThat(awaitItem().personalizedAds).isEqualTo(defaultIsPersonalizedAdsEnabled)
            assertThat(awaitItem().performance).isEqualTo(defaultIsPerformanceEnabled)
            assertThat(awaitItem().isLoading).isFalse()
            with(awaitItem().event as Error) {
                assertThat(this.message).isEqualTo(message)
                assertThat(this.action).isEqualTo(action)
            }
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify {
            settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
        }
    }

    @Test
    fun `WHEN OnPerformanceCheckChange GIVEN build config is FOSS THEN show only loading`() = runTest {
        // Arrange
        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns defaultIsPerformanceEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsCrashReporterEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns defaultIsCrashReporterEnabled

        val currentUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns currentUserTheme

        val checked = DataFactory.createRandomBoolean()

        appConfigProvider.setBuildFlavor(BuildFlavor.FOSS)

        settingsStateHandler.state.test {
            // Act
            val sut = getSettingsViewModel()

            sut.sendIntent(OnPerformanceCheckChange(checked))

            // Assert
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(awaitItem().isLoading).isTrue()
            assertThat(awaitItem().isLoading).isFalse()
            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
        coVerify(exactly = 0) {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(any())
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(any())
            settingsDataChoicesCollectionStates.setPerformanceCollection(any())
        }
    }

    private fun getSettingsViewModel() = SettingsViewModel(
        settingsStateHandler,
        settingsAppearanceCollectionStates,
        settingsDataChoicesCollectionStates,
        settingsErrorProvider,
        settingsThemeProvider,
        appConfigProvider
    )
}
