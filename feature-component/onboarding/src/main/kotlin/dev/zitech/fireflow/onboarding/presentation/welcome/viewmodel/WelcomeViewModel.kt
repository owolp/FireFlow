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
import dev.zitech.fireflow.common.domain.usecase.user.SaveUserAccountUseCase
import dev.zitech.fireflow.common.presentation.architecture.MviViewModel
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.work.OperationResult
import dev.zitech.fireflow.core.work.onFailure
import dev.zitech.fireflow.core.work.onSuccess
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
internal class WelcomeViewModel @Inject constructor(
    private val saveUserAccountUseCase: SaveUserAccountUseCase
) : MviViewModel<WelcomeIntent, WelcomeState>(WelcomeState()) {

    override fun receiveIntent(intent: WelcomeIntent) {
        viewModelScope.launch {
            when (intent) {
                BackClicked -> updateState { copy(quitApp = true) }
                ContinueWithOauthClicked -> updateState { copy(oauth = true) }
                ContinueWithPatClicked -> updateState { copy(pat = true) }
                DemoHandled -> updateState { copy(demo = false) }
                DemoPositiveClicked -> handleOnShowDemoPositive()
                DemoWarningDismissed -> updateState { copy(demoWarning = false) }
                FatalErrorHandled -> updateState { copy(fatalError = null) }
                FireflyClicked -> updateState { copy(fireflyAuthentication = true) }
                GetStartedClicked -> updateState { copy(demoWarning = true) }
                is NavigatedToFireflyResult -> handleNavigatedToFireflyResult(intent.result)
                NonFatalErrorHandled -> updateState { copy(nonFatalError = null) }
                OAuthHandled -> updateState { copy(oauth = false) }
                PatHandled -> updateState { copy(pat = false) }
                QuitAppHandled -> updateState { copy(quitApp = false) }
            }
        }
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

    @Suppress("ForbiddenComment")
    private suspend fun handleOnShowDemoPositive() {
        // TODO: Dev usage
        saveUserAccountUseCase(null, "", "", true, "", "")
        updateState {
            copy(
                demoWarning = false,
                demo = true
            )
        }
    }
}
