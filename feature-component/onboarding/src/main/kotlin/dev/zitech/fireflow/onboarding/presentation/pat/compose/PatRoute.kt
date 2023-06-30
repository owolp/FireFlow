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

package dev.zitech.fireflow.onboarding.presentation.pat.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.ds.molecules.snackbar.BottomNotifierMessage
import dev.zitech.fireflow.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.fireflow.onboarding.presentation.pat.viewmodel.BackClicked
import dev.zitech.fireflow.onboarding.presentation.pat.viewmodel.FatalErrorHandled
import dev.zitech.fireflow.onboarding.presentation.pat.viewmodel.LoginClicked
import dev.zitech.fireflow.onboarding.presentation.pat.viewmodel.NonFatalErrorHandled
import dev.zitech.fireflow.onboarding.presentation.pat.viewmodel.PatViewModel
import dev.zitech.fireflow.onboarding.presentation.pat.viewmodel.PersonalAccessTokenChanged
import dev.zitech.fireflow.onboarding.presentation.pat.viewmodel.ServerAddressChanged
import dev.zitech.fireflow.onboarding.presentation.pat.viewmodel.StepClosedHandled
import dev.zitech.fireflow.onboarding.presentation.pat.viewmodel.StepCompletedHandled

@Composable
internal fun PatRoute(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToError: (error: Error) -> Unit,
    navigateToNext: () -> Unit,
    viewModel: PatViewModel = hiltViewModel()
) {
    val screenState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = rememberSnackbarState()

    screenState.fatalError?.let { fireFlowError ->
        navigateToError(fireFlowError)
        viewModel.receiveIntent(FatalErrorHandled)
    }
    screenState.nonFatalError?.let { fireFlowError ->
        val text = when (fireFlowError) {
            is Error.UserVisible -> fireFlowError.message.orEmpty()
            is Error.UserWithServerAddressAlreadyExists -> stringResource(
                fireFlowError.uiResId,
                fireFlowError.email,
                fireFlowError.serverAddress
            )
            else -> stringResource(fireFlowError.uiResId)
        }
        snackbarState.showMessage(
            BottomNotifierMessage(
                text = text,
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

    PatScreen(
        modifier = modifier,
        patState = screenState,
        snackbarState = snackbarState,
        loginClicked = { viewModel.receiveIntent(LoginClicked) },
        backClicked = { viewModel.receiveIntent(BackClicked) },
        serverAddressChanged = { viewModel.receiveIntent(ServerAddressChanged(it)) },
        personalAccessTokenChanged = { viewModel.receiveIntent(PersonalAccessTokenChanged(it)) }
    )
}
