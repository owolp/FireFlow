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
import dev.zitech.core.common.domain.model.BuildFlavor
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsAnalyticsCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.collection.SettingsCrashReporterCollectionStates
import dev.zitech.settings.presentation.settings.viewmodel.error.SettingsErrorProvider
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsStateHolder: SettingsStateHolder,
    private val settingsAnalyticsCollectionStates: SettingsAnalyticsCollectionStates,
    private val settingsCrashReporterCollectionStates: SettingsCrashReporterCollectionStates,
    private val settingsErrorProvider: SettingsErrorProvider,
    private val appConfigProvider: AppConfigProvider
) : ViewModel(), MviViewModel<SettingsIntent, SettingsState> {

    companion object {
        private const val TAG = "SettingsViewModel"
    }

    override val state: StateFlow<SettingsState> = settingsStateHolder.state.asStateFlow()

    init {
        viewModelScope.launch {
            getPreferencesState()
        }
    }

    override fun sendIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is OnCrashReporterCheck -> handleOnCrashReporterCheck(intent.checked)
                is OnTelemetryCheck -> handleOnTelemetryCheck(intent.checked)
                is OnPersonalizedAdsCheck -> handleOnPersonalizedAdsCheck(intent.checked)
            }
        }
    }

    private suspend fun handleOnCrashReporterCheck(checked: Boolean) {
        settingsCrashReporterCollectionStates.setCrashReporterCollection(checked)
        val isEnabled = settingsCrashReporterCollectionStates.getCrashReporterCollectionValue()
        if (checked == isEnabled) {
            setCrashReporterState(checked)
        } else {
            setErrorState(settingsErrorProvider.getCrashReporterError())
        }
    }

    private suspend fun handleOnTelemetryCheck(checked: Boolean) {
        if (appConfigProvider.buildFlavor != BuildFlavor.FOSS) {
            settingsAnalyticsCollectionStates.setAnalyticsCollection(checked)
            val isEnabled = settingsAnalyticsCollectionStates.getAnalyticsCollectionValue()
            if (checked == isEnabled) {
                setTelemetryState(checked, appConfigProvider.buildFlavor)
                settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(checked)
                setPersonalizedAdsState(checked, appConfigProvider.buildFlavor)
            } else {
                setErrorState(settingsErrorProvider.getTelemetryError())
            }
        } else {
            Logger.e(TAG, "Setting telemetry on FOSS build is not supported")
        }
    }

    private suspend fun handleOnPersonalizedAdsCheck(checked: Boolean) {
        settingsAnalyticsCollectionStates.setAllowPersonalizedAdsValue(checked)
        val isEnabled = settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue()
        if (checked == isEnabled) {
            setPersonalizedAdsState(checked, appConfigProvider.buildFlavor)
        } else {
            setErrorState(settingsErrorProvider.getPersonalizedAdsError())
        }
    }

    private suspend fun getPreferencesState() {
        setIsLoadingState(true)
        setTelemetryState(
            settingsAnalyticsCollectionStates.getAnalyticsCollectionValue(),
            appConfigProvider.buildFlavor
        )
        setPersonalizedAdsState(
            settingsAnalyticsCollectionStates.getAllowPersonalizedAdsValue(),
            appConfigProvider.buildFlavor
        )
        setCrashReporterState(settingsCrashReporterCollectionStates.getCrashReporterCollectionValue())
        setIsLoadingState(false)
    }

    private fun setIsLoadingState(value: Boolean) {
        settingsStateHolder.state.update {
            it.copy(isLoading = value)
        }
    }

    private fun setTelemetryState(
        value: Boolean,
        buildFlavor: BuildFlavor
    ) {
        if (buildFlavor != BuildFlavor.FOSS) {
            settingsStateHolder.state.update {
                it.copy(telemetry = value)
            }
        }
    }

    private fun setPersonalizedAdsState(
        value: Boolean,
        buildFlavor: BuildFlavor
    ) {
        if (buildFlavor != BuildFlavor.FOSS) {
            settingsStateHolder.state.update {
                it.copy(personalizedAds = value)
            }
        }
    }

    private fun setCrashReporterState(value: Boolean) {
        settingsStateHolder.state.update {
            it.copy(crashReporter = value)
        }
    }

    private fun setErrorState(value: SettingsEvent) {
        settingsStateHolder.state.update {
            it.copy(event = value)
        }
    }
}
