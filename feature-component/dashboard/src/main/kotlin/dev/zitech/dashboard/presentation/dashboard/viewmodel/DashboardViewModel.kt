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
import dev.zitech.core.common.domain.navigation.LogInState
import dev.zitech.core.common.presentation.architecture.DeepLinkViewModel
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.common.presentation.splash.SplashScreenStateHandler
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase
import dev.zitech.navigation.presentation.extension.logInState
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    stateHandler: DashboardStateHandler,
    splashScreenState: SplashScreenStateHandler,
    getScreenDestinationUseCase: GetScreenDestinationUseCase
) : ViewModel(), MviViewModel<DashboardIntent, DashboardState>, DeepLinkViewModel {

    override val screenState: StateFlow<DashboardState> = stateHandler.state
    override val logInState: StateFlow<LogInState> by logInState(
        getScreenDestinationUseCase,
        splashScreenState,
        viewModelScope
    )

    @Suppress("ForbiddenComment")
    override fun sendIntent(intent: DashboardIntent) {
        // TODO
    }
}
