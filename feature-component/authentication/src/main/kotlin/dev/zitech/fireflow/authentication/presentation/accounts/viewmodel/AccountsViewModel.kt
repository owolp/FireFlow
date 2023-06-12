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

package dev.zitech.fireflow.authentication.presentation.accounts.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.fireflow.authentication.presentation.accounts.model.AccountItem
import dev.zitech.fireflow.authentication.presentation.accounts.model.AccountItem.MenuItem
import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.common.domain.usecase.user.GetUsersUseCase
import dev.zitech.fireflow.common.presentation.architecture.MviViewModel
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.onFailure
import dev.zitech.fireflow.core.result.onSuccess
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
internal class AccountsViewModel @Inject constructor(
    getUsersUseCase: GetUsersUseCase
) : MviViewModel<AccountsIntent, AccountsState>(AccountsState()) {

    init {
        observeUsers(getUsersUseCase)
    }

    override fun receiveIntent(intent: AccountsIntent) {
        viewModelScope.launch {
            when (intent) {
                SwitchToAccountClicked -> updateState { copy(home = true) }
                HomeHandled -> updateState { copy(home = false) }
                is BackClicked -> handleBackClicked(intent.isBackNavigationSupported)
                CloseHandled -> updateState { copy(close = false) }
                QuitHandled -> updateState { copy(quit = false) }
                FatalErrorHandled -> updateState { copy(fatalError = null) }
                NonFatalErrorHandled -> updateState { copy(nonFatalError = null) }
                is MoreClicked -> handleMoreClicked(intent)
                is MoreDismissed -> handleMoreDismissed(intent)
                is MoreItemClicked -> {
//                    viewModel.receiveIntent(SwitchToAccountClicked)
                }
            }
        }
    }

    private fun getAccountItems(users: List<User>): List<AccountItem> =
        users.map { user ->
            AccountItem(
                menuItems = if (user.isCurrentUser) {
                    listOf(MenuItem.REMOVE_ACCOUNT)
                } else {
                    listOf(MenuItem.SWITCH_TO_ACCOUNT, MenuItem.REMOVE_ACCOUNT)
                },
                more = false,
                user = user
            )
        }

    private fun handleBackClicked(isBackNavigationSupported: Boolean) {
        if (isBackNavigationSupported) {
            updateState { copy(close = true) }
        } else {
            updateState { copy(quit = true) }
        }
    }

    private fun handleMoreClicked(intent: MoreClicked) {
        updateState {
            copy(
                accounts = accounts.map { accountItem ->
                    if (accountItem.user.id == intent.userId) {
                        accountItem.copy(more = true)
                    } else {
                        accountItem.copy(more = false)
                    }
                }
            )
        }
    }

    private fun handleMoreDismissed(intent: MoreDismissed) {
        updateState {
            copy(
                accounts = accounts.map { accountItem ->
                    if (accountItem.user.id == intent.userId) {
                        accountItem.copy(more = false)
                    } else {
                        accountItem
                    }
                }
            )
        }
    }

    private fun observeUsers(getUsersUseCase: GetUsersUseCase) {
        updateState { copy(loading = true) }
        getUsersUseCase().onEach { usersResult ->
            usersResult.onSuccess { users ->
                updateState {
                    copy(
                        loading = false,
                        accounts = getAccountItems(users)
                    )
                }
            }.onFailure { error ->
                when (error) {
                    is Error.UserVisible -> updateState {
                        copy(
                            loading = false,
                            nonFatalError = error
                        )
                    }
                    else -> updateState { copy(loading = false, fatalError = error) }
                }
            }
        }.launchIn(viewModelScope)
    }
}
