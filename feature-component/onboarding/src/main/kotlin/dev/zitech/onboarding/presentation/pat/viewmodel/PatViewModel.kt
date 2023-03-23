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

import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.flow.StateFlow
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
    private val stateHandler: PatStateHandler,
    private val updateUserAccountUseCase: UpdateUserAccountUseCase
) : ViewModel(), MviViewModel<PatIntent, PatState> {

    private val tag = Logger.tag(this::class.java)

    override val screenState: StateFlow<PatState> = stateHandler.state

    override fun receiveIntent(intent: PatIntent) {
        viewModelScope.launch {
            when (intent) {
                OnLoginClick -> handleOnLoginClick()
                NavigationHandled -> stateHandler.resetEvent()
                OnBackClick -> stateHandler.setEvent(NavigateBack)
                is OnPersonalAccessTokenChange -> {
                    stateHandler.setPersonalAccessToken(intent.pat)
                    setLoginEnabled()
                }
                is OnServerAddressChange -> {
                    stateHandler.setServerAddress(intent.serverAddress)
                    setLoginEnabled()
                }
                ErrorHandled -> stateHandler.resetEvent()
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
                stateHandler.setLoading(false)
                when (error) {
                    is Error.Fatal -> {
                        Logger.e(tag, throwable = error.throwable)
                        stateHandler.setEvent(NavigateToError(error))
                    }
                    is Error.UserVisible,
                    is Error.TokenFailed ->
                        stateHandler.setEvent(ShowError(messageResId = error.uiResId))
                    else -> Logger.e(tag, error.debugText)
                }
            }
    }

    private suspend fun getUserAccountByState(state: String) {
        stateHandler.setLoading(true)
        getUserAccountByStateUseCase(state)
            .onSuccess { userAccount ->
                val authenticationType = userAccount.authenticationType
                if (authenticationType is UserAccount.AuthenticationType.Pat) {
                    val accessToken = authenticationType.accessToken
                    checkToken(userAccount, accessToken)
                } else {
                    stateHandler.setLoading(false)
                    stateHandler.setEvent(
                        NavigateToError(Error.AuthenticationProblem)
                    )
                }
            }.onError { error ->
                removeStaleUserAccountsUseCase()
                stateHandler.setLoading(false)
                when (error) {
                    is Error.NullUserAccountByState -> {
                        Logger.e(tag, error.debugText)
                        stateHandler.setEvent(NavigateToError(error))
                    }
                    is Error.Fatal -> {
                        Logger.e(tag, throwable = error.throwable)
                        stateHandler.setEvent(NavigateToError(error))
                    }
                    else -> {
                        Logger.e(tag, error.debugText)
                        stateHandler.setEvent(ShowError(messageResId = error.uiResId))
                    }
                }
            }
    }

    private suspend fun handleOnLoginClick() {
        val accessToken = screenState.value.pat
        val serverAddress = screenState.value.serverAddress
        val state = DataFactory.createRandomString(STATE_LENGTH)

        withContext(appDispatchers.io) {
            delay(LOADING_DELAY_IN_MILLISECONDS)
            stateHandler.setLoading(true)
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
            stateHandler.setLoading(false)
            when (error) {
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    stateHandler.setEvent(NavigateToError(error))
                }
                is Error.UserVisible ->
                    stateHandler.setEvent(ShowError(text = error.message))
                else -> Logger.e(tag, error.debugText)
            }
        }
    }

    private fun setLoginEnabled() {
        stateHandler.setLoginEnabled(
            with(screenState.value) {
                isPatLoginInputValidUseCase(
                    personalAccessToken = pat,
                    serverAddress = serverAddress
                )
            }
        )
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
            stateHandler.setLoading(false)
            stateHandler.setEvent(NavigateToDashboard)
        }.onError { error ->
            removeStaleUserAccountsUseCase()
            stateHandler.setLoading(false)
            when (error) {
                is Error.NullUserAccount -> {
                    Logger.e(tag, error.debugText)
                    stateHandler.setEvent(NavigateToError(error))
                }
                is Error.Fatal -> {
                    Logger.e(tag, throwable = error.throwable)
                    stateHandler.setEvent(NavigateToError(error))
                }
                else -> {
                    Logger.e(tag, error.debugText)
                    stateHandler.setEvent(ShowError(messageResId = error.uiResId))
                }
            }
        }
    }

    private companion object {
        const val LOADING_DELAY_IN_MILLISECONDS = 500L
    }
}
