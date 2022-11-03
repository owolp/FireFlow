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

package dev.zitech.onboarding.presentation.welcome.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.onboarding.presentation.welcome.viewmodel.NavigatedToDemo
import dev.zitech.onboarding.presentation.welcome.viewmodel.NavigatedToOath
import dev.zitech.onboarding.presentation.welcome.viewmodel.NavigatedToPat
import dev.zitech.onboarding.presentation.welcome.viewmodel.OnContinueWithOauthClick
import dev.zitech.onboarding.presentation.welcome.viewmodel.OnContinueWithPatClick
import dev.zitech.onboarding.presentation.welcome.viewmodel.OnDemoClick
import dev.zitech.onboarding.presentation.welcome.viewmodel.WelcomeViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun WelcomeRoute(
    navigateToOath: () -> Unit,
    navigateToPat: () -> Unit,
    navigateToDemo: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    WelcomeScreen(
        modifier = modifier,
        state = state.value,
        onContinueWithOauthClick = { viewModel.sendIntent(OnContinueWithOauthClick) },
        onContinueWithPatClick = { viewModel.sendIntent(OnContinueWithPatClick) },
        onDemoClick = { viewModel.sendIntent(OnDemoClick) },
        navigateToOath = {
            navigateToOath()
            viewModel.sendIntent(NavigatedToOath)
        },
        navigateToPat = {
            navigateToPat()
            viewModel.sendIntent(NavigatedToPat)
        },
        navigateToDemo = {
            navigateToDemo()
            viewModel.sendIntent(NavigatedToDemo)
        }
    )
}
