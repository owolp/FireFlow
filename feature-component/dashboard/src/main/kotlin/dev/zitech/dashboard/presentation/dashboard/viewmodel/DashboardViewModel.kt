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
import dev.zitech.fireflow.common.presentation.architecture.DeepLinkViewModel
import dev.zitech.fireflow.common.presentation.architecture.MviViewModel
import dev.zitech.fireflow.common.presentation.navigation.ScreenDestinationProvider
import dev.zitech.fireflow.common.presentation.navigation.state.LogInState
import dev.zitech.fireflow.common.presentation.navigation.state.LoginCheckCompletedHandler
import dev.zitech.fireflow.common.presentation.navigation.state.logInState
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    loginCheckCompletedHandler: LoginCheckCompletedHandler,
    screenDestinationProvider: ScreenDestinationProvider
) : MviViewModel<DashboardIntent, DashboardState>(DashboardState), DeepLinkViewModel {

    override val logInState: StateFlow<LogInState> by logInState(
        screenDestinationProvider,
        loginCheckCompletedHandler,
        viewModelScope
    )

    override fun receiveIntent(intent: DashboardIntent) {
        // NO_OP
    }
}
