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
import dev.zitech.core.common.presentation.architecture.MviState

data class SettingsState(
    val isLoading: Boolean = false,
    val analytics: Boolean? = null,
    val personalizedAds: Boolean? = null,
    val performance: Boolean? = null,
    val crashReporter: Boolean = false,
    val theme: ApplicationTheme = ApplicationTheme.SYSTEM,
    val language: ApplicationLanguage = ApplicationLanguage.SYSTEM,
    val event: SettingsEvent = Idle
) : MviState
