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

package dev.zitech.onboarding.presentation.pat.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.dispatcher.AppDispatchers
import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.onError
import dev.zitech.core.common.domain.model.onSuccess
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.network.domain.usecase.GetFireflyProfileUseCase
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.model.database.UserAccount.Companion.STATE_LENGTH
import dev.zitech.core.persistence.domain.usecase.database.GetUserAccountByStateUseCase
import dev.zitech.core.persistence.domain.usecase.database.RemoveStaleUserAccountsUseCase
import dev.zitech.core.persistence.domain.usecase.database.SaveUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.UpdateUserAccountUseCase
import dev.zitech.onboarding.domain.usecase.IsPatLoginInputValidUseCase
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
internal class PatViewModel @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val getFireflyProfileUseCase: dagger.Lazy<GetFireflyProfileUseCase>,
    private val getUserAccountByStateUseCase: GetUserAccountByStateUseCase,
    private val isPatLoginInputValidUseCase: IsPatLoginInputValidUseCase,
    private val removeStaleUserAccountsUseCase: RemoveStaleUserAccountsUseCase,
    private val saveUserAccountUseCase: SaveUserAccountUseCase,
    private val updateUserAccountUseCase: UpdateUserAccountUseCase
) : MviViewModel<PatIntent, PatState>(PatState()) {

    private val tag = Logger.tag(this::class.java)

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

    private suspend fun checkToken(userAccount: UserAccount, accessToken: String) {
        getFireflyProfileUseCase.get().invoke()
            .onSuccess { fireflyProfile ->
                updateUserAccount(
                    accessToken = accessToken,
                    email = fireflyProfile.email,
                    fireflyId = fireflyProfile.id,
                    role = fireflyProfile.role,
                    type = fireflyProfile.type,
                    userAccount = userAccount
                )
            }.onError { error ->
                removeStaleUserAccountsUseCase()
                updateState { copy(loading = false) }
                when (error) {
                    is Error.UserVisible,
                    is Error.TokenFailed -> {
                        updateState { copy(nonFatalError = error) }
                    }
                    is Error.Fatal -> {
                        Logger.e(tag, throwable = error.throwable)
                        updateState { copy(fatalError = error) }
                    }
                    else -> {
                        Logger.e(tag, error.debugText)
                        updateState { copy(fatalError = error) }
                    }
                }
            }
    }

    private suspend fun getUserAccountByState(state: String) {
        updateState { copy(loading = true) }
        getUserAccountByStateUseCase(state)
            .onSuccess { userAccount ->
                val authenticationType = userAccount.authenticationType
                if (authenticationType is UserAccount.AuthenticationType.Pat) {
                    val accessToken = authenticationType.accessToken
                    checkToken(userAccount, accessToken)
                } else {
                    updateState {
                        copy(
                            loading = false,
                            fatalError = Error.AuthenticationProblem
                        )
                    }
                }
            }.onError { error ->
                removeStaleUserAccountsUseCase()
                updateState { copy(loading = false) }
                when (error) {
                    is Error.UserVisible -> {
                        updateState { copy(nonFatalError = error) }
                    }
                    is Error.Fatal -> {
                        Logger.e(tag, throwable = error.throwable)
                        updateState { copy(fatalError = error) }
                    }
                    else -> {
                        Logger.e(tag, error.debugText)
                        updateState { copy(fatalError = error) }
                    }
                }
            }
    }

    private suspend fun handleLoginClicked() {
        val accessToken = state.value.pat
        val serverAddress = state.value.serverAddress
        val state = DataFactory.createRandomString(STATE_LENGTH)

        withContext(appDispatchers.io) {
            delay(LOADING_DELAY_IN_MILLISECONDS)
            updateState { copy(loading = true) }
        }
        saveUserAccountUseCase(
            accessToken = accessToken,
            isCurrentUserAccount = true,
            serverAddress = serverAddress,
            state = state
        ).onSuccess {
            getUserAccountByState(state)
        }.onError { error ->
            removeStaleUserAccountsUseCase()
            updateState { copy(loading = false) }
            when (error) {
                is Error.UserVisible -> {
                    updateState { copy(nonFatalError = error) }
                }
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    updateState { copy(fatalError = error) }
                }
                else -> {
                    Logger.e(tag, error.debugText)
                    updateState { copy(fatalError = error) }
                }
            }
        }
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

    @Suppress("LongParameterList")
    private suspend fun updateUserAccount(
        accessToken: String,
        email: String,
        fireflyId: String,
        role: String,
        type: String,
        userAccount: UserAccount
    ) {
        updateUserAccountUseCase(
            userAccount.copy(
                authenticationType = UserAccount.AuthenticationType.Pat(
                    accessToken = accessToken
                ),
                email = email,
                fireflyId = fireflyId,
                isCurrentUserAccount = true,
                role = role,
                state = null,
                type = type
            )
        ).onSuccess {
            updateState {
                copy(
                    loading = false,
                    stepCompleted = true
                )
            }
        }.onError { error ->
            removeStaleUserAccountsUseCase()
            updateState { copy(loading = false) }
            when (error) {
                is Error.UserVisible -> {
                    updateState { copy(nonFatalError = error) }
                }
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    updateState { copy(fatalError = error) }
                }
                else -> {
                    Logger.e(tag, error.debugText)
                    updateState { copy(fatalError = error) }
                }
            }
        }
    }

    private companion object {
        const val LOADING_DELAY_IN_MILLISECONDS = 500L
    }
}
