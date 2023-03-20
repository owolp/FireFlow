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

package dev.zitech.onboarding.presentation.oauth.viewmodel

import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.presentation.architecture.MviState

internal sealed interface OAuthEvent : MviState.Event

internal object Idle : OAuthEvent
internal object NavigateToDashboard : OAuthEvent
internal object NavigateBack : OAuthEvent
internal data class NavigateToFirefly(val url: String) : OAuthEvent
internal data class NavigateToError(val error: Error) : OAuthEvent
internal class ShowError(
    val messageResId: Int? = null,
    val text: String? = null
) : OAuthEvent
