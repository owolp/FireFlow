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

package dev.zitech.fireflow.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.authenticate.presentation.navigation.AccountsDestination
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.persistence.domain.model.exception.NullCurrentUserAccountException
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.GetUserAccountsUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetApplicationThemeValueUseCase
import dev.zitech.core.remoteconfig.domain.usecase.InitializeRemoteConfiguratorUseCase
import dev.zitech.core.reporter.analytics.domain.usecase.event.ApplicationLaunchAnalyticsEvent
import dev.zitech.dashboard.presentation.navigation.DashboardDestination
import dev.zitech.onboarding.presentation.navigation.WelcomeDestination
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val stateHandler: MainStateHandler,
    private val getApplicationThemeValueUseCase: GetApplicationThemeValueUseCase,
    private val initializeRemoteConfiguratorUseCase: InitializeRemoteConfiguratorUseCase,
    private val getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
    applicationLaunchAnalyticsEvent: ApplicationLaunchAnalyticsEvent
) : ViewModel(), MviViewModel<MainIntent, MainState> {

    override val state: StateFlow<MainState> = stateHandler.state

    init {
        initializeRemoteConfigurator()
        initApplicationThemeCollection()
        applicationLaunchAnalyticsEvent()
    }

    override fun sendIntent(intent: MainIntent) {
        viewModelScope.launch {
            when (intent) {
                ShowErrorHandled -> handleShowErrorHandled()
            }
        }
    }

    private fun handleShowErrorHandled() {
        stateHandler.resetEvent()
    }

    private fun initApplicationThemeCollection() {
        getApplicationThemeValueUseCase()
            .onEach { stateHandler.setTheme(it) }
            .launchIn(viewModelScope)
    }

    private fun initializeRemoteConfigurator() {
        initializeRemoteConfiguratorUseCase()
            .onCompletion { collectCurrentUserAccount() }
            .launchIn(viewModelScope)
    }

    private fun collectCurrentUserAccount() {
        getCurrentUserAccountUseCase()
            .onEach { result ->
                when (result) {
                    is DataResult.Success -> showDashboard()
                    is DataResult.Error -> {
                        when (result.cause) {
                            NullCurrentUserAccountException -> handleNullCurrentUserAccount()
                            else -> showError(result)
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun showDashboard() {
        stateHandler.setDestination(DashboardDestination)
        hideSplashScreen()
    }

    private suspend fun handleNullCurrentUserAccount() {
        when (val result = getUserAccountsUseCase().first()) {
            is DataResult.Success -> {
                if (result.value.isNotEmpty()) {
                    showAccounts()
                } else {
                    showWelcome()
                }
            }
            is DataResult.Error -> showError(result)
        }
    }

    private fun showAccounts() {
        stateHandler.setDestination(AccountsDestination)
        hideSplashScreen()
    }

    private fun showWelcome() {
        stateHandler.setDestination(WelcomeDestination)
        hideSplashScreen()
    }

    @Suppress("ForbiddenComment", "UnusedPrivateMember")
    private fun showError(result: DataResult.Error) {
        // TODO: Show error message
        hideSplashScreen()
    }

    private fun hideSplashScreen() {
        stateHandler.setSplash(false)
    }
}
