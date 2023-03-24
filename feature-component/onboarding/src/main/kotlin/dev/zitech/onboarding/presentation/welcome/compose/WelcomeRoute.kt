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
import dev.zitech.onboarding.R
import dev.zitech.onboarding.presentation.welcome.viewmodel.BackClicked
import dev.zitech.onboarding.presentation.welcome.viewmodel.ContinueWithOauthClicked
import dev.zitech.onboarding.presentation.welcome.viewmodel.ContinueWithPatClicked
import dev.zitech.onboarding.presentation.welcome.viewmodel.DemoHandled
import dev.zitech.onboarding.presentation.welcome.viewmodel.DemoPositiveClicked
import dev.zitech.onboarding.presentation.welcome.viewmodel.DemoWarningDismissed
import dev.zitech.onboarding.presentation.welcome.viewmodel.FatalErrorHandled
import dev.zitech.onboarding.presentation.welcome.viewmodel.FireflyClicked
import dev.zitech.onboarding.presentation.welcome.viewmodel.GetStartedClicked
import dev.zitech.onboarding.presentation.welcome.viewmodel.NavigatedToFireflyResult
import dev.zitech.onboarding.presentation.welcome.viewmodel.NotFatalErrorHandled
import dev.zitech.onboarding.presentation.welcome.viewmodel.OAuthHandled
import dev.zitech.onboarding.presentation.welcome.viewmodel.PatHandled
import dev.zitech.onboarding.presentation.welcome.viewmodel.QuitAppHandled
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
    val screenState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = rememberSnackbarState()
    val context = LocalContext.current
    val coroutineScope = LocalLifecycleOwner.current.lifecycle.coroutineScope

    if (screenState.demo) {
        navigateToDemo()
        viewModel.receiveIntent(DemoHandled)
    }
    if (screenState.demoWarning) {
        FireFlowDialogs.Alert(
            text = stringResource(R.string.welcome_demo_dialog_text),
            confirmButton = stringResource(R.string.welcome_demo_dialog_confirm),
            onConfirmButtonClick = { viewModel.receiveIntent(DemoPositiveClicked) },
            onDismissRequest = { viewModel.receiveIntent(DemoWarningDismissed) }
        )
    }
    screenState.fatalError?.let { fireFlowError ->
        navigateToError(fireFlowError)
        viewModel.receiveIntent(FatalErrorHandled)
    }
    if (screenState.firefly) {
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
        viewModel.receiveIntent(NotFatalErrorHandled)
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

    WelcomeScreen(
        modifier = modifier,
        snackbarState = snackbarState,
        continueWithOauthClicked = { viewModel.receiveIntent(ContinueWithOauthClicked) },
        continueWithPatClicked = { viewModel.receiveIntent(ContinueWithPatClicked) },
        getStartedClicked = { viewModel.receiveIntent(GetStartedClicked) },
        backClicked = { viewModel.receiveIntent(BackClicked) },
        fireflyClicked = { viewModel.receiveIntent(FireflyClicked) }
    )
}
