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

package dev.zitech.onboarding.presentation.welcome.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.coroutineScope
import dev.zitech.core.common.framework.browser.Browser
import dev.zitech.ds.molecules.dialog.FireFlowDialogs
import dev.zitech.ds.molecules.snackbar.BottomNotifierMessage
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.onboarding.presentation.welcome.viewmodel.ErrorHandled
import dev.zitech.onboarding.presentation.welcome.viewmodel.Idle
import dev.zitech.onboarding.presentation.welcome.viewmodel.NavigateOutOfApp
import dev.zitech.onboarding.presentation.welcome.viewmodel.NavigateToDemo
import dev.zitech.onboarding.presentation.welcome.viewmodel.NavigateToError
import dev.zitech.onboarding.presentation.welcome.viewmodel.NavigateToFirefly
import dev.zitech.onboarding.presentation.welcome.viewmodel.NavigateToOAuth
import dev.zitech.onboarding.presentation.welcome.viewmodel.NavigateToPat
import dev.zitech.onboarding.presentation.welcome.viewmodel.NavigatedToFireflyResult
import dev.zitech.onboarding.presentation.welcome.viewmodel.NavigationHandled
import dev.zitech.onboarding.presentation.welcome.viewmodel.OnBackClick
import dev.zitech.onboarding.presentation.welcome.viewmodel.OnContinueWithOauthClick
import dev.zitech.onboarding.presentation.welcome.viewmodel.OnContinueWithPatClick
import dev.zitech.onboarding.presentation.welcome.viewmodel.OnFireflyClick
import dev.zitech.onboarding.presentation.welcome.viewmodel.OnGetStartedClick
import dev.zitech.onboarding.presentation.welcome.viewmodel.OnShowDemoDismiss
import dev.zitech.onboarding.presentation.welcome.viewmodel.OnShowDemoPositive
import dev.zitech.onboarding.presentation.welcome.viewmodel.ShowDemoWarning
import dev.zitech.onboarding.presentation.welcome.viewmodel.ShowError
import dev.zitech.onboarding.presentation.welcome.viewmodel.WelcomeViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@Composable
internal fun WelcomeRoute(
    navigateToOAuth: () -> Unit,
    navigateToPat: () -> Unit,
    navigateToDemo: () -> Unit,
    navigateOutOfApp: () -> Unit,
    navigateToError: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val snackbarState = rememberSnackbarState()
    val context = LocalContext.current
    val coroutineScope = LocalLifecycleOwner.current.lifecycle.coroutineScope

    when (val event = screenState.event) {
        NavigateToOAuth -> {
            navigateToOAuth()
            viewModel.sendIntent(NavigationHandled)
        }
        NavigateToPat -> {
            navigateToPat()
            viewModel.sendIntent(NavigationHandled)
        }
        NavigateToDemo -> {
            navigateToDemo()
            viewModel.sendIntent(NavigationHandled)
        }
        NavigateOutOfApp -> {
            navigateOutOfApp()
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
                    event.url
                ).onEach { event ->
                    viewModel.sendIntent(NavigatedToFireflyResult(event))
                }.stateIn(coroutineScope)
            }
        }
        is ShowDemoWarning -> {
            FireFlowDialogs.Alert(
                text = event.text,
                confirmButton = event.confirm,
                onConfirmButtonClick = { viewModel.sendIntent(OnShowDemoPositive) },
                onDismissRequest = { viewModel.sendIntent(OnShowDemoDismiss) }
            )
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

    WelcomeScreen(
        modifier = modifier,
        snackbarState = snackbarState,
        onContinueWithOauthClick = { viewModel.sendIntent(OnContinueWithOauthClick) },
        onContinueWithPatClick = { viewModel.sendIntent(OnContinueWithPatClick) },
        onGetStartedClick = { viewModel.sendIntent(OnGetStartedClick) },
        onBackClick = { viewModel.sendIntent(OnBackClick) },
        onFireflyClick = { viewModel.sendIntent(OnFireflyClick) }
    )
}
