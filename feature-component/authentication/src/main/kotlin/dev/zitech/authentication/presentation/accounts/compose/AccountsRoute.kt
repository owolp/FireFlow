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

package dev.zitech.authentication.presentation.accounts.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.authentication.presentation.accounts.viewmodel.AccountsViewModel
import dev.zitech.authentication.presentation.accounts.viewmodel.Idle
import dev.zitech.authentication.presentation.accounts.viewmodel.NavigateToDashboard
import dev.zitech.authentication.presentation.accounts.viewmodel.NavigationHandled
import dev.zitech.authentication.presentation.accounts.viewmodel.OnLoginClick

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun AccountsRoute(
    navigateToDashboard: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.event) {
        NavigateToDashboard -> {
            LaunchedEffect(Unit) {
                navigateToDashboard()
                viewModel.sendIntent(NavigationHandled)
            }
        }
        Idle -> {
            // NO_OP
        }
    }

    AccountsScreen(
        modifier = modifier,
        state = state,
        onLoginClick = { viewModel.sendIntent(OnLoginClick) }
    )
}