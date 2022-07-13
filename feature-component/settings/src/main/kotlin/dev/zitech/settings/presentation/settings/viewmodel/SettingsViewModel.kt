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
import dev.zitech.core.common.domain.model.BuildFlavor
import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.persistence.domain.usecase.preferences.GetAnalyticsCollectionValueUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetCrashReporterCollectionValueUseCase
import dev.zitech.core.reporter.analytics.domain.usecase.SetAnalyticsCollectionUseCase
import dev.zitech.core.reporter.crash.domain.usecase.SetCrashReporterCollectionUseCase
import dev.zitech.settings.R
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsStateHolder: SettingsStateHolder,
    private val getAnalyticsCollectionValueUseCase: GetAnalyticsCollectionValueUseCase,
    private val setAnalyticsCollectionUseCase: SetAnalyticsCollectionUseCase,
    private val getCrashReporterCollectionValueUseCase: GetCrashReporterCollectionValueUseCase,
    private val setCrashReporterCollectionUseCase: SetCrashReporterCollectionUseCase,
    private val stringsProvider: StringsProvider,
    private val appConfigProvider: AppConfigProvider
) : ViewModel(), MviViewModel<SettingsIntent, SettingsState> {

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
            }
        }
    }

    private suspend fun handleOnTelemetryCheck(checked: Boolean) {
        setAnalyticsCollectionUseCase(checked)
        val isEnabled = getAnalyticsCollectionValueUseCase()
        if (checked == isEnabled) {
            setTelemetryState(checked, appConfigProvider.buildFlavor)
        } else {
            setErrorState(
                Error(
                    message = stringsProvider(R.string.data_choices_telemetry_error),
                    action = stringsProvider(R.string.action_restart)
                )
            )
        }
    }

    private suspend fun handleOnCrashReporterCheck(checked: Boolean) {
        setCrashReporterCollectionUseCase(checked)
        val isEnabled = getCrashReporterCollectionValueUseCase()
        if (checked == isEnabled) {
            setCrashReporterState(checked)
        } else {
            setErrorState(
                Error(
                    message = stringsProvider(R.string.data_choices_crash_reporter_error),
                    action = stringsProvider(R.string.action_restart)
                )
            )
        }
    }

    private suspend fun getPreferencesState() {
        setIsLoadingState(true)
        setTelemetryState(
            getAnalyticsCollectionValueUseCase(),
            appConfigProvider.buildFlavor
        )
        setCrashReporterState(getCrashReporterCollectionValueUseCase())
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
