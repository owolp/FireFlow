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

package dev.zitech.fireflow.onboarding.presentation.oauth.compose

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
import dev.zitech.ds.molecules.snackbar.BottomNotifierMessage
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.fireflow.common.presentation.browser.Browser
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.onboarding.R
import dev.zitech.fireflow.onboarding.presentation.oauth.model.OAuthAuthentication
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.AuthenticationCanceled
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.BackClicked
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.ClientIdChanged
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.ClientSecretChanged
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.FatalErrorHandled
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.LoginClicked
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.NavigatedToFireflyResult
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.NonFatalErrorHandled
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.OAuthViewModel
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.OauthCodeReceived
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.ServerAddressChanged
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.StepClosedHandled
import dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel.StepCompletedHandled
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@Composable
internal fun OAuthRoute(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToError: (error: Error) -> Unit,
    navigateToNext: () -> Unit,
    oauthAuthentication: OAuthAuthentication,
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
            viewModel.receiveIntent(AuthenticationCanceled)
        }
    }

    screenState.fatalError?.let { fireFlowError ->
        navigateToError(fireFlowError)
        viewModel.receiveIntent(FatalErrorHandled)
    }
    screenState.fireflyAuthentication?.let { state ->
        val url = stringResource(
            R.string.firefly_iii_new_access_token_url,
            screenState.serverAddress,
            screenState.clientId,
            state
        )
        LaunchedEffect(Unit) {
            Browser.openUrl(
                context,
                url,
                result
            ).onEach { event ->
                viewModel.receiveIntent(NavigatedToFireflyResult(event))
            }.stateIn(coroutineScope)
        }
    }
    screenState.nonFatalError?.let { fireFlowError ->
        snackbarState.showMessage(
            BottomNotifierMessage(
                text = stringResource(fireFlowError.uiResId),
                state = BottomNotifierMessage.State.ERROR,
                duration = BottomNotifierMessage.Duration.SHORT
            )
        )
        viewModel.receiveIntent(NonFatalErrorHandled)
    }
    if (screenState.stepClosed) {
        navigateBack()
        viewModel.receiveIntent(StepClosedHandled)
    }
    if (screenState.stepCompleted) {
        navigateToNext()
        viewModel.receiveIntent(StepCompletedHandled)
    }

    OAuthScreen(
        backClicked = { viewModel.receiveIntent(BackClicked) },
        clientIdChanged = { viewModel.receiveIntent(ClientIdChanged(it)) },
        clientSecretChanged = { viewModel.receiveIntent(ClientSecretChanged(it)) },
        loginClicked = { viewModel.receiveIntent(LoginClicked) },
        modifier = modifier,
        oauthState = screenState,
        serverAddressChanged = { viewModel.receiveIntent(ServerAddressChanged(it)) },
        snackbarState = snackbarState
    )

    LaunchedEffect(Unit) {
        viewModel.receiveIntent(OauthCodeReceived(oauthAuthentication))
    }
}
