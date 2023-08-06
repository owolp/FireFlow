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

package dev.zitech.fireflow.presentation.main.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.fireflow.common.domain.usecase.application.GetApplicationThemeValueUseCase
import dev.zitech.fireflow.common.domain.usecase.configurator.InitializeRemoteConfiguratorUseCase
import dev.zitech.fireflow.common.domain.usecase.user.RemoveStaleUsersUseCase
import dev.zitech.fireflow.common.presentation.architecture.MviViewModel
import dev.zitech.fireflow.common.presentation.connectivity.NetworkConnectivityProvider
import dev.zitech.fireflow.common.presentation.connectivity.NetworkState
import dev.zitech.fireflow.common.presentation.navigation.state.LoginCheckCompletedHandler
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.domain.usecase.ApplicationLaunchAnalyticsEvent
import dev.zitech.fireflow.presentation.model.LaunchState
import dev.zitech.fireflow.presentation.model.LaunchState.Status.Error
import dev.zitech.fireflow.presentation.model.LaunchState.Status.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
internal class MainViewModel @Inject constructor(
    applicationLaunchAnalyticsEvent: ApplicationLaunchAnalyticsEvent,
    private val connectivityProvider: NetworkConnectivityProvider,
    private val getApplicationThemeValueUseCase: GetApplicationThemeValueUseCase,
    private val initializeRemoteConfiguratorUseCase: InitializeRemoteConfiguratorUseCase,
    private val loginCheckHandler: LoginCheckCompletedHandler,
    private val removeStaleUsersUseCase: RemoveStaleUsersUseCase
) : MviViewModel<MainIntent, MainState>(MainState()) {

    private val tag = Logger.tag(this::class.java)

    init {
        applicationLaunchAnalyticsEvent()
        startMandatoryChecks()
        observeNetworkConnectivity()
    }

    private fun observeNetworkConnectivity() {
        connectivityProvider.networkState
            .onEach { networkState ->
                when (networkState) {
                    NetworkState.Connected,
                    NetworkState.Unknown -> updateState { copy(isConnected = true) }
                    NetworkState.Disconnected -> updateState { copy(isConnected = false) }
                }
            }
            .launchIn(viewModelScope)
    }

    override fun receiveIntent(intent: MainIntent) {
        viewModelScope.launch {
            when (intent) {
                is ScreenResumed -> handleScreenResumed(intent)
            }
        }
    }

    private fun ScreenResumed.resumingFromOauthDeepLink() =
        code != null && host != null && scheme != null && state != null

    private fun handleScreenResumed(intent: ScreenResumed) {
        viewModelScope.launch {
            with(intent) {
                if (!resumingFromOauthDeepLink()) {
                    removeStaleUsersUseCase()
                }
            }
            updateState { copy(databaseCleanCompleted = true) }
        }
    }

    private fun startMandatoryChecks() {
        combine(
            loginCheckHandler.loginCheckState,
            getApplicationThemeValueUseCase(),
            initializeRemoteConfiguratorUseCase()
        ) { _, themeResult, _ ->
            when (themeResult) {
                is OperationResult.Success -> LaunchState(Success(themeResult.data))
                is OperationResult.Failure -> LaunchState(Error(themeResult.error))
            }
        }.onEach { launchState ->
            when (val status = launchState.status) {
                is Success -> {
                    updateState {
                        copy(
                            theme = status.theme,
                            mandatoryStepsCompleted = true
                        )
                    }
                }
                is Error -> {
                    Logger.e(tag, message = status.fireFlowError.debugText)
                }
            }
        }.launchIn(viewModelScope)
    }
}
