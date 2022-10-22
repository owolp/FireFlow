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

import dev.zitech.core.common.domain.model.ApplicationLanguage
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.common.domain.model.BuildFlavor
import dev.zitech.core.common.presentation.architecture.MviStateHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@Suppress("TooManyFunctions")
internal class SettingsStateHandler @Inject constructor() : MviStateHandler<SettingsState> {

    private val mutableState = MutableStateFlow(SettingsState())
    override val state: StateFlow<SettingsState> = mutableState.asStateFlow()

    fun setIsLoadingState(value: Boolean) {
        mutableState.update {
            it.copy(isLoading = value)
        }
    }

    fun setAnalyticsState(
        value: Boolean,
        buildFlavor: BuildFlavor
    ) {
        if (buildFlavor != BuildFlavor.FOSS) {
            mutableState.update {
                it.copy(analytics = value)
            }
        }
    }

    fun setPersonalizedAdsState(
        value: Boolean,
        buildFlavor: BuildFlavor
    ) {
        if (buildFlavor != BuildFlavor.FOSS) {
            mutableState.update {
                it.copy(personalizedAds = value)
            }
        }
    }

    fun setPerformanceState(
        value: Boolean,
        buildFlavor: BuildFlavor
    ) {
        if (buildFlavor != BuildFlavor.FOSS) {
            mutableState.update {
                it.copy(performance = value)
            }
        }
    }

    fun setCrashReporterState(value: Boolean) {
        mutableState.update {
            it.copy(crashReporter = value)
        }
    }

    fun setErrorState(value: SettingsEvent) {
        mutableState.update {
            it.copy(event = value)
        }
    }

    fun resetEvent() {
        mutableState.update {
            it.copy(event = Idle)
        }
    }

    fun setTheme(value: ApplicationTheme) {
        mutableState.update {
            it.copy(theme = value)
        }
    }

    fun setLanguage(value: ApplicationLanguage) {
        mutableState.update {
            it.copy(language = value)
        }
    }

    fun setAppVersion(value: String) {
        mutableState.update {
            it.copy(version = value)
        }
    }

    fun setEvent(event: SettingsEvent) {
        mutableState.update {
            it.copy(event = event)
        }
    }
}
