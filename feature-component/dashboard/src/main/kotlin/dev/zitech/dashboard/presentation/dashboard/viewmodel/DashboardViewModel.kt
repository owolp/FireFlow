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

package dev.zitech.dashboard.presentation.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.onError
import dev.zitech.core.common.domain.model.onSuccess
import dev.zitech.core.common.domain.navigation.LogInState
import dev.zitech.core.common.presentation.architecture.DeepLinkViewModel
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.common.presentation.splash.LoginCheckCompletedHandler
import dev.zitech.core.network.domain.usecase.GetFireflyProfileUseCase
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase
import dev.zitech.navigation.presentation.extension.logInState
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    stateHandler: DashboardStateHandler,
    loginCheckCompletedHandler: LoginCheckCompletedHandler,
    getScreenDestinationUseCase: GetScreenDestinationUseCase,
    private val getFireflyProfileUseCase: dagger.Lazy<GetFireflyProfileUseCase>
) : ViewModel(), MviViewModel<DashboardIntent, DashboardState>, DeepLinkViewModel {

    private val tag = Logger.tag(this::class.java)

    override val screenState: StateFlow<DashboardState> = stateHandler.state
    override val logInState: StateFlow<LogInState> by logInState(
        getScreenDestinationUseCase,
        loginCheckCompletedHandler,
        viewModelScope
    )

    override fun sendIntent(intent: DashboardIntent) {
        if (intent is DoDevJob) {
            viewModelScope.launch {
                getFireflyProfileUseCase.get().invoke()
                    .onSuccess {
                        Logger.d(tag, it.toString())
                    }.onError { error ->
                        when (error) {
                            is Error.Fatal -> {
                                Logger.e(tag, throwable = error.throwable)
                            }
                            else -> Logger.e(tag, error.text)
                        }
                    }
            }
        }
    }
}
