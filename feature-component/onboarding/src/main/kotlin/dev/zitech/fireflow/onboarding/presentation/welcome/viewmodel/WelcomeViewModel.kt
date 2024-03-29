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

package dev.zitech.fireflow.onboarding.presentation.welcome.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.fireflow.common.presentation.architecture.MviViewModel
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.onFailure
import dev.zitech.fireflow.core.result.onSuccess
import dev.zitech.fireflow.onboarding.domain.usecase.SaveLocalUserUseCase
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
internal class WelcomeViewModel @Inject constructor(
    private val saveLocalUserUseCase: SaveLocalUserUseCase
) : MviViewModel<WelcomeIntent, WelcomeState>(WelcomeState()) {

    override fun receiveIntent(intent: WelcomeIntent) {
        viewModelScope.launch {
            when (intent) {
                is BackClicked -> handleBackClicked(intent.isBackNavigationSupported)
                ContinueWithOauthClicked -> updateState { copy(oauth = true) }
                ContinueWithPatClicked -> updateState { copy(pat = true) }
                NextHandled -> updateState { copy(next = false) }
                FatalErrorHandled -> updateState { copy(fatalError = null) }
                FireflyClicked -> updateState { copy(fireflyAuthentication = true) }
                GetStartedClicked -> handleGetStarterClicked()
                is NavigatedToFireflyResult -> handleNavigatedToFireflyResult(intent.result)
                NonFatalErrorHandled -> updateState { copy(nonFatalError = null) }
                OAuthHandled -> updateState { copy(oauth = false) }
                PatHandled -> updateState { copy(pat = false) }
                QuitAppHandled -> updateState { copy(quitApp = false) }
                CloseHandled -> updateState { copy(close = false) }
            }
        }
    }

    private fun handleBackClicked(isBackNavigationSupported: Boolean) {
        if (isBackNavigationSupported) {
            updateState { copy(close = true) }
        } else {
            updateState { copy(quitApp = true) }
        }
    }

    private fun handleError(error: Error) {
        when (error) {
            is Error.UserVisible -> updateState { copy(loading = false, nonFatalError = error) }
            else -> updateState { copy(loading = false, fatalError = error) }
        }
    }

    /**
     * Handles the click event when the "Get Started" button is clicked.
     *
     * This function updates the state to show a loading state, saves the local user, and then
     * navigates to the Dashboard Screen after a slight delay. The delay is necessary to ensure
     * that the current user is properly saved in the database before navigating to the Dashboard.
     * Without the delay, there is a possibility that the navigation could happen before the
     * current user is set, resulting in the user being redirected to the Accounts screen instead of
     * the Dashboard screen.
     */
    private suspend fun handleGetStarterClicked() {
        updateState { copy(loading = true) }
        saveLocalUserUseCase().onSuccess {
            delay(SUCCESS_DELAY_IN_MS)
            updateState {
                copy(
                    loading = false,
                    next = true
                )
            }
        }.onFailure(::handleError)
    }

    private suspend fun handleNavigatedToFireflyResult(result: OperationResult<Unit>) {
        result.onSuccess {
            updateState { copy(fireflyAuthentication = false) }
        }.onFailure { error ->
            when (error) {
                is Error.NoBrowserInstalled,
                is Error.UserVisible -> {
                    updateState { copy(fireflyAuthentication = false, nonFatalError = error) }
                }
                else -> {
                    updateState { copy(fireflyAuthentication = false, fatalError = error) }
                }
            }
        }
    }

    private companion object {
        const val SUCCESS_DELAY_IN_MS = 1000L
    }
}
