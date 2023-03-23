/*
 * Copyright (C) 2023 Zitech Ltd.
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
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Suppress("TooManyFunctions")
internal class SettingsStateHandler @Inject constructor() : MviStateHandler<SettingsState> {

    private val mutableState = MutableStateFlow(SettingsState())

    override val state: StateFlow<SettingsState> = mutableState.asStateFlow()

    fun setAnalyticsError(value: Boolean) {
        mutableState.update { it.copy(analyticsError = value) }
    }

    fun setAnalyticsState(
        value: Boolean,
        buildFlavor: BuildFlavor
    ) {
        if (buildFlavor != BuildFlavor.FOSS) {
            mutableState.update { it.copy(analytics = value) }
        }
    }

    fun setAppVersionState(value: String) {
        mutableState.update { it.copy(version = value) }
    }

    fun setApplicationLanguage(value: ApplicationLanguage) {
        mutableState.update { it.copy(applicationLanguage = value) }
    }

    fun setApplicationThemeState(value: ApplicationTheme) {
        mutableState.update { it.copy(applicationTheme = value) }
    }

    fun setConfirmLogOut(value: Boolean) {
        mutableState.update { it.copy(confirmLogOut = value) }
    }

    fun setCrashReporter(value: Boolean) {
        mutableState.update { it.copy(crashReporter = value) }
    }

    fun setCrashReporterError(value: Boolean) {
        mutableState.update { it.copy(crashReporterError = value) }
    }

    fun setEmail(value: String) {
        mutableState.update { it.copy(email = value) }
    }

    fun setPerformance(
        value: Boolean,
        buildFlavor: BuildFlavor
    ) {
        if (buildFlavor != BuildFlavor.FOSS) {
            mutableState.update { it.copy(performance = value) }
        }
    }

    fun setPerformanceError(value: Boolean) {
        mutableState.update { it.copy(performanceError = value) }
    }

    fun setPersonalizedAds(
        value: Boolean,
        buildFlavor: BuildFlavor
    ) {
        if (buildFlavor != BuildFlavor.FOSS) {
            mutableState.update { it.copy(personalizedAds = value) }
        }
    }

    fun setPersonalizedAdsError(value: Boolean) {
        mutableState.update { it.copy(personalizedAdsError = value) }
    }

    fun setSelectApplicationLanguage(value: ApplicationLanguage?) {
        mutableState.update { it.copy(selectApplicationLanguage = value) }
    }

    fun setSelectApplicationTheme(value: ApplicationTheme?) {
        mutableState.update { it.copy(selectApplicationTheme = value) }
    }

    fun setViewState(value: SettingsState.ViewState) {
        mutableState.update { it.copy(viewState = value) }
    }

}
