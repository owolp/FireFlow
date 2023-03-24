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

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zitech.core.common.domain.navigation.LogInState
import dev.zitech.core.common.presentation.architecture.DeepLinkViewModel
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.core.common.presentation.splash.LoginCheckCompletedHandler
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase
import dev.zitech.navigation.presentation.extension.logInState
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    getScreenDestinationUseCase: GetScreenDestinationUseCase,
    loginCheckCompletedHandler: LoginCheckCompletedHandler
) : MviViewModel<DashboardIntent, DashboardState>(DashboardState), DeepLinkViewModel {

    override val logInState: StateFlow<LogInState> by logInState(
        getScreenDestinationUseCase,
        loginCheckCompletedHandler,
        viewModelScope
    )

    override fun receiveIntent(intent: DashboardIntent) {
        // NO_OP
    }
}
