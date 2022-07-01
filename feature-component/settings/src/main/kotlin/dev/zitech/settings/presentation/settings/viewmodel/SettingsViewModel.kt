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
import dev.zitech.core.reporter.analytics.domain.usecase.SetAnalyticsCollectionUseCase
import dev.zitech.core.reporter.crash.domain.usecase.SetCrashCollectionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setCrashCollectionUseCase: SetCrashCollectionUseCase,
    private val setAnalyticsCollectionUseCase: SetAnalyticsCollectionUseCase
) : ViewModel(), MviViewModel<SettingsIntent, SettingsState> {

    private val mutableState = MutableStateFlow(SettingsState())
    override val state: StateFlow<SettingsState> = mutableState

    override fun sendIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is OnCrashReporterCheck -> handleOnCrashReporterCheck(intent.checked)
                is OnTelemetryCheck -> handleOnTelemetryCheck(intent.checked)
            }
        }
    }

    private suspend fun handleOnTelemetryCheck(checked: Boolean) {
        // TODO: Show Loading
        setAnalyticsCollectionUseCase(checked)
        // TODO: Update value from getAnalyticsCollectionUseCase
        // TODO: If getAnalyticsCollectionUseCase != checked -> Show Error
        mutableState.update {
            it.copy(telemetry = checked)
        }
        // TODO: Hide Loading
    }

    private suspend fun handleOnCrashReporterCheck(checked: Boolean) {
        // TODO: Show Loading
        setCrashCollectionUseCase(checked)
        // TODO: Update value from getCrashCollectionUseCase
        // TODO: If getCrashCollectionUseCase != checked -> Show Error
        mutableState.update {
            it.copy(crashReporter = checked)
        }
        // TODO: Hide Loading
    }
}
