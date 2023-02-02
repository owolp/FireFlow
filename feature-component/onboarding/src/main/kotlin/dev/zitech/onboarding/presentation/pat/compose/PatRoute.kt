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

package dev.zitech.onboarding.presentation.pat.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.onboarding.presentation.pat.viewmodel.Idle
import dev.zitech.onboarding.presentation.pat.viewmodel.NavigateBack
import dev.zitech.onboarding.presentation.pat.viewmodel.NavigateToDashboard
import dev.zitech.onboarding.presentation.pat.viewmodel.NavigationHandled
import dev.zitech.onboarding.presentation.pat.viewmodel.OnBackClick
import dev.zitech.onboarding.presentation.pat.viewmodel.OnLoginClick
import dev.zitech.onboarding.presentation.pat.viewmodel.PatViewModel

@Composable
internal fun PatRoute(
    navigateToDashboard: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PatViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val snackbarState = rememberSnackbarState()

    when (screenState.event) {
        NavigateToDashboard -> {
            LaunchedEffect(Unit) {
                navigateToDashboard()
                viewModel.sendIntent(NavigationHandled)
            }
        }
        NavigateBack -> {
            navigateBack()
            viewModel.sendIntent(NavigationHandled)
        }
        Idle -> {
            // NO_OP
        }
    }
    PatScreen(
        modifier = modifier,
        patState = screenState,
        snackbarState = snackbarState,
        onLoginClick = { viewModel.sendIntent(OnLoginClick) },
        onBackClick = { viewModel.sendIntent(OnBackClick) }
    )
}
