/*
 * Copyright (C) 2022 Zitech Ltd.
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

package dev.zitech.onboarding.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.persistence.domain.usecase.database.SaveUserAccountUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WelcomeViewModel @Inject constructor(
    private val welcomeStateHandler: WelcomeStateHandler,
    private val saveUserAccountUseCase: SaveUserAccountUseCase
) : ViewModel(), MviViewModel<WelcomeIntent, WelcomeState> {

    override val state: StateFlow<WelcomeState> = welcomeStateHandler.state

    override fun sendIntent(intent: WelcomeIntent) {
        viewModelScope.launch {
            when (intent) {
                OnContinueWithOauthClick -> handleOnContinueWithOauthClick()
                OnContinueWithPatClick -> handleOnContinueWithPatClick()
                OnDemoClick -> handleOnDemoClick()
                NavigatedToOath -> handleNavigatedToOath()
                NavigatedToPat -> handleNavigatedToPat()
                NavigatedToDemo -> handleNavigatedToDemo()
            }
        }
    }

    @Suppress("ForbiddenComment")
    private suspend fun handleOnContinueWithOauthClick() {
        // TODO: Dev usage
        welcomeStateHandler.setEvent(NavigateToOathScreen)
        saveUserAccountUseCase(true)
    }

    @Suppress("ForbiddenComment")
    private suspend fun handleOnContinueWithPatClick() {
        // TODO: Dev usage
        welcomeStateHandler.setEvent(NavigateToPatScreen)
        saveUserAccountUseCase(true)
    }

    private suspend fun handleOnDemoClick() {
        welcomeStateHandler.setEvent(NavigateToDemo)
    }

    private fun handleNavigatedToOath() {
        welcomeStateHandler.resetEvent()
    }

    private fun handleNavigatedToPat() {
        welcomeStateHandler.resetEvent()
    }

    private fun handleNavigatedToDemo() {
        welcomeStateHandler.resetEvent()
    }
}
