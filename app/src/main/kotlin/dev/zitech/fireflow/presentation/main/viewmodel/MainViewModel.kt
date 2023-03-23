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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.common.presentation.splash.LoginCheckCompletedHandler
import dev.zitech.core.persistence.domain.usecase.database.RemoveStaleUserAccountsUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetApplicationThemeValueUseCase
import dev.zitech.core.remoteconfig.domain.usecase.InitializeRemoteConfiguratorUseCase
import dev.zitech.core.reporter.analytics.domain.usecase.event.ApplicationLaunchAnalyticsEvent
import dev.zitech.fireflow.presentation.model.LaunchState
import dev.zitech.fireflow.presentation.model.LaunchState.Status.Error
import dev.zitech.fireflow.presentation.model.LaunchState.Status.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val stateHandler: MainStateHandler,
    private val loginCheckHandler: LoginCheckCompletedHandler,
    private val getApplicationThemeValueUseCase: GetApplicationThemeValueUseCase,
    private val initializeRemoteConfiguratorUseCase: InitializeRemoteConfiguratorUseCase,
    private val removeStaleUserAccountsUseCase: RemoveStaleUserAccountsUseCase,
    applicationLaunchAnalyticsEvent: ApplicationLaunchAnalyticsEvent
) : ViewModel(), MviViewModel<MainIntent, MainState> {

    private val tag = Logger.tag(this::class.java)

    override val state: StateFlow<MainState> = stateHandler.state

    init {
        applicationLaunchAnalyticsEvent()
        startMandatoryChecks()
    }

    private fun startMandatoryChecks() {
        viewModelScope.launch {
            combine(
                loginCheckHandler.loginCheckState,
                getApplicationThemeValueUseCase(),
                initializeRemoteConfiguratorUseCase()
            ) { _, themeResult, _ ->
                LaunchState(Success(themeResult))
            }.onEach { launchState ->
                when (val status = launchState.status) {
                    is Success -> {
                        stateHandler.setTheme(status.theme)
                        stateHandler.setMandatoryCompleted(true)
                    }
                    is Error -> {
                        Logger.e(tag, exception = status.cause)
                    }
                }
            }.collect()
        }
    }

    override fun receiveIntent(intent: MainIntent) {
        viewModelScope.launch {
            when (intent) {
                is ScreenResumed -> handleScreenResumed(intent)
            }
        }
    }

    private fun handleScreenResumed(intent: ScreenResumed) {
        viewModelScope.launch {
            with(intent) {
                if (!resumingFromOauthDeepLink()) {
                    removeStaleUserAccountsUseCase()
                }
            }
            stateHandler.setDatabaseCleanCompleted(true)
        }
    }

    private fun ScreenResumed.resumingFromOauthDeepLink() =
        code != null && host != null && scheme != null && state != null
}
