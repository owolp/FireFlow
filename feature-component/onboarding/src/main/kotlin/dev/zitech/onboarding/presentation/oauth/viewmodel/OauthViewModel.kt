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

package dev.zitech.onboarding.presentation.oauth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.persistence.domain.usecase.database.SaveUserAccountUseCase
import dev.zitech.onboarding.domain.usecase.IsOauthLoginInputValidUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class OauthViewModel @Inject constructor(
    private val stateHandler: OauthStateHandler,
    private val saveUserAccountUseCase: SaveUserAccountUseCase,
    private val isOauthLoginInputValidUseCase: IsOauthLoginInputValidUseCase
) : ViewModel(), MviViewModel<OauthIntent, OauthState> {

    override val screenState: StateFlow<OauthState> = stateHandler.state

    override fun sendIntent(intent: OauthIntent) {
        viewModelScope.launch {
            when (intent) {
                OnLoginClick -> handleOnLoginClick()
                NavigationHandled -> stateHandler.resetEvent()
                OnBackClick -> stateHandler.setEvent(NavigateBack)
                is OnClientIdChange -> {
                    stateHandler.setClientId(intent.clientId.trim())
                    setLoginEnabled()
                }
                is OnClientSecretChange -> {
                    stateHandler.setClientSecret(intent.clientSecret.trim())
                    setLoginEnabled()
                }
                is OnServerAddressChange -> {
                    stateHandler.setServerAddress(intent.serverAddress.trim())
                    setLoginEnabled()
                }
            }
        }
    }

    @Suppress("ForbiddenComment")
    private suspend fun handleOnLoginClick() {
        // TODO: Dev usage
        saveUserAccountUseCase(true)
        stateHandler.setEvent(NavigateToDashboard)
    }

    private fun setLoginEnabled() {
        stateHandler.setLoginEnabled(
            with(screenState.value) {
                isOauthLoginInputValidUseCase(
                    clientId = clientId,
                    clientSecret = clientSecret,
                    serverAddress = serverAddress
                )
            }
        )
    }
}
