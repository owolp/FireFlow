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
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.persistence.domain.usecase.preferences.GetAnalyticsCollectionValueUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetCrashReporterCollectionValueUseCase
import dev.zitech.core.reporter.analytics.domain.usecase.SetAnalyticsCollectionUseCase
import dev.zitech.core.reporter.crash.domain.usecase.SetCrashReporterCollectionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getAnalyticsCollectionValueUseCase: GetAnalyticsCollectionValueUseCase,
    private val setAnalyticsCollectionUseCase: SetAnalyticsCollectionUseCase,
    private val getCrashReporterCollectionValueUseCase: GetCrashReporterCollectionValueUseCase,
    private val setCrashReporterCollectionUseCase: SetCrashReporterCollectionUseCase
) : ViewModel(), MviViewModel<SettingsIntent, SettingsState> {

    private val mutableState = MutableStateFlow(SettingsState())
    override val state: StateFlow<SettingsState> = mutableState

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
            mutableState.update {
                it.copy(telemetry = checked)
            }
        } else {
            // TODO: Show Error
        }
    }

    private suspend fun handleOnCrashReporterCheck(checked: Boolean) {
        setCrashReporterCollectionUseCase(checked)
        val isEnabled = getCrashReporterCollectionValueUseCase()
        if (checked == isEnabled) {
            mutableState.update {
                it.copy(crashReporter = checked)
            }
        } else {
            // TODO: Show Error
        }
    }

    private suspend fun getPreferencesState() {
        mutableState.update {
            it.copy(isLoading = true)
        }
        getTelemetryState()
        getCrashReporterState()
        mutableState.update {
            it.copy(isLoading = false)
        }
    }

    private suspend fun getTelemetryState() {
        mutableState.update {
            it.copy(telemetry = getAnalyticsCollectionValueUseCase())
        }
    }

    private suspend fun getCrashReporterState() {
        mutableState.update {
            it.copy(crashReporter = getCrashReporterCollectionValueUseCase())
        }
    }
}
