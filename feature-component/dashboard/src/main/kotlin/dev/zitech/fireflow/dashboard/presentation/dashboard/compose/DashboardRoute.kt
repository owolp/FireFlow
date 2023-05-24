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

package dev.zitech.fireflow.dashboard.presentation.dashboard.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.fireflow.dashboard.presentation.dashboard.viewmodel.DashboardViewModel
import dev.zitech.fireflow.common.presentation.navigation.deeplink.DeepLinkScreenDestination
import dev.zitech.fireflow.common.presentation.navigation.state.LogInState
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.ds.atoms.loading.FireFlowProgressIndicators

@Composable
internal fun DashboardRoute(
    modifier: Modifier = Modifier,
    navigateToAccounts: () -> Unit,
    navigateToError: (error: Error) -> Unit,
    navigateToWelcome: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val screenState by viewModel.state.collectAsStateWithLifecycle()
    val logInState by viewModel.logInState.collectAsStateWithLifecycle()

    when (val state = logInState) {
        LogInState.InitScreen -> {
            FireFlowProgressIndicators.Magnifier()
        }

        LogInState.Logged -> {
            DashboardScreen(
                modifier = modifier,
                state = screenState
            )
        }

        is LogInState.NotLogged -> {
            LaunchedEffect(Unit) {
                when (val destination = state.destination) {
                    DeepLinkScreenDestination.Accounts -> navigateToAccounts()
                    is DeepLinkScreenDestination.Error ->
                        navigateToError(destination.error)

                    DeepLinkScreenDestination.Welcome -> navigateToWelcome()
                    DeepLinkScreenDestination.Current,
                    DeepLinkScreenDestination.Init -> {
                        // NO_OP
                    }
                }
            }
        }
    }
}
