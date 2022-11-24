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

package dev.zitech.dashboard.presentation.dashboard.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.dashboard.presentation.dashboard.viewmodel.DashboardState.ViewState.InitScreen
import dev.zitech.dashboard.presentation.dashboard.viewmodel.DashboardState.ViewState.NotLoggedIn
import dev.zitech.dashboard.presentation.dashboard.viewmodel.DashboardState.ViewState.Success
import dev.zitech.dashboard.presentation.dashboard.viewmodel.DashboardViewModel
import dev.zitech.ds.atoms.loading.FireFlowProgressIndicators
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase.Destination.Accounts
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase.Destination.Current
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase.Destination.Error
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase.Destination.Welcome

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun DashboardRoute(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel(),
    navigateToAccounts: () -> Unit,
    navigateToError: () -> Unit,
    navigateToWelcome: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val viewState = state.viewState) {
        InitScreen -> {
            FireFlowProgressIndicators.Magnifier()
        }
        Success -> {
            DashboardScreen(
                modifier = modifier,
                state = state
            )
        }
        is NotLoggedIn -> {
            LaunchedEffect(Unit) {
                when (viewState.destination) {
                    Accounts -> navigateToAccounts()
                    Error -> navigateToError()
                    Welcome -> navigateToWelcome()
                    Current -> {
                        // NO_OP
                    }
                }
            }
        }
    }
}
