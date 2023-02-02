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

package dev.zitech.onboarding.presentation.oauth.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.onboarding.presentation.oauth.viewmodel.Idle
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigateBack
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigateToDashboard
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigationHandled
import dev.zitech.onboarding.presentation.oauth.viewmodel.OauthViewModel
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnBackClick
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnClientIdChange
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnClientSecretChange
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnLoginClick
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnServerAddressChange

@Composable
internal fun OauthRoute(
    navigateToDashboard: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OauthViewModel = hiltViewModel()
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
    OauthScreen(
        modifier = modifier,
        oauthState = screenState,
        snackbarState = snackbarState,
        onLoginClick = { viewModel.sendIntent(OnLoginClick) },
        onBackClick = { viewModel.sendIntent(OnBackClick) },
        onServerAddressChange = { viewModel.sendIntent(OnServerAddressChange(it)) },
        onClientIdChange = { viewModel.sendIntent(OnClientIdChange(it)) },
        onClientSecretChange = { viewModel.sendIntent(OnClientSecretChange(it)) }
    )
}
