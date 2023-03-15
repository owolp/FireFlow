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

package dev.zitech.onboarding.presentation.pat.viewmodel

import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.presentation.architecture.MviState

internal sealed interface PatEvent : MviState.Event

internal object Idle : PatEvent
internal object NavigateToDashboard : PatEvent
internal object NavigateBack : PatEvent
internal data class NavigateToError(val error: Error) : PatEvent
internal class ShowError(
    val messageResId: Int? = null,
    val text: String? = null
) : PatEvent
