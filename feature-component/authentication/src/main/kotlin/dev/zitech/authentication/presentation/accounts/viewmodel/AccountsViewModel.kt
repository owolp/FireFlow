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

package dev.zitech.authentication.presentation.accounts.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.presentation.architecture.MviViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
internal class AccountsViewModel @Inject constructor() :
    MviViewModel<AccountsIntent, AccountsState>(
        AccountsState()
    ) {

    override fun receiveIntent(intent: AccountsIntent) {
        viewModelScope.launch {
            when (intent) {
                LoginClicked -> updateState { copy(home = true) }
                HomeHandled -> updateState { copy(home = false) }
            }
        }
    }
}
