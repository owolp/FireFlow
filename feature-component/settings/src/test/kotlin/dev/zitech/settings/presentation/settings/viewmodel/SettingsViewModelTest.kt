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
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.model.ApplicationLanguage
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.common.domain.model.BuildFlavor
import dev.zitech.core.common.domain.navigation.DeepLinkScreenDestination
import dev.zitech.core.common.framework.applicationconfig.FakeAppConfigProvider
import dev.zitech.core.common.presentation.splash.SplashScreenStateHandler
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsAppearanceCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsDataChoicesCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.error.SettingsShowErrorProvider
import dev.zitech.settings.presentation.settings.viewmodel.theme.SettingsStringsProvider
import dev.zitech.settings.presentation.test.MainDispatcherRule
import dev.zitech.settings.presentation.test.TestObserver
import dev.zitech.settings.presentation.test.test
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MainDispatcherRule::class)
internal class SettingsViewModelTest {

    private val settingsStateHandler = SettingsStateHandler()
    private val splashScreenStateHandler = mockk<SplashScreenStateHandler>(relaxUnitFun = true)
    private val getScreenDestinationUseCase = mockk<GetScreenDestinationUseCase> {
        every { this@mockk.invoke() } returns flowOf(DeepLinkScreenDestination.Current)
    }
    private val settingsAppearanceCollectionStates = mockk<SettingsAppearanceCollectionStates>()
    private val settingsDataChoicesCollectionStates = mockk<SettingsDataChoicesCollectionStates>()
    private val settingsShowErrorProvider = mockk<SettingsShowErrorProvider>()
    private val settingsStringsProvider = mockk<SettingsStringsProvider>()
    private val appConfigProvider = FakeAppConfigProvider()

    private lateinit var testObserver: TestObserver<SettingsState>

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is true and collection is true THEN crashReporter is true`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        val collection = true
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returnsMany listOf(defaultIsCrashReporterEnabled, collection)

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setCrashReporterCollection(checked) } just Runs

        mockInit(
            analytics = false,
            personalizedAds = false,
            performance = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnCrashReporterCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState(),
            getInitState(crashReporter = checked)
        )
        coVerify {
            settingsDataChoicesCollectionStates.setCrashReporterCollection(checked)
        }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is false and collection is false THEN do nothing`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returnsMany listOf(defaultIsCrashReporterEnabled, collection)

        val checked = false
        coEvery { settingsDataChoicesCollectionStates.setCrashReporterCollection(checked) } just Runs

        mockInit(
            analytics = false,
            personalizedAds = false,
            performance = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )
        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnCrashReporterCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState()
        )
        coVerify {
            settingsDataChoicesCollectionStates.setCrashReporterCollection(checked)
        }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnCrashReporterCheck GIVEN checked is true and collection is false THEN event Error`() = runTest {
        // Arrange
        val defaultIsCrashReporterEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returnsMany listOf(defaultIsCrashReporterEnabled, collection)

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setCrashReporterCollection(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsShowErrorProvider.crashReporterError } returns ShowError(
            message,
            action
        )

        mockInit(
            analytics = false,
            personalizedAds = false,
            performance = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnCrashReporterCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState(),
            getInitState(event = ShowError(message, action))
        )
        coVerify {
            settingsDataChoicesCollectionStates.setCrashReporterCollection(checked)
        }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnAnalyticsCheck GIVEN checked is true and collection is true THEN analytics and personalisedAds and performance are true`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        val collection = true
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returnsMany listOf(defaultIsAnalyticsEnabled, collection)

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returnsMany listOf(defaultIsPersonalizedAdsEnabled, true)

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returnsMany listOf(defaultIsPerformanceEnabled, true)

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setAnalyticsCollection(checked) } just Runs
        coEvery { settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs
        coEvery { settingsDataChoicesCollectionStates.setPerformanceCollection(checked) } just Runs

        mockInit(
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnAnalyticsCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState(),
            getInitState(
                analytics = checked,
                personalizedAds = checked,
                performance = checked,
            )
        )
        coVerify {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(checked)
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
            settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
        }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnAnalyticsCheck GIVEN checked is false and collection is false THEN analytics and personalisedAds and performance are false`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returnsMany listOf(defaultIsAnalyticsEnabled, collection)

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns defaultIsPerformanceEnabled

        val checked = false
        coEvery { settingsDataChoicesCollectionStates.setAnalyticsCollection(checked) } just Runs
        coEvery { settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs
        coEvery { settingsDataChoicesCollectionStates.setPerformanceCollection(checked) } just Runs

        mockInit(
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnAnalyticsCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState()
        )
        coVerify {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(checked)
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
            settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
        }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnAnalyticsCheck GIVEN build config is FOSS THEN do nothing`() = runTest {
        // Arrange
        val defaultIsAnalyticsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns defaultIsAnalyticsEnabled

        val defaultIsPersonalizedAdsEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns defaultIsPersonalizedAdsEnabled

        val defaultIsPerformanceEnabled = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns defaultIsPerformanceEnabled

        val checked = DataFactory.createRandomBoolean()

        appConfigProvider.setBuildFlavor(BuildFlavor.FOSS)

        mockInit(
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnAnalyticsCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState(
                analytics = null,
                personalizedAds = null,
                performance = null
            )
        )
        coVerify(exactly = 0) {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(any())
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(any())
            settingsDataChoicesCollectionStates.setPerformanceCollection(any())
        }
        testObserver.finish()
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

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setAnalyticsCollection(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsShowErrorProvider.analyticsError } returns ShowError(
            message,
            action
        )

        mockInit(
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnAnalyticsCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState(),
            getInitState(event = ShowError(message, action))
        )
        coVerify(exactly = 0) {
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(any())
            settingsDataChoicesCollectionStates.setPerformanceCollection(any())
        }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnPersonalizedAdsCheck GIVEN checked is true and collection is true THEN personalizedAds is true`() = runTest {
        // Arrange
        val defaultPersonalizedAdsEnabled = false
        val collection = true
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returnsMany listOf(defaultPersonalizedAdsEnabled, collection)

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs

        mockInit(
            analytics = false,
            performance = false,
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnPersonalizedAdsCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState(),
            getInitState(personalizedAds = checked)
        )
        coVerify {
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
        }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnPersonalizedAdsCheck GIVEN checked is false and collection is false THEN show only loading`() = runTest {
        // Arrange
        val defaultPersonalizedAdsEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returnsMany listOf(defaultPersonalizedAdsEnabled, collection)

        val checked = false
        coEvery { settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs

        mockInit(
            analytics = false,
            performance = false,
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnPersonalizedAdsCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState()
        )
        coVerify {
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
        }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnPersonalizedAdsCheck GIVEN checked is true and collection is false THEN event Error`() = runTest {
        // Arrange
        val defaultIsPersonalizedAdsEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returnsMany listOf(defaultIsPersonalizedAdsEnabled, collection)

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsShowErrorProvider.personalizedAdsError } returns ShowError(
            message,
            action
        )

        mockInit(
            analytics = false,
            performance = false,
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnPersonalizedAdsCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState(),
            getInitState(event = ShowError(message, action))
        )
        coVerify {
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
        }
        testObserver.finish()
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

        val checked = DataFactory.createRandomBoolean()

        appConfigProvider.setBuildFlavor(BuildFlavor.FOSS)

        mockInit(
            analytics = false,
            performance = false,
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnAnalyticsCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState(
                analytics = null,
                personalizedAds = null,
                performance = null
            )
        )
        coVerify(exactly = 0) {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(any())
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(any())
            settingsDataChoicesCollectionStates.setPerformanceCollection(any())
        }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnThemeSelect GIVEN theme id THEN update application theme and update theme and reset events`() = runTest {
        // Arrange
        val themeId = ApplicationTheme.LIGHT.id

        coEvery { settingsAppearanceCollectionStates.setApplicationThemeValue(ApplicationTheme.LIGHT) } just Runs

        mockInit(
            analytics = false,
            personalizedAds = false,
            performance = false,
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnThemeSelect(themeId))

        // Assert
        testObserver.assertValues(
            getInitState(),
            getInitState(
                theme = ApplicationTheme.LIGHT
            )
        )
        coVerify { settingsAppearanceCollectionStates.setApplicationThemeValue(ApplicationTheme.LIGHT) }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnThemePreferenceClick THEN send SelectTheme event`() = runTest {
        // Arrange
        val dialogTitle = DataFactory.createRandomString()
        every { settingsStringsProvider.getDialogThemeTitle() } returns dialogTitle

        val dialogThemes = listOf(
            DialogRadioItemBuilder().setId(DataFactory.createRandomInt()).build(),
            DialogRadioItemBuilder().setId(DataFactory.createRandomInt()).build(),
            DialogRadioItemBuilder().setId(DataFactory.createRandomInt()).build()
        )

        val defaultUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsStringsProvider.getDialogThemes(defaultUserTheme) } returns dialogThemes

        mockInit(
            analytics = false,
            personalizedAds = false,
            performance = false,
            crashReporter = false,
            theme = defaultUserTheme,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnThemePreferenceClick)

        // Assert
        testObserver.assertValues(
            getInitState(),
            getInitState(
                event = SelectTheme(
                    title = dialogTitle,
                    themes = dialogThemes
                )
            )
        )
        testObserver.finish()
    }

    @Test
    fun `WHEN OnThemeDismiss THEN send Idle event`() = runTest {
        // Arrange
        mockInit(
            analytics = false,
            personalizedAds = false,
            performance = false,
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnThemeDismiss)

        // Assert
        testObserver.assertValues(
            getInitState()
        )
        testObserver.finish()
    }

    @Test
    fun `WHEN OnPerformanceCheckChange GIVEN checked is true and collection is true THEN performance is true`() = runTest {
        // Arrange
        val defaultPerformanceEnabled = false
        val collection = true
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returnsMany listOf(defaultPerformanceEnabled, collection)

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setPerformanceCollection(checked) } just Runs

        mockInit(
            analytics = false,
            personalizedAds = false,
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnPerformanceCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState(),
            getInitState(
                performance = checked
            )
        )
        coVerify {
            settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
        }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnPerformanceCheckChange GIVEN checked is false and collection is false THEN show only loading`() = runTest {
        // Arrange
        val defaultPerformanceEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returnsMany listOf(defaultPerformanceEnabled, collection)

        val checked = false
        coEvery { settingsDataChoicesCollectionStates.setPerformanceCollection(checked) } just Runs

        mockInit(
            analytics = false,
            personalizedAds = false,
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnPerformanceCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState()
        )
        coVerify {
            settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
        }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnPerformanceCheckChange GIVEN checked is true and collection is false THEN event Error`() = runTest {
        // Arrange
        val defaultIsPerformanceEnabled = false
        val collection = false
        coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returnsMany listOf(defaultIsPerformanceEnabled, collection)

        val checked = true
        coEvery { settingsDataChoicesCollectionStates.setPerformanceCollection(checked) } just Runs

        val message = DataFactory.createRandomString()
        val action = DataFactory.createRandomString()
        every { settingsShowErrorProvider.performanceError } returns ShowError(
            message,
            action
        )

        mockInit(
            analytics = false,
            personalizedAds = false,
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnPerformanceCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState(),
            getInitState(event = ShowError(message, action))
        )
        coVerify {
            settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
        }
        testObserver.finish()
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

        val checked = DataFactory.createRandomBoolean()

        appConfigProvider.setBuildFlavor(BuildFlavor.FOSS)

        mockInit(
            analytics = false,
            personalizedAds = false,
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnPerformanceCheckChange(checked))

        // Assert
        testObserver.assertValues(
            getInitState(
                analytics = null,
                personalizedAds = null,
                performance = null
            )
        )
        coVerify(exactly = 0) {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(any())
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(any())
            settingsDataChoicesCollectionStates.setPerformanceCollection(any())
        }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnLanguageSelect GIVEN theme id THEN update application language and update language and reset events`() = runTest {
        // Arrange
        val languageId = ApplicationLanguage.ENGLISH.id

        coEvery { settingsAppearanceCollectionStates.setApplicationLanguageValue(ApplicationLanguage.ENGLISH) } just Runs

        mockInit(
            analytics = false,
            personalizedAds = false,
            performance = false,
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnLanguageSelect(languageId))

        // Assert
        testObserver.assertValues(
            getInitState(),
            getInitState(
                language = ApplicationLanguage.ENGLISH
            )
        )
        coVerify { settingsAppearanceCollectionStates.setApplicationLanguageValue(ApplicationLanguage.ENGLISH) }
        testObserver.finish()
    }

    @Test
    fun `WHEN OnLanguagePreferenceClick THEN send SelectLanguage event`() = runTest {
        // Arrange
        val defaultUserTheme = ApplicationTheme.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns defaultUserTheme

        val defaultApplicationLanguage = ApplicationLanguage.SYSTEM
        coEvery { settingsAppearanceCollectionStates.getApplicationLanguageValue() } returns defaultApplicationLanguage

        val dialogLanguages = listOf(
            DialogRadioItemBuilder().setId(DataFactory.createRandomInt()).build(),
            DialogRadioItemBuilder().setId(DataFactory.createRandomInt()).build(),
            DialogRadioItemBuilder().setId(DataFactory.createRandomInt()).build()
        )
        coEvery { settingsStringsProvider.getDialogLanguages(defaultApplicationLanguage) } returns dialogLanguages

        val dialogTitle = DataFactory.createRandomString()
        every { settingsStringsProvider.getDialogLanguageTitle() } returns dialogTitle

        mockInit(
            analytics = false,
            personalizedAds = false,
            performance = false,
            crashReporter = false,
            theme = defaultUserTheme,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnLanguagePreferenceClick)

        // Assert
        testObserver.assertValues(
            getInitState(),
            getInitState(
                event = SelectLanguage(
                    title = dialogTitle,
                    languages = dialogLanguages
                )
            )
        )
        testObserver.finish()
    }

    @Test
    fun `WHEN OnLanguageDismiss THEN send Idle event`() = runTest {
        // Arrange
        mockInit(
            analytics = false,
            personalizedAds = false,
            performance = false,
            crashReporter = false,
            theme = ApplicationTheme.SYSTEM,
            language = ApplicationLanguage.SYSTEM
        )

        val sut = getSettingsViewModel()
        testObserver = settingsStateHandler.state.test(this)

        // Act
        sut.sendIntent(OnLanguageDismiss)

        // Assert
        testObserver.assertValues(
            getInitState()
        )
        testObserver.finish()
    }

    private fun getSettingsViewModel() = SettingsViewModel(
        settingsStateHandler,
        splashScreenStateHandler,
        getScreenDestinationUseCase,
        settingsAppearanceCollectionStates,
        settingsDataChoicesCollectionStates,
        settingsShowErrorProvider,
        settingsStringsProvider,
        appConfigProvider
    )

    private fun mockInit(
        analytics: Boolean? = null,
        personalizedAds: Boolean? = null,
        performance: Boolean? = null,
        crashReporter: Boolean? = null,
        theme: ApplicationTheme? = null,
        language: ApplicationLanguage? = null
    ) {
        analytics?.let {
            coEvery { settingsDataChoicesCollectionStates.getAnalyticsCollectionValue() } returns it
        }
        personalizedAds?.let {
            coEvery { settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue() } returns it
        }
        performance?.let {
            coEvery { settingsDataChoicesCollectionStates.getPerformanceCollectionValue() } returns it
        }
        crashReporter?.let {
            coEvery { settingsDataChoicesCollectionStates.getCrashReporterCollectionValue() } returns it
        }
        theme?.let {
            coEvery { settingsAppearanceCollectionStates.getApplicationThemeValue() } returns it
        }
        language?.let {
            coEvery { settingsAppearanceCollectionStates.getApplicationLanguageValue() } returns it
        }
    }

    private fun getInitState(
        viewState: SettingsState.ViewState = SettingsState.ViewState.Success,
        event: SettingsEvent = Idle,
        analytics: Boolean? = false,
        personalizedAds: Boolean? = false,
        performance: Boolean? = false,
        crashReporter: Boolean = false,
        theme: ApplicationTheme = ApplicationTheme.SYSTEM,
        language: ApplicationLanguage = ApplicationLanguage.SYSTEM,
        version: String = "4.2"
    ) = SettingsState(
        viewState,
        event,
        analytics,
        personalizedAds,
        performance,
        crashReporter,
        theme,
        language,
        version
    )
}
