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

package dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.fireflow.common.domain.model.authentication.Token
import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.common.domain.model.user.UserAuthenticationType
import dev.zitech.fireflow.common.domain.usecase.profile.GetFireflyProfileUseCase
import dev.zitech.fireflow.common.domain.usecase.user.GetUserByStateUseCase
import dev.zitech.fireflow.common.domain.usecase.user.RemoveStaleUsersUseCase
import dev.zitech.fireflow.common.domain.usecase.user.SaveUserUseCase
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserUseCase
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserUseCase.Email
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserUseCase.FireflyId
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserUseCase.Role
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserUseCase.Type
import dev.zitech.fireflow.common.domain.usecase.user.UpdateUserUseCase
import dev.zitech.fireflow.common.presentation.architecture.MviViewModel
import dev.zitech.fireflow.core.dispatcher.AppDispatchers
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.onFailure
import dev.zitech.fireflow.core.result.onSuccess
import dev.zitech.fireflow.onboarding.domain.usecase.GetAccessTokenUseCase
import dev.zitech.fireflow.onboarding.domain.usecase.IsOAuthLoginInputValidUseCase
import dev.zitech.fireflow.onboarding.domain.validator.ClientIdValidator
import dev.zitech.fireflow.onboarding.presentation.oauth.model.OAuthAuthentication
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
internal class OAuthViewModel @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val clientIdValidator: ClientIdValidator,
    private val getAccessTokenUseCase: dagger.Lazy<GetAccessTokenUseCase>,
    private val getFireflyProfileUseCase: dagger.Lazy<GetFireflyProfileUseCase>,
    private val getUserByStateUseCase: GetUserByStateUseCase,
    private val isOauthLoginInputValidUseCase: IsOAuthLoginInputValidUseCase,
    private val removeStaleUsersUseCase: RemoveStaleUsersUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : MviViewModel<OAuthIntent, OAuthState>(OAuthState()) {

    override fun receiveIntent(intent: OAuthIntent) {
        viewModelScope.launch {
            when (intent) {
                AuthenticationCanceled -> updateState {
                    copy(
                        loading = false,
                        fireflyAuthentication = null
                    )
                }
                BackClicked -> updateState { copy(stepClosed = true) }
                is ClientIdChanged -> handleClientIdChanged(intent)
                is ClientSecretChanged -> handleClientSecretChanged(intent)
                FatalErrorHandled -> updateState { copy(fatalError = null) }
                LoginClicked -> handleLoginClicked()
                is NavigatedToFireflyResult -> handleNavigatedToFireflyResult(intent.result)
                NonFatalErrorHandled -> updateState { copy(nonFatalError = null) }
                is OauthCodeReceived -> handleOAuthCodeReceived(intent.authentication)
                is ServerAddressChanged -> handleServerAddressChanged(intent)
                StepClosedHandled -> updateState { copy(stepClosed = false) }
                StepCompletedHandled -> updateState { copy(stepCompleted = false) }
            }
        }
    }

    private fun handleClientIdChanged(intent: ClientIdChanged) {
        with(intent.clientId.trim()) {
            if (clientIdValidator(this) || this.isEmpty()) {
                updateState { copy(clientId = this@with) }
                setLoginState()
            }
        }
    }

    private fun handleClientSecretChanged(intent: ClientSecretChanged) {
        updateState {
            copy(
                clientSecret = intent.clientSecret.trim()
            )
        }
        setLoginState()
    }

    private suspend fun handleError(error: Error) {
        removeStaleUsersUseCase()
        when (error) {
            is Error.UserVisible,
            is Error.UserWithServerAlreadyExists -> updateState {
                copy(
                    loading = false,
                    nonFatalError = error
                )
            }
            else -> updateState { copy(loading = false, fatalError = error) }
        }
    }

    private suspend fun handleLoginClicked() {
        val clientId = state.value.clientId
        val clientSecret = state.value.clientSecret
        val serverAddress = state.value.serverAddress
        val state = User.getRandomState()

        withContext(appDispatchers.io) {
            delay(LOADING_DELAY_IN_MILLISECONDS)
            updateState { copy(loading = true) }
        }
        saveUserUseCase(
            clientId = clientId,
            clientSecret = clientSecret,
            connectivityNotification = true,
            isCurrentUser = false,
            serverAddress = serverAddress,
            state = state
        ).onSuccess {
            updateState {
                copy(
                    fireflyAuthentication = state,
                    loading = true
                )
            }
        }.onFailure(::handleError)
    }

    private suspend fun handleNavigatedToFireflyResult(result: OperationResult<Unit>) {
        result.onSuccess {
            updateState { copy(loading = false) }
        }.onFailure { error ->
            when (error) {
                is Error.NoBrowserInstalled,
                is Error.UserVisible -> {
                    updateState {
                        copy(
                            loading = false,
                            fireflyAuthentication = null,
                            nonFatalError = error
                        )
                    }
                }

                else -> {
                    updateState {
                        copy(
                            loading = false,
                            fireflyAuthentication = null,
                            fatalError = error
                        )
                    }
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
        updateState { copy(loading = true) }
        getUserByStateUseCase(state)
            .onSuccess { user ->
                when (user) {
                    is User.Local -> {
                        // NO_OP
                    }
                    is User.Remote -> {
                        val authenticationType = user.authenticationType
                        if (authenticationType is UserAuthenticationType.OAuth) {
                            val clientId = authenticationType.clientId
                            val clientSecret = authenticationType.clientSecret
                            updateState {
                                copy(
                                    clientId = clientId,
                                    clientSecret = clientSecret,
                                    serverAddress = user.serverAddress
                                )
                            }
                            retrieveToken(user, clientId, clientSecret, code)
                        } else {
                            updateState {
                                copy(
                                    loading = false,
                                    fatalError = Error.AuthenticationProblem
                                )
                            }
                        }
                    }
                }
            }.onFailure(::handleError)
    }

    private fun handleServerAddressChanged(intent: ServerAddressChanged) {
        updateState {
            copy(
                serverAddress = intent.serverAddress.trim()
            )
        }
        setLoginState()
    }

    private suspend fun retrieveFireflyInfo() {
        getFireflyProfileUseCase.get().invoke()
            .onSuccess { fireflyProfile ->
                updateCurrentUserUseCase(
                    Email(fireflyProfile.email),
                    FireflyId(fireflyProfile.id),
                    Role(fireflyProfile.role),
                    Type(fireflyProfile.type)
                ).onSuccess {
                    updateState {
                        copy(
                            loading = false,
                            stepCompleted = true
                        )
                    }
                }.onFailure(::handleError)
            }.onFailure(::handleError)
    }

    private suspend fun retrieveToken(
        user: User,
        clientId: String,
        clientSecret: String,
        code: String
    ) {
        getAccessTokenUseCase.get()(
            clientId = clientId,
            clientSecret = clientSecret,
            code = code
        ).onSuccess { accessToken ->
            updateUser(accessToken, user, clientId, clientSecret, code)
        }.onFailure(::handleError)
    }

    private fun setLoginState() {
        updateState {
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

    private suspend fun updateUser(
        accessToken: Token,
        user: User,
        clientId: String,
        clientSecret: String,
        oauthCode: String
    ) {
        when (user) {
            is User.Local -> {
                // NO_OP
            }
            is User.Remote -> {
                val updatedUser = user.copy(
                    authenticationType = UserAuthenticationType.OAuth(
                        accessToken = accessToken.accessToken,
                        clientId = clientId,
                        clientSecret = clientSecret,
                        oauthCode = oauthCode,
                        refreshToken = accessToken.refreshToken
                    ),
                    isCurrentUser = true,
                    state = null
                )
                updateUserUseCase(updatedUser).onSuccess {
                    retrieveFireflyInfo()
                }.onFailure(::handleError)
            }
        }
    }

    private companion object {
        const val LOADING_DELAY_IN_MILLISECONDS = 500L
    }
}
