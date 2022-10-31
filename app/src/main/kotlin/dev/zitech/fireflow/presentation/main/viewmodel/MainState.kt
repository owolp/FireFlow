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
import dev.zitech.core.common.presentation.architecture.MviState
import dev.zitech.core.common.presentation.navigation.FireFlowNavigationDestination

data class MainState(
    val splash: Boolean = true,
    val theme: ApplicationTheme? = null,
    val startDestination: FireFlowNavigationDestination? = null,
    val event: MainEvent = Idle
) : MviState
