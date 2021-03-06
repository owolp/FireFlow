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

import dev.zitech.core.common.presentation.architecture.MviState
import dev.zitech.ds.molecules.dialog.DialogRadioItem

sealed interface SettingsEvent : MviState.Event

internal object Idle : SettingsEvent
internal data class Error(
    val message: String,
    val action: String? = null
) : SettingsEvent

internal data class Dialog(
    val title: String,
    val text: String
) : SettingsEvent

internal data class SelectTheme(
    val title: String,
    val themes: List<DialogRadioItem>
) : SettingsEvent
