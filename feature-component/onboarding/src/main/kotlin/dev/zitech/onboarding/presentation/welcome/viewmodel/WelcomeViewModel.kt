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

package dev.zitech.onboarding.presentation.welcome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.Work
import dev.zitech.core.common.domain.model.onError
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.common.presentation.architecture.updateState
import dev.zitech.core.persistence.domain.usecase.database.SaveUserAccountUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class WelcomeViewModel @Inject constructor(
    private val saveUserAccountUseCase: SaveUserAccountUseCase
) : ViewModel(), MviViewModel<WelcomeIntent, WelcomeState> {

    private val mutableState = MutableStateFlow(WelcomeState())
    private val tag = Logger.tag(this::class.java)

    override val state: StateFlow<WelcomeState> = mutableState.asStateFlow()

    override fun receiveIntent(intent: WelcomeIntent) {
        viewModelScope.launch {
            when (intent) {
                BackClicked -> mutableState.updateState { copy(quitApp = true) }
                ContinueWithOauthClicked -> mutableState.updateState { copy(oauth = true) }
                ContinueWithPatClicked -> mutableState.updateState { copy(pat = true) }
                DemoHandled -> mutableState.updateState { copy(demo = false) }
                DemoPositiveClicked -> handleOnShowDemoPositive()
                DemoWarningDismissed -> mutableState.updateState { copy(demoWarning = false) }
                FatalErrorHandled -> mutableState.updateState { copy(fatalError = null) }
                FireflyClicked -> mutableState.updateState { copy(fireflyAuthentication = true) }
                GetStartedClicked -> mutableState.updateState { copy(demoWarning = true) }
                is NavigatedToFireflyResult -> handleNavigatedToFireflyResult(intent.result)
                NonFatalErrorHandled -> mutableState.updateState { copy(nonFatalError = null) }
                OAuthHandled -> mutableState.updateState { copy(oauth = false) }
                PatHandled -> mutableState.updateState { copy(pat = false) }
                QuitAppHandled -> mutableState.updateState { copy(quitApp = false) }
            }
        }
    }

    private suspend fun handleNavigatedToFireflyResult(result: Work<Unit>) {
        mutableState.updateState { copy(fireflyAuthentication = false) }
        result.onError { error ->
            when (error) {
                is Error.NoBrowserInstalled,
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

    @Suppress("ForbiddenComment")
    private suspend fun handleOnShowDemoPositive() {
        // TODO: Dev usage
        saveUserAccountUseCase(null, "", "", true, "", "")
        mutableState.updateState {
            copy(
                demoWarning = false,
                demo = true
            )
        }
    }
}
