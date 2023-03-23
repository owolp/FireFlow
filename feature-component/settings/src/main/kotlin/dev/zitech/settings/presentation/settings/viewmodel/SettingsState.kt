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
import dev.zitech.core.common.presentation.architecture.MviState

internal data class SettingsState(
    val analytics: Boolean? = null,
    val analyticsError: Boolean = false,
    val applicationLanguage: ApplicationLanguage = ApplicationLanguage.SYSTEM,
    val applicationTheme: ApplicationTheme = ApplicationTheme.SYSTEM,
    val confirmLogOut: Boolean = false,
    val crashReporter: Boolean = false,
    val crashReporterError: Boolean = false,
    val email: String = "",
    val performance: Boolean? = null,
    val performanceError: Boolean = false,
    val personalizedAds: Boolean? = null,
    val personalizedAdsError: Boolean = false,
    val selectApplicationLanguage: ApplicationLanguage? = null,
    val selectApplicationTheme: ApplicationTheme? = null,
    val version: String = "",
    val viewState: ViewState = ViewState.InitScreen,
) : MviState {

    sealed class ViewState {
        object InitScreen : ViewState()
        object Success : ViewState()
    }

}
