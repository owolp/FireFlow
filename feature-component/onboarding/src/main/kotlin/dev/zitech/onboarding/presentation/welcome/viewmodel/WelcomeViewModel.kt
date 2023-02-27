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
import dev.zitech.core.common.domain.exception.FireFlowException
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.LegacyDataResult
import dev.zitech.core.common.domain.model.exception.NoBrowserInstalledException
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

    private val tag = Logger.tag(this::class.java)

    override val screenState: StateFlow<WelcomeState> = stateHandler.state

    override fun sendIntent(intent: WelcomeIntent) {
        viewModelScope.launch {
            when (intent) {
                OnContinueWithOauthClick -> stateHandler.setEvent(NavigateToOAuth)
                OnContinueWithPatClick -> stateHandler.setEvent(NavigateToPat)
                OnGetStartedClick -> handleOnGetStartedClick()
                OnBackClick -> stateHandler.setEvent(NavigateOutOfApp)
                OnFireflyClick -> handleOnFireflyClick()
                OnShowDemoPositive -> handleOnShowDemoPositive()
                is NavigatedToFireflyResult -> handleNavigatedToFireflyResult(intent.dataResult)
                NavigationHandled,
                OnShowDemoDismiss,
                ErrorHandled -> stateHandler.resetEvent()
            }
        }
    }

    private fun handleOnGetStartedClick() {
        stateHandler.setEvent(
            ShowDemoWarning(
                text = welcomeStringsProvider.getDemoDialogText(),
                confirm = welcomeStringsProvider.getDemoDialogConfirm()
            )
        )
    }

    private fun handleOnFireflyClick() {
        stateHandler.setEvent(NavigateToFirefly(welcomeStringsProvider.getFireflyHomePageUrl()))
    }

    @Suppress("ForbiddenComment")
    private suspend fun handleOnShowDemoPositive() {
        // TODO: Dev usage
        saveUserAccountUseCase("", "", true, "", "")
        stateHandler.setEvent(NavigateToDemo)
    }

    private fun handleNavigatedToFireflyResult(dataResult: LegacyDataResult<Unit>) {
        when (dataResult) {
            is LegacyDataResult.Success -> stateHandler.resetEvent()
            is LegacyDataResult.Error -> {
                when (dataResult.cause) {
                    is NoBrowserInstalledException -> {
                        stateHandler.setEvent(
                            ShowError(welcomeStringsProvider.getNoSupportedBrowserInstalled())
                        )
                    }
                    else -> {
                        Logger.e(tag, exception = dataResult.cause)
                        stateHandler.setEvent(NavigateToError(FireFlowException.Legacy))
                    }
                }
            }
        }
    }
}
