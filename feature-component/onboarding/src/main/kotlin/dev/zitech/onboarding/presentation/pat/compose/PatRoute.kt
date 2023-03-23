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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.core.common.domain.error.Error
import dev.zitech.ds.molecules.snackbar.BottomNotifierMessage
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.onboarding.presentation.pat.viewmodel.ErrorHandled
import dev.zitech.onboarding.presentation.pat.viewmodel.Idle
import dev.zitech.onboarding.presentation.pat.viewmodel.NavigateBack
import dev.zitech.onboarding.presentation.pat.viewmodel.NavigateToDashboard
import dev.zitech.onboarding.presentation.pat.viewmodel.NavigateToError
import dev.zitech.onboarding.presentation.pat.viewmodel.NavigationHandled
import dev.zitech.onboarding.presentation.pat.viewmodel.OnBackClick
import dev.zitech.onboarding.presentation.pat.viewmodel.OnLoginClick
import dev.zitech.onboarding.presentation.pat.viewmodel.OnPersonalAccessTokenChange
import dev.zitech.onboarding.presentation.pat.viewmodel.OnServerAddressChange
import dev.zitech.onboarding.presentation.pat.viewmodel.PatViewModel
import dev.zitech.onboarding.presentation.pat.viewmodel.ShowError

@Composable
internal fun PatRoute(
    navigateToDashboard: () -> Unit,
    navigateBack: () -> Unit,
    navigateToError: (error: Error) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PatViewModel = hiltViewModel()
) {
    val screenState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = rememberSnackbarState()

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
    PatScreen(
        modifier = modifier,
        patState = screenState,
        snackbarState = snackbarState,
        onLoginClick = { viewModel.receiveIntent(OnLoginClick) },
        onBackClick = { viewModel.receiveIntent(OnBackClick) },
        onServerAddressChange = { viewModel.receiveIntent(OnServerAddressChange(it)) },
        onPersonalAccessTokenChange = { viewModel.receiveIntent(OnPersonalAccessTokenChange(it)) }
    )
}
