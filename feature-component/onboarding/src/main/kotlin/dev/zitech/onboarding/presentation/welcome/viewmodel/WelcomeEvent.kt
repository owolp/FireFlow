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

package dev.zitech.onboarding.presentation.welcome.viewmodel

import dev.zitech.core.common.domain.exception.FireFlowException
import dev.zitech.core.common.presentation.architecture.MviState

internal sealed interface WelcomeEvent : MviState.Event

internal object Idle : WelcomeEvent
internal object NavigateToOAuth : WelcomeEvent
internal object NavigateToPat : WelcomeEvent
internal object NavigateToDemo : WelcomeEvent
internal data class NavigateToFirefly(val url: String) : WelcomeEvent

internal object NavigateOutOfApp : WelcomeEvent
internal data class ShowDemoWarning(
    val text: String,
    val confirm: String
) : WelcomeEvent

internal data class ShowError(
    val messageResId: Int? = null,
    val text: String? = null
) : WelcomeEvent

internal data class NavigateToError(val exception: FireFlowException) : WelcomeEvent
