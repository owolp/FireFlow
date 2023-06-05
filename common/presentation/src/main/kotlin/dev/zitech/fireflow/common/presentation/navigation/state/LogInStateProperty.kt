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

import dev.zitech.fireflow.common.presentation.navigation.ScreenDestinationProvider
import dev.zitech.fireflow.common.presentation.navigation.deeplink.DeepLinkScreenDestination
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Creates a [ReadOnlyProperty] delegate for accessing a [StateFlow] of [LogInState] values.
 *
 * This function creates a delegate that provides read-only access to a [StateFlow] of [LogInState] values.
 * The delegate is initialized with a default initial state of [LogInState.InitScreen].
 * It uses the provided `getScreenDestinationUseCase` to obtain the screen destination and updates the
 * [logInState] accordingly. When a new screen destination is received, the [logInState] is updated based on
 * the destination type, and the [loginCheckCompletedHandler] is invoked with `true` to indicate that the
 * login check is completed.
 *
 * @param getScreenDestinationUseCase The use case for obtaining the screen destination.
 * @param loginCheckCompletedHandler The handler to be called when the login check is completed.
 * @param coroutineScope The [CoroutineScope] to use for launching the collection of screen destinations.
 *
 * @return A [ReadOnlyProperty] delegate providing read-only access to the [StateFlow] of [LogInState] values.
 */
fun logInState(
    getScreenDestinationUseCase: ScreenDestinationProvider,
    loginCheckCompletedHandler: LoginCheckCompletedHandler,
    coroutineScope: CoroutineScope
): ReadOnlyProperty<Any, StateFlow<LogInState>> =
    object : ReadOnlyProperty<Any, StateFlow<LogInState>> {

        private val logInState = MutableStateFlow<LogInState>(LogInState.InitScreen)

        override fun getValue(thisRef: Any, property: KProperty<*>): StateFlow<LogInState> =
            logInState.asStateFlow()

        init {
            getScreenDestination()
        }

        private fun getScreenDestination() {
            getScreenDestinationUseCase().onEach { destination ->
                logInState.emit(
                    when (destination) {
                        DeepLinkScreenDestination.Accounts,
                        is DeepLinkScreenDestination.Error,
                        DeepLinkScreenDestination.Welcome ->
                            LogInState.NotLogged(destination)

                        DeepLinkScreenDestination.Current ->
                            LogInState.Logged

                        DeepLinkScreenDestination.Init ->
                            LogInState.InitScreen
                    }.also {
                        loginCheckCompletedHandler(true)
                    }
                )
            }.launchIn(coroutineScope)
        }
    }
