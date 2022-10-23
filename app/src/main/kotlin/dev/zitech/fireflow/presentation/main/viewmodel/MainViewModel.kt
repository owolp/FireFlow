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
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.common.presentation.splash.SplashScreenStateController
import dev.zitech.core.persistence.domain.usecase.preferences.GetApplicationThemeValueUseCase
import dev.zitech.core.reporter.analytics.domain.usecase.event.ApplicationLaunchAnalyticsEvent
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val mainStateHandler: MainStateHandler,
    private val getApplicationThemeValueUseCase: GetApplicationThemeValueUseCase,
    private val splashScreenController: SplashScreenStateController,
    applicationLaunchAnalyticsEvent: ApplicationLaunchAnalyticsEvent
) : ViewModel(), MviViewModel<MainIntent, MainState> {

    override val state: StateFlow<MainState> = mainStateHandler.state

    init {
        applicationLaunchAnalyticsEvent()
        initSplashScreenController()
        initApplicationThemeCollection()
    }

    override fun sendIntent(intent: MainIntent) {
        viewModelScope.launch {
            when (intent) {
                ShowErrorHandled -> handleShowErrorHandled()
            }
        }
    }

    private fun handleShowErrorHandled() {
        mainStateHandler.setEvent(Idle)
    }

    private fun initSplashScreenController() {
        splashScreenController.state
            .onEach { mainStateHandler.setSplash(it) }
            .launchIn(viewModelScope)
    }

    private fun initApplicationThemeCollection() {
        getApplicationThemeValueUseCase()
            .onEach { mainStateHandler.setTheme(it) }
            .launchIn(viewModelScope)
    }
}
