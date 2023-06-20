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

package dev.zitech.fireflow.authentication.presentation.accounts.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.fireflow.authentication.R
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.AccountsViewModel
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.BackClicked
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.CloseHandled
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.ConfirmRemoveAccountClicked
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.ConfirmRemoveAccountDismissed
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.FatalErrorHandled
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.HomeHandled
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.MoreClicked
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.MoreDismissed
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.MoreItemClicked
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.NonFatalErrorHandled
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.QuitHandled
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.ds.molecules.dialog.FireFlowDialogs
import dev.zitech.fireflow.ds.molecules.snackbar.BottomNotifierMessage
import dev.zitech.fireflow.ds.molecules.snackbar.rememberSnackbarState

@Composable
internal fun AccountsRoute(
    isBackNavigationSupported: Boolean,
    navigateToHome: () -> Unit,
    navigateOutOfApp: () -> Unit,
    navigateBack: () -> Unit,
    navigateToError: (error: Error) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val screenState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = rememberSnackbarState()

    if (screenState.home) {
        navigateToHome()
        viewModel.receiveIntent(HomeHandled)
    }

    if (screenState.quit) {
        navigateOutOfApp()
        viewModel.receiveIntent(QuitHandled)
    }

    if (screenState.close) {
        navigateBack()
        viewModel.receiveIntent(CloseHandled)
    }

    screenState.confirmRemoveAccount?.let {
        FireFlowDialogs.Alert(
            title = stringResource(R.string.accounts_dialog_remove_account_title),
            text = stringResource(R.string.accounts_dialog_remove_account_text, it.identification),
            onConfirmButtonClick = {
                viewModel.receiveIntent(ConfirmRemoveAccountClicked(userId = it.userId))
            },
            onDismissRequest = {
                viewModel.receiveIntent(ConfirmRemoveAccountDismissed)
            }
        )
    }

    screenState.fatalError?.let { fireFlowError ->
        navigateToError(fireFlowError)
        viewModel.receiveIntent(FatalErrorHandled)
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

    AccountsScreen(
        isBackNavigationSupported = isBackNavigationSupported,
        accountsState = screenState,
        backClicked = { viewModel.receiveIntent(BackClicked(it)) },
        modifier = modifier,
        snackbarState = snackbarState,
        onMoreItemClicked = { identification, menuItemId, userId ->
            viewModel.receiveIntent(
                MoreItemClicked(
                    identification = identification,
                    menuItemId = menuItemId,
                    userId = userId
                )
            )
        },
        onMoreDismissed = { index -> viewModel.receiveIntent(MoreDismissed(index)) },
        onMoreClicked = { index -> viewModel.receiveIntent(MoreClicked(index)) }
    )
}
