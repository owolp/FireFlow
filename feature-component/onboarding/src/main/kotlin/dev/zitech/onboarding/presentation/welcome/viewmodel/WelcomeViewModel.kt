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
import dev.zitech.onboarding.presentation.welcome.viewmodel.resoure.WelcomeStringsProvider
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class WelcomeViewModel @Inject constructor(
    private val stateHandler: WelcomeStateHandler,
    private val welcomeStringsProvider: WelcomeStringsProvider,
    private val saveUserAccountUseCase: SaveUserAccountUseCase
) : ViewModel(), MviViewModel<WelcomeIntent, WelcomeState> {

    override val screenState: StateFlow<WelcomeState> = stateHandler.state

    override fun sendIntent(intent: WelcomeIntent) {
        viewModelScope.launch {
            when (intent) {
                OnContinueWithOauthClick -> handleOnContinueWithOauthClick()
                OnContinueWithPatClick -> handleOnContinueWithPatClick()
                OnDemoClick -> handleOnDemoClick()
                OnBackClick -> handleOnBackClick()
                OnFireflyClick -> handleOnFireflyClick()
                NavigationHandled -> handleNavigationHandled()
                OnShowDemoDismiss -> handleOnShowDemoDismiss()
                OnShowDemoPositive -> handleOnShowDemoPositive()
            }
        }
    }

    private fun handleOnContinueWithOauthClick() {
        stateHandler.setEvent(NavigateToOath)
    }

    private fun handleOnContinueWithPatClick() {
        stateHandler.setEvent(NavigateToPat)
    }

    private fun handleOnDemoClick() {
        stateHandler.setEvent(
            ShowDemoWarning(
                text = welcomeStringsProvider.getDemoDialogText(),
                confirm = welcomeStringsProvider.getDemoDialogConfirm()
            )
        )
    }

    private fun handleOnBackClick() {
        stateHandler.setEvent(NavigateOutOfApp)
    }

    private fun handleOnFireflyClick() {
        stateHandler.setEvent(NavigateToFirefly(welcomeStringsProvider.getFireflyUrl()))
    }

    private fun handleNavigationHandled() {
        stateHandler.resetEvent()
    }

    private fun handleOnShowDemoDismiss() {
        stateHandler.resetEvent()
    }

    @Suppress("ForbiddenComment")
    private suspend fun handleOnShowDemoPositive() {
        // TODO: Dev usage
        saveUserAccountUseCase(true)
        stateHandler.setEvent(NavigateToDemo)
    }
}
