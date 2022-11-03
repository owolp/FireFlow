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

package dev.zitech.onboarding.presentation.login.viewmodel

import dev.zitech.core.common.presentation.architecture.MviStateHandler
import dev.zitech.onboarding.presentation.login.model.LoginType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class LoginStateHandler @Inject constructor() : MviStateHandler<LoginState> {

    private val mutableState = MutableStateFlow(LoginState())
    override val state: StateFlow<LoginState> = mutableState.asStateFlow()

    fun setEvent(event: LoginEvent) {
        mutableState.update {
            it.copy(event = event)
        }
    }

    fun resetEvent() {
        mutableState.update {
            it.copy(event = Idle)
        }
    }

    fun setLoginType(value: LoginType) {
        mutableState.update {
            it.copy(loginType = value)
        }
    }
}
