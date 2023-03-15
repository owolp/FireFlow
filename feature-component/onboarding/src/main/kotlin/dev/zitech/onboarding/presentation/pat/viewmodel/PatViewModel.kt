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
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.network.domain.usecase.GetFireflyProfileUseCase
import dev.zitech.core.persistence.domain.model.database.UserAccount.Companion.STATE_LENGTH
import dev.zitech.core.persistence.domain.usecase.database.SaveUserAccountUseCase
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
    private val isPatLoginInputValidUseCase: IsPatLoginInputValidUseCase,
    private val saveUserAccountUseCase: SaveUserAccountUseCase,
    private val stateHandler: PatStateHandler
) : ViewModel(), MviViewModel<PatIntent, PatState> {

    override val screenState: StateFlow<PatState> = stateHandler.state

    override fun sendIntent(intent: PatIntent) {
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
            }
        }
    }

    @Suppress("ForbiddenComment")
    private suspend fun handleOnLoginClick() {
        val pat = screenState.value.pat
        val serverAddress = screenState.value.serverAddress
        val state = DataFactory.createRandomString(STATE_LENGTH)

        withContext(appDispatchers.io) {
            delay(LOADING_DELAY_IN_MILLISECONDS)
            stateHandler.setLoading(true)
        }

        // TODO: Dev usage
        stateHandler.setEvent(NavigateToDashboard)
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

    private companion object {
        const val LOADING_DELAY_IN_MILLISECONDS = 500L
    }
}
