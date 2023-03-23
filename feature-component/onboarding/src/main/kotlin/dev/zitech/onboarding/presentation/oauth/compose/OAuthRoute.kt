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

import android.app.Activity.RESULT_CANCELED
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import dev.zitech.ds.molecules.snackbar.BottomNotifierMessage
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.onboarding.presentation.oauth.model.OAuthAuthentication
import dev.zitech.onboarding.presentation.oauth.viewmodel.ErrorHandled
import dev.zitech.onboarding.presentation.oauth.viewmodel.Idle
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigateBack
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigateToDashboard
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigateToError
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigateToFirefly
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigatedToFireflyResult
import dev.zitech.onboarding.presentation.oauth.viewmodel.NavigationHandled
import dev.zitech.onboarding.presentation.oauth.viewmodel.OAuthViewModel
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnAuthenticationCanceled
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnBackClick
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnClientIdChange
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnClientSecretChange
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnLoginClick
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnOauthCode
import dev.zitech.onboarding.presentation.oauth.viewmodel.OnServerAddressChange
import dev.zitech.onboarding.presentation.oauth.viewmodel.ShowError
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@Composable
internal fun OAuthRoute(
    oauthAuthentication: OAuthAuthentication,
    navigateToDashboard: () -> Unit,
    navigateBack: () -> Unit,
    navigateToError: (error: Error) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OAuthViewModel = hiltViewModel()
) {
    val screenState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = rememberSnackbarState()
    val context = LocalContext.current
    val coroutineScope = LocalLifecycleOwner.current.lifecycle.coroutineScope

    val result = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_CANCELED) {
            viewModel.receiveIntent(OnAuthenticationCanceled)
        }
    }

    when (val event = screenState.event) {
        NavigateToDashboard -> {
            navigateToDashboard()
            viewModel.receiveIntent(NavigationHandled)
        }
        NavigateBack -> {
            navigateBack()
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
                    event.url,
                    result
                ).onEach { event ->
                    viewModel.receiveIntent(NavigatedToFireflyResult(event))
                }.stateIn(coroutineScope)
            }
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

    OAuthScreen(
        modifier = modifier,
        oauthState = screenState,
        snackbarState = snackbarState,
        onLoginClick = { viewModel.receiveIntent(OnLoginClick) },
        onBackClick = { viewModel.receiveIntent(OnBackClick) },
        onServerAddressChange = { viewModel.receiveIntent(OnServerAddressChange(it)) },
        onClientIdChange = { viewModel.receiveIntent(OnClientIdChange(it)) },
        onClientSecretChange = { viewModel.receiveIntent(OnClientSecretChange(it)) }
    )

    LaunchedEffect(Unit) {
        viewModel.receiveIntent(OnOauthCode(oauthAuthentication))
    }
}
