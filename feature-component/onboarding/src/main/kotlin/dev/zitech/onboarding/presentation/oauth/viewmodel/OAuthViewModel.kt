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
import dev.zitech.core.common.presentation.architecture.updateState
import dev.zitech.core.network.domain.usecase.GetFireflyProfileUseCase
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.model.database.UserAccount.Companion.STATE_LENGTH
import dev.zitech.core.persistence.domain.usecase.database.GetUserAccountByStateUseCase
import dev.zitech.core.persistence.domain.usecase.database.SaveUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.UpdateCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.UpdateCurrentUserAccountUseCase.Email
import dev.zitech.core.persistence.domain.usecase.database.UpdateCurrentUserAccountUseCase.FireflyId
import dev.zitech.core.persistence.domain.usecase.database.UpdateCurrentUserAccountUseCase.Role
import dev.zitech.core.persistence.domain.usecase.database.UpdateCurrentUserAccountUseCase.Type
import dev.zitech.core.persistence.domain.usecase.database.UpdateUserAccountUseCase
import dev.zitech.onboarding.domain.usecase.IsOAuthLoginInputValidUseCase
import dev.zitech.onboarding.domain.validator.ClientIdValidator
import dev.zitech.onboarding.presentation.oauth.model.OAuthAuthentication
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("TooManyFunctions")
@HiltViewModel
internal class OAuthViewModel @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val clientIdValidator: ClientIdValidator,
    private val getAccessTokenUseCase: dagger.Lazy<GetAccessTokenUseCase>,
    private val getFireflyProfileUseCase: dagger.Lazy<GetFireflyProfileUseCase>,
    private val getUserAccountByStateUseCase: GetUserAccountByStateUseCase,
    private val isOauthLoginInputValidUseCase: IsOAuthLoginInputValidUseCase,
    private val saveUserAccountUseCase: SaveUserAccountUseCase,
    private val updateCurrentUserAccountUseCase: UpdateCurrentUserAccountUseCase,
    private val updateUserAccountUseCase: UpdateUserAccountUseCase
) : ViewModel(), MviViewModel<OAuthIntent, OAuthState> {

    private val mutableState = MutableStateFlow(OAuthState())
    private val tag = Logger.tag(this::class.java)

    override val state: StateFlow<OAuthState> = mutableState.asStateFlow()

    override fun receiveIntent(intent: OAuthIntent) {
        viewModelScope.launch {
            when (intent) {
                AuthenticationCanceled -> mutableState.updateState { copy(loading = false) }
                BackClicked -> mutableState.updateState { copy(stepClosed = true) }
                is ClientIdChanged -> handleClientIdChanged(intent)
                is ClientSecretChanged -> handleClientSecretChanged(intent)
                FatalErrorHandled -> mutableState.updateState { copy(fatalError = null) }
                LoginClicked -> handleLoginClicked()
                is NavigatedToFireflyResult -> handleNavigatedToFireflyResult(intent.result)
                NonFatalErrorHandled -> mutableState.updateState { copy(nonFatalError = null) }
                is OauthCodeReceived -> handleOAuthCodeReceived(intent.authentication)
                is ServerAddressChanged -> handleServerAddressChanged(intent)
                StepClosedHandled -> mutableState.updateState { copy(stepClosed = false) }
                StepCompletedHandled -> mutableState.updateState { copy(stepCompleted = false) }
            }
        }
    }

    private fun handleClientIdChanged(intent: ClientIdChanged) {
        with(intent.clientId.trim()) {
            if (clientIdValidator(this) || this.isEmpty()) {
                mutableState.updateState { copy(clientId = this@with) }
                setLoginState()
            }
        }
    }

    private fun handleClientSecretChanged(intent: ClientSecretChanged) {
        mutableState.updateState {
            copy(
                clientSecret = intent.clientSecret.trim()
            )
        }
        setLoginState()
    }

    private suspend fun handleLoginClicked() {
        val clientId = state.value.clientId
        val clientSecret = state.value.clientSecret
        val serverAddress = state.value.serverAddress
        val state = DataFactory.createRandomString(STATE_LENGTH)

        withContext(appDispatchers.io) {
            delay(LOADING_DELAY_IN_MILLISECONDS)
            mutableState.updateState { copy(loading = true) }
        }
        saveUserAccountUseCase(
            clientId = clientId,
            clientSecret = clientSecret,
            isCurrentUserAccount = false,
            serverAddress = serverAddress,
            state = state
        ).onSuccess {
            mutableState.updateState {
                copy(
                    fireflyAuthentication = state,
                    loading = true
                )
            }
        }.onError { error ->
            mutableState.updateState { copy(loading = false) }
            when (error) {
                is Error.UserVisible -> {
                    mutableState.updateState { copy(nonFatalError = error) }
                }
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    mutableState.updateState { copy(fatalError = error) }
                }
                else -> {
                    Logger.e(tag, error.debugText)
                    mutableState.updateState { copy(fatalError = error) }
                }
            }
        }
    }

    private suspend fun handleNavigatedToFireflyResult(result: Work<Unit>) {
        mutableState.updateState { copy(loading = false) }
        result.onError { error ->
            when (error) {
                is Error.NoBrowserInstalled,
                is Error.UserVisible -> {
                    mutableState.updateState {
                        copy(
                            fireflyAuthentication = null,
                            nonFatalError = error
                        )
                    }
                }
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    mutableState.updateState { copy(fatalError = error) }
                }
                else -> {
                    Logger.e(tag, error.debugText)
                    mutableState.updateState { copy(fatalError = error) }
                }
            }
        }
    }

    private suspend fun handleOAuthCodeReceived(authentication: OAuthAuthentication) {
        val code = authentication.code
        val state = authentication.state
        if (code != null && state != null) {
            handleScreenOpenedFromDeepLink(state, code)
        }
    }

    private suspend fun handleScreenOpenedFromDeepLink(state: String, code: String) {
        mutableState.updateState { copy(loading = true) }
        getUserAccountByStateUseCase(state)
            .onSuccess { userAccount ->
                val authenticationType = userAccount.authenticationType
                if (authenticationType is UserAccount.AuthenticationType.OAuth) {
                    val clientId = authenticationType.clientId
                    val clientSecret = authenticationType.clientSecret
                    mutableState.updateState {
                        copy(
                            clientId = clientId,
                            clientSecret = clientSecret,
                            serverAddress = userAccount.serverAddress
                        )
                    }
                    retrieveToken(userAccount, clientId, clientSecret, code)
                } else {
                    mutableState.updateState {
                        copy(
                            loading = false,
                            fatalError = Error.AuthenticationProblem
                        )
                    }
                }
            }.onError { error ->
                mutableState.updateState { copy(loading = false) }
                when (error) {
                    is Error.UserVisible -> {
                        mutableState.updateState { copy(nonFatalError = error) }
                    }
                    is Error.Fatal -> {
                        Logger.e(tag, throwable = error.throwable)
                        mutableState.updateState { copy(fatalError = error) }
                    }
                    else -> {
                        Logger.e(tag, error.debugText)
                        mutableState.updateState { copy(fatalError = error) }
                    }
                }
            }
    }

    private fun handleServerAddressChanged(intent: ServerAddressChanged) {
        mutableState.updateState {
            copy(
                serverAddress = intent.serverAddress.trim()
            )
        }
        setLoginState()
    }

    private suspend fun retrieveFireflyInfo() {
        getFireflyProfileUseCase.get().invoke()
            .onSuccess { fireflyProfile ->
                updateCurrentUserAccountUseCase(
                    Email(fireflyProfile.email),
                    FireflyId(fireflyProfile.id),
                    Role(fireflyProfile.role),
                    Type(fireflyProfile.type)
                ).onSuccess {
                    mutableState.updateState {
                        copy(
                            loading = false,
                            stepCompleted = true
                        )
                    }
                }.onError { error ->
                    mutableState.updateState { copy(loading = false) }
                    when (error) {
                        is Error.UserVisible -> {
                            mutableState.updateState { copy(nonFatalError = error) }
                        }
                        is Error.Fatal -> {
                            Logger.e(tag, throwable = error.throwable)
                            mutableState.updateState { copy(fatalError = error) }
                        }
                        else -> {
                            Logger.e(tag, error.debugText)
                            mutableState.updateState { copy(fatalError = error) }
                        }
                    }
                }
            }.onError { error ->
                mutableState.updateState { copy(loading = false) }
                when (error) {
                    is Error.UserVisible,
                    is Error.TokenFailed -> {
                        mutableState.updateState { copy(nonFatalError = error) }
                    }
                    is Error.Fatal -> {
                        Logger.e(tag, throwable = error.throwable)
                        mutableState.updateState { copy(fatalError = error) }
                    }
                    else -> {
                        Logger.e(tag, error.debugText)
                        mutableState.updateState { copy(fatalError = error) }
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
        ).onSuccess { accessToken ->
            updateUserAccount(accessToken, userAccount, clientId, clientSecret, code)
        }.onError { error ->
            mutableState.updateState { copy(loading = false) }
            when (error) {
                is Error.UserVisible -> {
                    mutableState.updateState { copy(nonFatalError = error) }
                }
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    mutableState.updateState { copy(fatalError = error) }
                }
                else -> {
                    Logger.e(tag, error.debugText)
                    mutableState.updateState { copy(fatalError = error) }
                }
            }
        }
    }

    private fun setLoginState() {
        mutableState.updateState {
            copy(
                loginEnabled = with(state.value) {
                    isOauthLoginInputValidUseCase(
                        clientId = clientId,
                        clientSecret = clientSecret,
                        serverAddress = serverAddress
                    )
                }
            )
        }
    }

    private suspend fun updateUserAccount(
        accessToken: Token,
        userAccount: UserAccount,
        clientId: String,
        clientSecret: String,
        oauthCode: String
    ) {
        val updatedUserAccount = userAccount.copy(
            authenticationType = UserAccount.AuthenticationType.OAuth(
                accessToken = accessToken.accessToken,
                clientId = clientId,
                clientSecret = clientSecret,
                oauthCode = oauthCode,
                refreshToken = accessToken.refreshToken
            ),
            isCurrentUserAccount = true,
            state = null
        )
        updateUserAccountUseCase(updatedUserAccount).onSuccess {
            retrieveFireflyInfo()
        }.onError { error ->
            mutableState.updateState { copy(loading = false) }
            when (error) {
                is Error.UserVisible -> {
                    mutableState.updateState { copy(nonFatalError = error) }
                }
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    mutableState.updateState { copy(fatalError = error) }
                }
                else -> {
                    Logger.e(tag, error.debugText)
                    mutableState.updateState { copy(fatalError = error) }
                }
            }
        }
    }

    private companion object {
        const val LOADING_DELAY_IN_MILLISECONDS = 500L
    }
}
