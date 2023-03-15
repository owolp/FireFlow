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
import dev.zitech.authenticator.domain.model.Token
import dev.zitech.authenticator.domain.usecase.GetAccessTokenUseCase
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.dispatcher.AppDispatchers
import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.Work
import dev.zitech.core.common.domain.model.onError
import dev.zitech.core.common.domain.model.onSuccess
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.model.database.UserAccount.Companion.STATE_LENGTH
import dev.zitech.core.persistence.domain.usecase.database.GetUserAccountByStateUseCase
import dev.zitech.core.persistence.domain.usecase.database.SaveUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.UpdateUserAccountUseCase
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
    private val getAccessTokenUseCase: dagger.Lazy<GetAccessTokenUseCase>,
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
                is NavigatedToFireflyResult -> handleNavigatedToFireflyResult(intent.result)
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
        saveUserAccountUseCase(
            clientId = clientId,
            clientSecret = clientSecret,
            isCurrentUserAccount = false,
            serverAddress = serverAddress,
            state = state
        ).onSuccess {
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
        }.onError { error ->
            stateHandler.setLoading(false)
            when (error) {
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    stateHandler.setEvent(NavigateToError(error))
                }
                is Error.UserVisible ->
                    stateHandler.setEvent(ShowError(text = error.text))
                else -> Logger.e(tag, error.text)
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

    private suspend fun handleNavigatedToFireflyResult(result: Work<Unit>) {
        result.onSuccess {
            stateHandler.resetEvent()
        }.onError { error ->
            stateHandler.setLoading(false)
            when (error) {
                is Error.NoBrowserInstalled -> {
                    stateHandler.setEvent(
                        ShowError(messageResId = error.uiResId)
                    )
                }
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    stateHandler.setEvent(NavigateToError(error))
                }
                is Error.UserVisible ->
                    stateHandler.setEvent(ShowError(text = error.text))
                else -> {
                    Logger.e(tag, error.text)
                    stateHandler.setEvent(NavigateToError(error))
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
        getUserAccountByStateUseCase(state)
            .onSuccess { userAccount ->
                with(stateHandler) {
                    setServerAddress(userAccount.serverAddress)
                    val authenticationType = userAccount.authenticationType
                    if (authenticationType is UserAccount.AuthenticationType.OAuth) {
                        val clientId = authenticationType.clientId
                        setClientId(clientId)
                        val clientSecret = authenticationType.clientSecret
                        setClientSecret(clientSecret)
                        retrieveToken(userAccount, clientId, clientSecret, code)
                    } else {
                        stateHandler.setLoading(false)
                        stateHandler.setEvent(
                            NavigateToError(Error.AuthenticationProblem)
                        )
                    }
                }
            }.onError { error ->
                stateHandler.setLoading(false)
                when (error) {
                    is Error.NullUserAccountByState -> {
                        Logger.e(tag, error.text)
                        stateHandler.setEvent(NavigateToError(error))
                    }
                    is Error.Fatal -> {
                        Logger.e(tag, throwable = error.throwable)
                        stateHandler.setEvent(NavigateToError(error))
                    }
                    else -> {
                        Logger.e(tag, error.text)
                        stateHandler.setEvent(ShowError(messageResId = error.uiResId))
                    }
                }
            }
    }

    private suspend fun retrieveToken(
        userAccount: UserAccount,
        clientId: String,
        clientSecret: String,
        code: String
    ) {
        getAccessTokenUseCase.get()(
            clientId = clientId,
            clientSecret = clientSecret,
            code = code
        ).onSuccess { token ->
            updateUserAccount(userAccount, clientId, clientSecret, token, code)
        }.onError { error ->
            stateHandler.setLoading(false)
            when (error) {
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    stateHandler.setEvent(NavigateToError(error))
                }
                is Error.UserVisible ->
                    stateHandler.setEvent(ShowError(text = error.text))
                else -> Logger.e(tag, error.text)
            }
        }
    }

    private suspend fun updateUserAccount(
        userAccount: UserAccount,
        clientId: String,
        clientSecret: String,
        token: Token,
        oauthCode: String
    ) {
        updateUserAccountUseCase(
            userAccount.copy(
                authenticationType = UserAccount.AuthenticationType.OAuth(
                    accessToken = token.accessToken,
                    clientId = clientId,
                    clientSecret = clientSecret,
                    oauthCode = oauthCode,
                    refreshToken = token.refreshToken
                ),
                isCurrentUserAccount = true,
                state = null
            )
        ).onSuccess {
            stateHandler.setLoading(false)
            stateHandler.setEvent(NavigateToDashboard)
        }.onError { error ->
            stateHandler.setLoading(false)
            when (error) {
                is Error.NullUserAccount -> {
                    Logger.e(tag, error.text)
                    stateHandler.setEvent(NavigateToError(error))
                }
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    stateHandler.setEvent(NavigateToError(error))
                }
                else -> {
                    Logger.e(tag, error.text)
                    stateHandler.setEvent(ShowError(messageResId = error.uiResId))
                }
            }
        }
    }
}
