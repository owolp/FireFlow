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

package dev.zitech.fireflow.common.presentation.navigation.state

import dev.zitech.fireflow.common.presentation.navigation.deeplink.DeepLinkScreenDestination

/**
 * The `LogInState` sealed class represents the possible states related to logging in within the app.
 *
 * This sealed class includes three possible states:
 * - `NotLogged`: Represents the state when a user has not yet logged in, and provides a `DeepLinkScreenDestination`
 *                object that describes the destination screen for the user to log in.
 * - `InitScreen`: Represents the initial screen state before a user has attempted to log in.
 * - `Logged`: Represents the state when a user has successfully logged in.
 */
sealed class LogInState {

    data class NotLogged(
        val destination: DeepLinkScreenDestination
    ) : LogInState()

    object InitScreen : LogInState()

    object Logged : LogInState()
}
