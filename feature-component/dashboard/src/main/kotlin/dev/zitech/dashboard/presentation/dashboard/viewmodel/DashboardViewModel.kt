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

package dev.zitech.dashboard.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.common.presentation.splash.SplashScreenStateController
import dev.zitech.core.persistence.domain.model.database.OnboardingState
import dev.zitech.core.persistence.domain.model.exception.NullCurrentUserAccount
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.GetUserAccountsUseCase
import dev.zitech.core.remoteconfig.domain.usecase.InitializeRemoteConfiguratorUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    private val dashboardStateHandler: DashboardStateHandler,
    private val initializeRemoteConfiguratorUseCase: InitializeRemoteConfiguratorUseCase,
    private val getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase,
    private val splashScreenStateController: SplashScreenStateController
) : ViewModel(), MviViewModel<DashboardIntent, DashboardState> {

    override val state: StateFlow<DashboardState> = dashboardStateHandler.state

    init {
        initializeRemoteConfigurator()
    }

    override fun sendIntent(intent: DashboardIntent) {
        // NO_OP
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
                            NullCurrentUserAccount -> handleNullCurrentUserAccount()
                            else -> showError(result)
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun handleNullCurrentUserAccount() {
        when (val result = getUserAccountsUseCase().first()) {
            is DataResult.Success -> {
                if (result.value.isNotEmpty()) {
                    showSelectAccount()
                } else {
                    showWelcome()
                }
            }
            is DataResult.Error -> showError(result)
        }
    }

    private fun showDashboard() {
        dashboardStateHandler.setOnboardingState(OnboardingState.COMPLETED)
        hideSplashScreen()
    }

    private fun showWelcome() {
        dashboardStateHandler.setOnboardingState(OnboardingState.NOT_COMPLETED)
        hideSplashScreen()
    }

    @Suppress("ForbiddenComment")
    private fun showSelectAccount() {
        // TODO: Add select account screen
        hideSplashScreen()
    }

    @Suppress("ForbiddenComment")
    private fun showError(result: DataResult.Error) {
        // TODO: Show error message
        hideSplashScreen()
    }

    private fun hideSplashScreen() {
        splashScreenStateController(false)
    }
}
