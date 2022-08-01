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
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.common.domain.model.BuildFlavor
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsAppearanceCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsDataChoicesCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.error.SettingsErrorProvider
import dev.zitech.settings.presentation.settings.viewmodel.theme.SettingsThemeProvider
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsStateHandler: SettingsStateHandler,
    private val settingsAppearanceCollectionStates: SettingsAppearanceCollectionStates,
    private val settingsDataChoicesCollectionStates: SettingsDataChoicesCollectionStates,
    private val settingsErrorProvider: SettingsErrorProvider,
    private val settingsThemeProvider: SettingsThemeProvider,
    private val appConfigProvider: AppConfigProvider
) : ViewModel(), MviViewModel<SettingsIntent, SettingsState> {

    companion object {
        private const val TAG = "SettingsViewModel"
    }

    override val state: StateFlow<SettingsState> = settingsStateHandler.state

    init {
        viewModelScope.launch {
            getPreferencesState()
        }
    }

    override fun sendIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is OnCrashReporterCheckChange -> handleOnCrashReporterCheckChange(intent.checked)
                is OnAnalyticsCheckChange -> handleOnAnalyticsCheckChange(intent.checked)
                is OnPersonalizedAdsCheckChange -> handleOnPersonalizedAdsCheckChange(intent.checked)
                is OnPerformanceCheckChange -> handleOnPerformanceCheckChange(intent.checked)
                is OnThemeSelect -> handleOnThemeSelect(intent.id)
                OnThemePreferenceClick -> handleOnThemeClick()
                OnThemeDismiss -> handleOnThemeDismiss()
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
            Logger.e(TAG, "Setting analytics on FOSS build is not supported")
        }
    }

    private suspend fun handleOnThemeSelect(id: Int) {
        ApplicationTheme.values().first { it.id == id }.run {
            settingsAppearanceCollectionStates.saveApplicationThemeValue(this)
            settingsStateHandler.setTheme(this)
            settingsStateHandler.resetEvent()
        }
    }

    private fun handleOnThemeDismiss() {
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
            Logger.e(TAG, "Setting personalized ads on FOSS build is not supported")
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
            Logger.e(TAG, "Setting performance on FOSS build is not supported")
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
            setIsLoadingState(false)
        }
    }

    private suspend fun handleOnThemeClick() {
        settingsStateHandler.setEvent(
            SelectTheme(
                title = settingsThemeProvider.getDialogThemeTitle(),
                themes = settingsThemeProvider.getDialogThemes(
                    settingsAppearanceCollectionStates.getApplicationThemeValue()
                )
            )
        )
    }
}
