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

package dev.zitech.navigation.presentation.extension

import dev.zitech.core.common.domain.navigation.DeepLinkScreenDestination
import dev.zitech.core.common.domain.navigation.LogInState
import dev.zitech.core.common.presentation.splash.SplashScreenStateHandler
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun logInState(
    getScreenDestinationUseCase: GetScreenDestinationUseCase,
    splashScreenStateHandler: SplashScreenStateHandler,
    coroutineScope: CoroutineScope
): ReadOnlyProperty<Any, StateFlow<LogInState>> =
    object : ReadOnlyProperty<Any, StateFlow<LogInState>> {

        init {
            getScreenDestination()
        }

        private val logInState = MutableStateFlow<LogInState>(LogInState.InitScreen)

        override fun getValue(thisRef: Any, property: KProperty<*>): StateFlow<LogInState> =
            logInState.asStateFlow()

        private fun getScreenDestination() {
            getScreenDestinationUseCase().onEach { destination ->
                logInState.emit(
                    when (destination) {
                        DeepLinkScreenDestination.Accounts,
                        DeepLinkScreenDestination.Error,
                        DeepLinkScreenDestination.Welcome ->
                            LogInState.NotLogged(destination)
                        DeepLinkScreenDestination.Current ->
                            LogInState.Logged
                        DeepLinkScreenDestination.Init ->
                            LogInState.InitScreen
                    }.also {
                        hideSplashScreen()
                    }
                )
            }.launchIn(coroutineScope)
        }

        private fun hideSplashScreen() {
            coroutineScope.launch {
                splashScreenStateHandler(false)
            }
        }
    }
