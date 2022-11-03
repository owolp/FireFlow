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

package dev.zitech.onboarding.presentation.login.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.onboarding.presentation.login.viewmodel.LoginViewModel
import dev.zitech.onboarding.presentation.login.viewmodel.NavigatedToDashboard
import dev.zitech.onboarding.presentation.login.viewmodel.OnLoginClick

enum class LoginType(val value: String) {
    OAUTH("OAUTH"),
    PAT("PAT")
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun LoginRoute(
    navigateToDashboard: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    if (state.value.loginType != null) {
        LoginScreen(
            state = state.value,
            onLoginClick = { viewModel.sendIntent(OnLoginClick) },
            navigateToDashboard = {
                navigateToDashboard()
                viewModel.sendIntent(NavigatedToDashboard)
            },
            modifier = modifier
        )
    }
}
