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

package dev.zitech.core.common.presentation.splash

import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SplashScreenStateController {

    val state: StateFlow<Boolean>

    operator fun invoke(toShow: Boolean)
}

internal class SplashScreenStateControllerImpl @Inject constructor() : SplashScreenStateController {

    private val mutableState = MutableStateFlow(true)
    override val state: StateFlow<Boolean> = mutableState.asStateFlow()

    override operator fun invoke(toShow: Boolean) {
        mutableState.update { toShow }
    }
}
