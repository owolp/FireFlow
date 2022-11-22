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
import dev.zitech.core.common.presentation.architecture.MviViewModel
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase.Destination.Current
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    private val stateHandler: DashboardStateHandler,
    private val getScreenDestinationUseCase: GetScreenDestinationUseCase
) : ViewModel(), MviViewModel<DashboardIntent, DashboardState> {

    override val state: StateFlow<DashboardState> = stateHandler.state

    init {
        getScreenDestination()
    }

    private fun getScreenDestination() {
        getScreenDestinationUseCase().onEach { destination ->
            when (destination) {
                Current -> {
                    stateHandler.setViewState(DashboardState.ViewState.Success)
                }
                else -> stateHandler.setViewState(DashboardState.ViewState.NotLoggedIn(destination))
            }
        }.launchIn(viewModelScope)
    }

    @Suppress("ForbiddenComment")
    override fun sendIntent(intent: DashboardIntent) {
        // TODO
    }
}
