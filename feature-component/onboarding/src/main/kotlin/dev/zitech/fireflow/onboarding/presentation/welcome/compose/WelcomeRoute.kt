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

package dev.zitech.fireflow.onboarding.presentation.welcome.compose

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
import dev.zitech.fireflow.common.presentation.browser.Browser
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.ds.molecules.snackbar.BottomNotifierMessage
import dev.zitech.fireflow.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.fireflow.onboarding.R
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.BackClicked
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.CloseHandled
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.ContinueWithOauthClicked
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.ContinueWithPatClicked
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.FatalErrorHandled
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.FireflyClicked
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.GetStartedClicked
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.NavigatedToFireflyResult
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.NextHandled
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.NonFatalErrorHandled
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.OAuthHandled
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.PatHandled
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.QuitAppHandled
import dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel.WelcomeViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@Composable
internal fun WelcomeRoute(
    isBackNavigationSupported: Boolean,
    navigateToOAuth: () -> Unit,
    navigateToPat: () -> Unit,
    navigateToNext: () -> Unit,
    navigateOutOfApp: () -> Unit,
    navigateToError: (error: Error) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    val screenState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = rememberSnackbarState()
    val context = LocalContext.current
    val coroutineScope = LocalLifecycleOwner.current.lifecycle.coroutineScope

    if (screenState.next) {
        navigateToNext()
        viewModel.receiveIntent(NextHandled)
    }

    screenState.fatalError?.let { fireFlowError ->
        navigateToError(fireFlowError)
        viewModel.receiveIntent(FatalErrorHandled)
    }

    if (screenState.fireflyAuthentication) {
        val fireFlyHomePageUrl = stringResource(R.string.firefly_iii_home_page_url)
        LaunchedEffect(Unit) {
            Browser.openUrl(
                context,
                fireFlyHomePageUrl
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

    if (screenState.oauth) {
        navigateToOAuth()
        viewModel.receiveIntent(OAuthHandled)
    }

    if (screenState.pat) {
        navigateToPat()
        viewModel.receiveIntent(PatHandled)
    }

    if (screenState.quitApp) {
        navigateOutOfApp()
        viewModel.receiveIntent(QuitAppHandled)
    }

    if (screenState.close) {
        navigateBack()
        viewModel.receiveIntent(CloseHandled)
    }

    WelcomeScreen(
        welcomeState = screenState,
        isBackNavigationSupported = isBackNavigationSupported,
        modifier = modifier,
        snackbarState = snackbarState,
        continueWithOauthClicked = { viewModel.receiveIntent(ContinueWithOauthClicked) },
        continueWithPatClicked = { viewModel.receiveIntent(ContinueWithPatClicked) },
        getStartedClicked = { viewModel.receiveIntent(GetStartedClicked) },
        backClicked = { viewModel.receiveIntent(BackClicked(it)) },
        fireflyClicked = { viewModel.receiveIntent(FireflyClicked) }
    )
}
