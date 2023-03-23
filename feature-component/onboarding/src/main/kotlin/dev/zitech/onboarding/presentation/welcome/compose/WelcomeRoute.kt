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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.coroutineScope
import dev.zitech.core.common.domain.error.Error
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
    navigateToError: (error: Error) -> Unit,
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
            viewModel.receiveIntent(NavigationHandled)
        }
        NavigateToPat -> {
            navigateToPat()
            viewModel.receiveIntent(NavigationHandled)
        }
        NavigateToDemo -> {
            navigateToDemo()
            viewModel.receiveIntent(NavigationHandled)
        }
        NavigateOutOfApp -> {
            navigateOutOfApp()
            viewModel.receiveIntent(NavigationHandled)
        }
        is NavigateToError -> {
            navigateToError(event.error)
            viewModel.receiveIntent(NavigationHandled)
        }
        is NavigateToFirefly -> {
            LaunchedEffect(Unit) {
                Browser.openUrl(
                    context,
                    event.url
                ).onEach { event ->
                    viewModel.receiveIntent(NavigatedToFireflyResult(event))
                }.stateIn(coroutineScope)
            }
        }
        is ShowDemoWarning -> {
            FireFlowDialogs.Alert(
                text = event.text,
                confirmButton = event.confirm,
                onConfirmButtonClick = { viewModel.receiveIntent(OnShowDemoPositive) },
                onDismissRequest = { viewModel.receiveIntent(OnShowDemoDismiss) }
            )
        }
        is ShowError -> {
            snackbarState.showMessage(
                BottomNotifierMessage(
                    text = event.messageResId?.let { stringResource(it) }
                        ?: event.text.orEmpty(),
                    state = BottomNotifierMessage.State.ERROR,
                    duration = BottomNotifierMessage.Duration.SHORT
                )
            )
            viewModel.receiveIntent(ErrorHandled)
        }
        Idle -> {
            // NO_OP
        }
    }

    WelcomeScreen(
        modifier = modifier,
        snackbarState = snackbarState,
        onContinueWithOauthClick = { viewModel.receiveIntent(OnContinueWithOauthClick) },
        onContinueWithPatClick = { viewModel.receiveIntent(OnContinueWithPatClick) },
        onGetStartedClick = { viewModel.receiveIntent(OnGetStartedClick) },
        onBackClick = { viewModel.receiveIntent(OnBackClick) },
        onFireflyClick = { viewModel.receiveIntent(OnFireflyClick) }
    )
}
