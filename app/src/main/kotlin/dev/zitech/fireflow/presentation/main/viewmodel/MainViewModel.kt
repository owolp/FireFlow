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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.analytics.domain.usecase.event.SendApplicationStartAnalyticsEvent
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.remoteconfig.domain.usecase.InitializeRemoteConfiguratorUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val initializeRemoteConfiguratorUseCase: InitializeRemoteConfiguratorUseCase,
    sendApplicationStartAnalyticsEvent: SendApplicationStartAnalyticsEvent
) : ViewModel(), MviViewModel<MainIntent, MainState> {

    private val mutableShowSplashScreen = MutableLiveData(true)
    val showSplashScreen: LiveData<Boolean> = mutableShowSplashScreen

    private val mutableState = MutableStateFlow(MainState())
    override val state: StateFlow<MainState> = mutableState

    init {
        initializeRemoteConfigurator()
        sendApplicationStartAnalyticsEvent()
    }

    override fun sendIntent(intent: MainIntent) {
        viewModelScope.launch {
            when (intent) {
                ShowErrorHandled -> handleShowErrorHandled()
            }
        }
    }

    private fun handleShowErrorHandled() {
        mutableState.update {
            it.copy(event = Idle)
        }
    }

    private fun initializeRemoteConfigurator() {
        viewModelScope.launch {
            initializeRemoteConfiguratorUseCase().collect {
                mutableShowSplashScreen.postValue(false)
            }
        }
    }
}
