/*
 * Copyright (C) 2022 Zitech Ltd.
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

package dev.zitech.onboarding.presentation.login.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.persistence.domain.usecase.database.SaveUserAccountUseCase
import dev.zitech.onboarding.presentation.login.model.LoginType
import dev.zitech.onboarding.presentation.navigation.LoginDestination
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val stateHandler: LoginStateHandler,
    private val savedStateHandle: SavedStateHandle,
    private val saveUserAccountUseCase: SaveUserAccountUseCase
) : ViewModel(), MviViewModel<LoginIntent, LoginState> {

    override val screenState: StateFlow<LoginState> = stateHandler.state

    init {
        getLoginType()
    }

    override fun sendIntent(intent: LoginIntent) {
        viewModelScope.launch {
            when (intent) {
                OnLoginClick -> handleOnLoginClick()
                NavigationHandled -> stateHandler.resetEvent()
                OnBackClick -> stateHandler.setEvent(NavigateBack)
            }
        }
    }

    private fun getLoginType() {
        stateHandler.setLoginType(
            LoginType.valueOf(
                savedStateHandle.get<String>(LoginDestination.loginType)
                    ?: throw UnsupportedOperationException(
                        "Destination is missing key argument!"
                    )
            )
        )
    }

    @Suppress("ForbiddenComment")
    private suspend fun handleOnLoginClick() {
        // TODO: Dev usage
        saveUserAccountUseCase(true)
        stateHandler.setEvent(NavigateToDashboard)
    }
}
