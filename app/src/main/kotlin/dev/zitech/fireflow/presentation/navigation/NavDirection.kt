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

package dev.zitech.fireflow.presentation.navigation

import dev.zitech.fireflow.common.presentation.navigation.destination.FireFlowNavigationDestination

internal data class NavDirection(
    val destination: FireFlowNavigationDestination,
    val route: String? = null,
    val inclusive: Boolean = DEFAULT_STATE_INCLUSIVE,
    val popUpToDestination: FireFlowNavigationDestination? = null,
    val restoreState: Boolean = DEFAULT_STATE_RESTORE_STATE
) {
    companion object {
        const val DEFAULT_STATE_INCLUSIVE = false
        const val DEFAULT_STATE_RESTORE_STATE = true
    }
}
