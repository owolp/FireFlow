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
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.dispatcher.AppDispatchers
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.domain.model.exception.NoBrowserInstalledException
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.model.database.UserAccount.Companion.STATE_LENGTH
import dev.zitech.core.persistence.domain.model.exception.NullUserAccountException
import dev.zitech.core.persistence.domain.usecase.database.GetUserAccountByStateUseCase
import dev.zitech.core.persistence.domain.usecase.database.SaveUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.UpdateUserAccountUseCase
import dev.zitech.onboarding.domain.model.Token
import dev.zitech.onboarding.domain.usecase.GetTokenUseCase
import dev.zitech.onboarding.domain.usecase.IsOAuthLoginInputValidUseCase
import dev.zitech.onboarding.domain.validator.ClientIdValidator
import dev.zitech.onboarding.presentation.oauth.model.OAuthAuthentication
import dev.zitech.onboarding.presentation.oauth.viewmodel.resource.OAuthStringsProvider
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
internal class OAuthViewModel @Inject constructor(
    private val stateHandler: OAuthStateHandler,
    private val isOauthLoginInputValidUseCase: IsOAuthLoginInputValidUseCase,
    private val saveUserAccountUseCase: SaveUserAccountUseCase,
    private val updateUserAccountUseCase: UpdateUserAccountUseCase,
    private val getUserAccountByStateUseCase: GetUserAccountByStateUseCase,
    private val getTokenUseCase: dagger.Lazy<GetTokenUseCase>,
    private val clientIdValidator: ClientIdValidator,
    private val oauthStringsProvider: OAuthStringsProvider,
    private val appDispatchers: AppDispatchers
) : ViewModel(), MviViewModel<OAuthIntent, OAuthState> {

    private companion object {
        const val LOADING_DELAY_IN_MILLISECONDS = 500L
    }

    private val tag = Logger.tag(this::class.java)

    override val screenState: StateFlow<OAuthState> = stateHandler.state

    override fun sendIntent(intent: OAuthIntent) {
        viewModelScope.launch {
            when (intent) {
                OnLoginClick -> handleOnLoginClick()
                NavigationHandled -> stateHandler.resetEvent()
                OnBackClick -> stateHandler.setEvent(NavigateBack)
                is OnClientIdChange -> handleOnClientIdChange(intent)
                is OnClientSecretChange -> {
                    stateHandler.setClientSecret(intent.clientSecret.trim())
                    setLoginEnabledOrDisabled()
                }
                is OnServerAddressChange -> {
                    stateHandler.setServerAddress(intent.serverAddress.trim())
                    setLoginEnabledOrDisabled()
                }
                is NavigatedToFireflyResult -> handleNavigatedToFireflyResult(intent.dataResult)
                ErrorHandled -> stateHandler.resetEvent()
                is OnOauthCode -> handleOnOauthCode(intent.authentication)
                OnAuthenticationCanceled -> stateHandler.setLoading(false)
            }
        }
    }

    private suspend fun handleOnLoginClick() {
        val clientId = screenState.value.clientId
        val clientSecret = screenState.value.clientSecret
        val serverAddress = screenState.value.serverAddress
        val state = DataFactory.createRandomString(STATE_LENGTH)

        withContext(appDispatchers.io) {
            delay(LOADING_DELAY_IN_MILLISECONDS)
            stateHandler.setLoading(true)
        }
        when (
            val result = saveUserAccountUseCase(
                clientId = clientId,
                clientSecret = clientSecret,
                isCurrentUserAccount = false,
                serverAddress = serverAddress,
                state = state
            )
        ) {
            is DataResult.Success -> {
                with(stateHandler) {
                    setEvent(
                        NavigateToFirefly(
                            oauthStringsProvider.getNewAccessTokenUrl(
                                serverAddress,
                                clientId,
                                state
                            )
                        )
                    )
                    setLoading(true)
                }
            }
            is DataResult.Error -> {
                Logger.e(tag, exception = result.cause)
                stateHandler.setLoading(false)
                stateHandler.setEvent(NavigateToError)
            }
        }
    }

    private fun handleOnClientIdChange(intent: OnClientIdChange) {
        with(intent.clientId.trim()) {
            if (clientIdValidator(this) || this.isEmpty()) {
                stateHandler.setClientId(this)
                setLoginEnabledOrDisabled()
            }
        }
    }

    private fun setLoginEnabledOrDisabled() {
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

    private fun handleNavigatedToFireflyResult(result: DataResult<Unit>) {
        when (result) {
            is DataResult.Success -> stateHandler.resetEvent()
            is DataResult.Error -> {
                stateHandler.setLoading(false)
                when (result.cause) {
                    is NoBrowserInstalledException -> {
                        stateHandler.setEvent(
                            ShowError(oauthStringsProvider.getNoSupportedBrowserInstalled())
                        )
                    }
                    else -> {
                        Logger.e(tag, exception = result.cause)
                        stateHandler.setEvent(NavigateToError)
                    }
                }
            }
        }
    }

    private suspend fun handleOnOauthCode(authentication: OAuthAuthentication) {
        val code = authentication.code
        val state = authentication.state
        if (code != null && state != null) {
            handleScreenOpenedFromDeepLink(state, code)
        }
    }

    private suspend fun handleScreenOpenedFromDeepLink(state: String, code: String) {
        stateHandler.setLoading(true)
        when (val result = getUserAccountByStateUseCase(state)) {
            is DataResult.Success -> {
                val userAccount = result.value
                with(stateHandler) {
                    setServerAddress(userAccount.serverAddress)
                    setClientId(userAccount.clientId)
                    setClientSecret(userAccount.clientSecret)
                }
                retrieveToken(userAccount, code)
            }
            is DataResult.Error -> {
                stateHandler.setLoading(false)
                if (result.cause is NullUserAccountException) {
                    stateHandler.setEvent(
                        ShowError(oauthStringsProvider.getCodeStateError())
                    )
                } else {
                    Logger.e(tag, exception = result.cause)
                    stateHandler.setEvent(NavigateToError)
                }
            }
        }
    }

    private suspend fun retrieveToken(userAccount: UserAccount, code: String) {
        when (
            val result = getTokenUseCase.get()(
                clientId = userAccount.clientId,
                clientSecret = userAccount.clientSecret,
                code = code
            )
        ) {
            is DataResult.Success -> updateUserAccount(userAccount, result.value, code)
            is DataResult.Error -> {
                stateHandler.setLoading(false)
                stateHandler.setEvent(
                    ShowError(oauthStringsProvider.getTokenError())
                )
            }
        }
    }

    private suspend fun updateUserAccount(
        userAccount: UserAccount,
        token: Token,
        code: String
    ) {
        when (
            val result = updateUserAccountUseCase(
                userAccount.copy(
                    accessToken = token.accessToken,
                    isCurrentUserAccount = true,
                    oauthCode = code,
                    refreshToken = token.refreshToken,
                    state = null
                )
            )
        ) {
            is DataResult.Success -> {
                stateHandler.setLoading(false)
                stateHandler.setEvent(NavigateToDashboard)
            }
            is DataResult.Error -> {
                Logger.e(tag, exception = result.cause)
                stateHandler.setLoading(false)
                stateHandler.setEvent(NavigateToError)
            }
        }
    }
}