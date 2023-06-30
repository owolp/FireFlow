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

package dev.zitech.fireflow.onboarding.presentation.pat.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.common.domain.model.user.UserAuthenticationType
import dev.zitech.fireflow.common.domain.usecase.profile.GetFireflyProfileUseCase
import dev.zitech.fireflow.common.domain.usecase.user.GetUserByStateUseCase
import dev.zitech.fireflow.common.domain.usecase.user.RemoveStaleUsersUseCase
import dev.zitech.fireflow.common.domain.usecase.user.SaveUserUseCase
import dev.zitech.fireflow.common.domain.usecase.user.UpdateUserUseCase
import dev.zitech.fireflow.common.presentation.architecture.MviViewModel
import dev.zitech.fireflow.core.dispatcher.AppDispatchers
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.onFailure
import dev.zitech.fireflow.core.result.onSuccess
import dev.zitech.fireflow.onboarding.domain.usecase.IsPatLoginInputValidUseCase
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
internal class PatViewModel @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val getFireflyProfileUseCase: dagger.Lazy<GetFireflyProfileUseCase>,
    private val getUserByStateUseCase: GetUserByStateUseCase,
    private val isPatLoginInputValidUseCase: IsPatLoginInputValidUseCase,
    private val removeStaleUsersUseCase: RemoveStaleUsersUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : MviViewModel<PatIntent, PatState>(PatState()) {

    override fun receiveIntent(intent: PatIntent) {
        viewModelScope.launch {
            when (intent) {
                BackClicked -> updateState { copy(stepClosed = true) }
                FatalErrorHandled -> updateState { copy(fatalError = null) }
                LoginClicked -> handleLoginClicked()
                NonFatalErrorHandled -> updateState { copy(nonFatalError = null) }
                is PersonalAccessTokenChanged -> handlePersonalAccessTokenChanged(intent)
                is ServerAddressChanged -> handleServerAddressChanged(intent)
                StepClosedHandled -> updateState { copy(stepClosed = false) }
                StepCompletedHandled -> updateState { copy(stepCompleted = false) }
            }
        }
    }

    private suspend fun checkToken(user: User, accessToken: String) {
        getFireflyProfileUseCase.get().invoke()
            .onSuccess { fireflyProfile ->
                updateUser(
                    accessToken = accessToken,
                    email = fireflyProfile.email,
                    fireflyId = fireflyProfile.id,
                    role = fireflyProfile.role,
                    type = fireflyProfile.type,
                    user = user
                )
            }.onFailure(::handleError)
    }

    private suspend fun getUserByState(state: String) {
        updateState { copy(loading = true) }
        getUserByStateUseCase(state)
            .onSuccess { user ->
                when (user) {
                    is User.Local -> {
                        // NO_OP
                    }
                    is User.Remote -> {
                        val authenticationType = user.authenticationType
                        if (authenticationType is UserAuthenticationType.Pat) {
                            val accessToken = authenticationType.accessToken
                            checkToken(user, accessToken)
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
            is Error.Fatal -> {
                if (error.type == Error.Fatal.Type.NETWORK) {
                    updateState {
                        copy(
                            loading = false,
                            nonFatalError = error.toUserVisible()
                        )
                    }
                } else {
                    updateState { copy(loading = false, fatalError = error) }
                }
            }
            else -> updateState { copy(loading = false, fatalError = error) }
        }
    }

    private suspend fun handleLoginClicked() {
        val accessToken = state.value.pat
        val serverAddress = state.value.serverAddress
        val state = User.getRandomState()

        withContext(appDispatchers.io) {
            delay(LOADING_DELAY_IN_MILLISECONDS)
            updateState { copy(loading = true) }
        }
        saveUserUseCase(
            accessToken = accessToken,
            connectivityNotification = true,
            isCurrentUser = true,
            serverAddress = serverAddress,
            state = state
        ).onSuccess {
            getUserByState(state)
        }.onFailure(::handleError)
    }

    private fun handlePersonalAccessTokenChanged(intent: PersonalAccessTokenChanged) {
        updateState {
            copy(
                pat = intent.pat.trim()
            )
        }
        setLoginState()
    }

    private fun handleServerAddressChanged(intent: ServerAddressChanged) {
        updateState {
            copy(
                serverAddress = intent.serverAddress.trim()
            )
        }
        setLoginState()
    }

    private fun setLoginState() {
        updateState {
            copy(
                loginEnabled = with(state.value) {
                    isPatLoginInputValidUseCase(
                        personalAccessToken = pat,
                        serverAddress = serverAddress
                    )
                }
            )
        }
    }

    private suspend fun updateUser(
        accessToken: String,
        email: String,
        fireflyId: String,
        role: String,
        type: String,
        user: User
    ) {
        when (user) {
            is User.Local -> {
                // NO_OP
            }
            is User.Remote -> {
                val updatedUser = user.copy(
                    authenticationType = UserAuthenticationType.Pat(
                        accessToken = accessToken
                    ),
                    email = email,
                    fireflyId = fireflyId,
                    isCurrentUser = true,
                    role = role,
                    state = null,
                    type = type
                )
                updateUserUseCase(updatedUser).onSuccess {
                    updateState {
                        copy(
                            loading = false,
                            stepCompleted = true
                        )
                    }
                }.onFailure(::handleError)
            }
        }
    }

    private companion object {
        const val LOADING_DELAY_IN_MILLISECONDS = 500L
    }
}
