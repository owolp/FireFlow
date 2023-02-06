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
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.domain.model.exception.NoBrowserInstalledException
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.onboarding.domain.usecase.IsOauthLoginInputValidUseCase
import dev.zitech.onboarding.domain.validator.ClientIdValidator
import dev.zitech.onboarding.presentation.oauth.viewmodel.resource.OauthStringsProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class OauthViewModel @Inject constructor(
    private val stateHandler: OauthStateHandler,
    private val isOauthLoginInputValidUseCase: IsOauthLoginInputValidUseCase,
    private val clientIdValidator: ClientIdValidator,
    private val oauthStringsProvider: OauthStringsProvider
) : ViewModel(), MviViewModel<OauthIntent, OauthState> {

    private val tag = Logger.tag(this::class.java)

    override val screenState: StateFlow<OauthState> = stateHandler.state

    override fun sendIntent(intent: OauthIntent) {
        viewModelScope.launch {
            when (intent) {
                OnLoginClick -> handleOnLoginClick()
                NavigationHandled -> stateHandler.resetEvent()
                OnBackClick -> stateHandler.setEvent(NavigateBack)
                is OnClientIdChange -> handleOnClientIdChange(intent)
                is OnClientSecretChange -> {
                    stateHandler.setClientSecret(intent.clientSecret.trim())
                    setLoginEnabled()
                }
                is OnServerAddressChange -> {
                    stateHandler.setServerAddress(intent.serverAddress.trim())
                    setLoginEnabled()
                }
                is NavigatedToFireflyResult -> handleNavigatedToFireflyResult(intent.dataResult)
                ErrorHandled -> stateHandler.resetEvent()
                is OnOauthCode -> {
                    // TODO: Use secret + authcode to generate token
                }
            }
        }
    }

    private fun handleOnLoginClick() {
        stateHandler.setEvent(
            NavigateToFirefly(
                oauthStringsProvider.getNewAccessTokenUrl(
                    screenState.value.serverAddress,
                    screenState.value.clientId
                )
            )
        )
    }

    private fun handleOnClientIdChange(intent: OnClientIdChange) {
        with(intent.clientId.trim()) {
            if (clientIdValidator(this) || this.isEmpty()) {
                stateHandler.setClientId(this)
                setLoginEnabled()
            }
        }
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

    private fun handleNavigatedToFireflyResult(dataResult: DataResult<Unit>) {
        when (dataResult) {
            is DataResult.Success -> stateHandler.resetEvent()
            is DataResult.Error -> {
                when (dataResult.cause) {
                    is NoBrowserInstalledException -> {
                        stateHandler.setEvent(
                            ShowError(oauthStringsProvider.getNoSupportedBrowserInstalled())
                        )
                    }
                    else -> {
                        Logger.e(tag, exception = dataResult.cause)
                        stateHandler.setEvent(NavigateToError)
                    }
                }
            }
        }
    }
}
