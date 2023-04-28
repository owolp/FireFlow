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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.AccountsViewModel
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.HomeHandled
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.LoginClicked

@Composable
internal fun AccountsRoute(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val screenState by viewModel.state.collectAsStateWithLifecycle()

    if (screenState.home) {
        navigateToHome()
        viewModel.receiveIntent(HomeHandled)
    }

    AccountsScreen(
        accountsState = screenState,
        loginClicked = { viewModel.receiveIntent(LoginClicked) },
        modifier = modifier
    )
}
