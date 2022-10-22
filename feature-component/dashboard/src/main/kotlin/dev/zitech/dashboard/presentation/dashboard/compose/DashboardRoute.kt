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

package dev.zitech.dashboard.presentation.dashboard.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.core.common.domain.model.ONBOARD_KEY
import dev.zitech.core.common.domain.model.OnboardResult
import dev.zitech.dashboard.domain.model.OnboardingState
import dev.zitech.dashboard.presentation.dashboard.viewmodel.DashboardViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun DashboardRoute(
    navigateToWelcome: () -> Unit,
    onOnboardingCancel: () -> Unit,
    savedStateHandle: SavedStateHandle,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val onboardResult = savedStateHandle.getStateFlow(
        ONBOARD_KEY, OnboardResult.NOT_COMPLETED
    ).collectAsStateWithLifecycle()

    when (state.value.onboardingState) {
        OnboardingState.COMPLETED -> {
            DashboardScreen(
                state = state.value,
                modifier = modifier
            )
        }
        OnboardingState.UNCOMPLETED -> {
            when (onboardResult.value) {
                OnboardResult.NOT_COMPLETED -> {
                    LaunchedEffect(Unit) {
                        navigateToWelcome()
                    }
                }
                OnboardResult.CANCELLED -> {
                    LaunchedEffect(Unit) {
                        onOnboardingCancel()
                    }
                }
            }
        }
        null -> {
            // NO_OP
        }
    }
}
