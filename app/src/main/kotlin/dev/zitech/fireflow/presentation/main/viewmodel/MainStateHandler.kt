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

package dev.zitech.fireflow.presentation.main.viewmodel

import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.common.presentation.architecture.MviStateHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MainStateHandler @Inject constructor() : MviStateHandler<MainState> {

    private val mutableState = MutableStateFlow(MainState())
    override val state: StateFlow<MainState> = mutableState.asStateFlow()

    fun setSplash(value: Boolean) {
        mutableState.update {
            it.copy(splash = value)
        }
    }

    fun setTheme(value: ApplicationTheme) {
        mutableState.update {
            it.copy(theme = value)
        }
    }

    fun setEvent(value: MainEvent) {
        mutableState.update {
            it.copy(event = value)
        }
    }
}
