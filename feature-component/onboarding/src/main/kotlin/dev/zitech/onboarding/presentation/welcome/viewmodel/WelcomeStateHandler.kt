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

import dev.zitech.core.common.presentation.architecture.MviStateHandler
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class WelcomeStateHandler @Inject constructor() : MviStateHandler<WelcomeState> {

    private val mutableState = MutableStateFlow(WelcomeState())

    override val state: StateFlow<WelcomeState> = mutableState.asStateFlow()

    fun resetEvent() {
        mutableState.update { it.copy(event = Idle) }
    }

    fun setEvent(event: WelcomeEvent) {
        mutableState.update { it.copy(event = event) }
    }
}
