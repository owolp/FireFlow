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

package dev.zitech.fireflow.common.presentation.navigation.destination

/**
 * Interface representing a navigation destination in the FireFlow navigation system.
 */
interface FireFlowNavigationDestination {

    /**
     * The route associated with the navigation destination.
     * It is used for navigating to the destination within the app.
     */
    val route: String

    /**
     * The name or identifier of the navigation destination.
     * It provides a descriptive name for the destination.
     */
    val destination: String
}
