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

package dev.zitech.settings.presentation.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.domain.applicationconfig.AppConfigProvider
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.ApplicationLanguage
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.common.domain.model.BuildFlavor
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsAppearanceCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsDataChoicesCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.error.SettingsErrorProvider
import dev.zitech.settings.presentation.settings.viewmodel.theme.SettingsStringsProvider
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsStateHandler: SettingsStateHandler,
    private val settingsAppearanceCollectionStates: SettingsAppearanceCollectionStates,
    private val settingsDataChoicesCollectionStates: SettingsDataChoicesCollectionStates,
    private val settingsErrorProvider: SettingsErrorProvider,
    private val settingsStringsProvider: SettingsStringsProvider,
    private val appConfigProvider: AppConfigProvider
) : ViewModel(), MviViewModel<SettingsIntent, SettingsState> {

    private val tag = Logger.tag(this::class.java)

    override val state: StateFlow<SettingsState> = settingsStateHandler.state

    init {
        viewModelScope.launch {
            getPreferencesState()
        }
        Logger.i(tag, "Hello")
    }

    override fun sendIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is OnCrashReporterCheckChange -> handleOnCrashReporterCheckChange(intent.checked)
                is OnAnalyticsCheckChange -> handleOnAnalyticsCheckChange(intent.checked)
                is OnPersonalizedAdsCheckChange -> handleOnPersonalizedAdsCheckChange(intent.checked)
                is OnPerformanceCheckChange -> handleOnPerformanceCheckChange(intent.checked)
                is OnThemeSelect -> handleOnThemeSelect(intent.id)
                is OnLanguageSelect -> handleOnLanguageSelect(intent.id)
                OnThemePreferenceClick -> handleOnThemeClick()
                OnThemeDismiss -> handleOnThemeDismiss()
                OnLanguagePreferenceClick -> handleOnLanguagePreferenceClick()
                OnLanguageDismiss -> handleOnLanguageDismiss()
            }
        }
    }

    private suspend fun handleOnCrashReporterCheckChange(checked: Boolean) {
        settingsDataChoicesCollectionStates.setCrashReporterCollection(checked)
        val isEnabled = settingsDataChoicesCollectionStates.getCrashReporterCollectionValue()
        if (checked == isEnabled) {
            settingsStateHandler.setCrashReporterState(checked)
        } else {
            settingsStateHandler.setErrorState(settingsErrorProvider.getCrashReporterError())
        }
    }

    private suspend fun handleOnAnalyticsCheckChange(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            settingsDataChoicesCollectionStates.setAnalyticsCollection(checked)
            val isEnabled = settingsDataChoicesCollectionStates.getAnalyticsCollectionValue()
            if (checked == isEnabled) {
                settingsStateHandler.setAnalyticsState(checked, appConfigProvider.buildFlavor)
                settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
                settingsStateHandler.setPersonalizedAdsState(checked, appConfigProvider.buildFlavor)
                settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
                settingsStateHandler.setPerformanceState(checked, appConfigProvider.buildFlavor)
            } else {
                settingsStateHandler.setErrorState(settingsErrorProvider.getAnalyticsError())
            }
        } else {
            Logger.e(tag, "Setting analytics on FOSS build is not supported")
        }
    }

    private suspend fun handleOnThemeSelect(id: Int) {
        ApplicationTheme.values().first { it.id == id }.run {
            settingsAppearanceCollectionStates.setApplicationThemeValue(this)
            settingsStateHandler.setTheme(this)
            settingsStateHandler.resetEvent()
        }
    }

    private fun handleOnLanguageSelect(id: Int) {
        ApplicationLanguage.values().first { it.id == id }.run {
            settingsAppearanceCollectionStates.setApplicationLanguageValue(this)
            settingsStateHandler.setLanguage(this)
            settingsStateHandler.resetEvent()
        }
    }

    private fun handleOnThemeDismiss() {
        settingsStateHandler.resetEvent()
    }

    private fun handleOnLanguageDismiss() {
        settingsStateHandler.resetEvent()
    }

    private suspend fun handleOnPersonalizedAdsCheckChange(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            settingsDataChoicesCollectionStates.setAllowPersonalizedAdsValue(checked)
            val isEnabled = settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue()
            if (checked == isEnabled) {
                settingsStateHandler.setPersonalizedAdsState(checked, appConfigProvider.buildFlavor)
            } else {
                settingsStateHandler.setErrorState(settingsErrorProvider.getPersonalizedAdsError())
            }
        } else {
            Logger.e(tag, "Setting personalized ads on FOSS build is not supported")
        }
    }

    private suspend fun handleOnPerformanceCheckChange(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            settingsDataChoicesCollectionStates.setPerformanceCollection(checked)
            val isEnabled = settingsDataChoicesCollectionStates.getPerformanceCollectionValue()
            if (checked == isEnabled) {
                settingsStateHandler.setPerformanceState(checked, appConfigProvider.buildFlavor)
            } else {
                settingsStateHandler.setErrorState(settingsErrorProvider.getPerformanceError())
            }
        } else {
            Logger.e(tag, "Setting performance on FOSS build is not supported")
        }
    }

    private suspend fun getPreferencesState() {
        settingsStateHandler.run {
            setIsLoadingState(true)
            setAnalyticsState(
                settingsDataChoicesCollectionStates.getAnalyticsCollectionValue(),
                appConfigProvider.buildFlavor
            )
            setPersonalizedAdsState(
                settingsDataChoicesCollectionStates.getAllowPersonalizedAdsValue(),
                appConfigProvider.buildFlavor
            )
            setPerformanceState(
                settingsDataChoicesCollectionStates.getPerformanceCollectionValue(),
                appConfigProvider.buildFlavor
            )
            setCrashReporterState(settingsDataChoicesCollectionStates.getCrashReporterCollectionValue())
            setTheme(settingsAppearanceCollectionStates.getApplicationThemeValue())
            setLanguage(settingsAppearanceCollectionStates.getApplicationLanguageValue())
            setIsLoadingState(false)
        }
    }

    private fun handleOnThemeClick() {
        settingsStateHandler.setEvent(
            SelectTheme(
                title = settingsStringsProvider.getDialogThemeTitle(),
                themes = settingsStringsProvider.getDialogThemes(
                    settingsStateHandler.state.value.theme
                )
            )
        )
    }

    private fun handleOnLanguagePreferenceClick() {
        settingsStateHandler.setEvent(
            SelectLanguage(
                title = settingsStringsProvider.getDialogLanguageTitle(),
                languages = settingsStringsProvider.getDialogLanguages(
                    settingsStateHandler.state.value.language
                )
            )
        )
    }
}
