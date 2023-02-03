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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.coroutineScope
import dev.zitech.core.common.framework.browser.Browser
import dev.zitech.ds.molecules.snackbar.BottomNotifierMessage
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.onboarding.presentation.oauth.viewmodel.ErrorHandled
import dev.zitech.onboarding.presentation.oauth.viewmodel.Idle
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigateBack
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigateToDashboard
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigateToError
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigateToFirefly
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigatedToFireflyResult
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigationHandled
import dev.zitech.onboarding.presentation.oauth.viewmodel.OauthViewModel
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnBackClick
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnClientIdChange
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnClientSecretChange
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnLoginClick
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnServerAddressChange
import dev.zitech.onboarding.presentation.oauth.viewmodel.ShowError
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@Composable
internal fun OauthRoute(
    navigateToDashboard: () -> Unit,
    navigateBack: () -> Unit,
    navigateToError: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OauthViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val snackbarState = rememberSnackbarState()
    val backgroundColorResource = FireFlowTheme.colors.background.toArgb()
    val context = LocalContext.current
    val coroutineScope = LocalLifecycleOwner.current.lifecycle.coroutineScope

    when (val event = screenState.event) {
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
        NavigateToError -> {
            navigateToError()
            viewModel.sendIntent(NavigationHandled)
        }
        is NavigateToFirefly -> {
            LaunchedEffect(Unit) {
                Browser.openUrl(
                    context,
                    event.url,
                    backgroundColorResource
                ).onEach { event ->
                    viewModel.sendIntent(NavigatedToFireflyResult(event))
                }.stateIn(coroutineScope)
            }
        }
        is ShowError -> {
            snackbarState.showMessage(
                BottomNotifierMessage(
                    text = event.message,
                    state = BottomNotifierMessage.State.ERROR,
                    duration = BottomNotifierMessage.Duration.SHORT
                )
            )
            viewModel.sendIntent(ErrorHandled)
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
