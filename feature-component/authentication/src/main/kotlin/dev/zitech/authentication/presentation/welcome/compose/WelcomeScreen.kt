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

package dev.zitech.authentication.presentation.welcome.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.zitech.authentication.presentation.welcome.viewmodel.Idle
import dev.zitech.authentication.presentation.welcome.viewmodel.NavigateToDemo
import dev.zitech.authentication.presentation.welcome.viewmodel.NavigateToOathScreen
import dev.zitech.authentication.presentation.welcome.viewmodel.NavigateToPatScreen
import dev.zitech.authentication.presentation.welcome.viewmodel.WelcomeState
import dev.zitech.ds.atoms.button.FireFlowButtons
import dev.zitech.ds.templates.scaffold.FireFlowScaffolds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WelcomeScreen(
    state: WelcomeState,
    onContinueWithOauthCLick: () -> Unit,
    onContinueWithPatClick: () -> Unit,
    onDemoClick: () -> Unit,
    navigateToOath: () -> Unit,
    navigateToPat: () -> Unit,
    navigateToDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state.event) {
        NavigateToOathScreen -> navigateToOath()
        NavigateToPatScreen -> navigateToPat()
        NavigateToDemo -> navigateToDemo()
        Idle -> {
            // NO_OP
        }
    }

    FireFlowScaffolds.Primary(
        modifier = modifier
    ) {
        WelcomeScreenContent(
            onContinueWithOauthCLick = onContinueWithOauthCLick,
            onContinueWithPatClick = onContinueWithPatClick,
            onDemoClick = onDemoClick
        )
    }
}

@Composable
private fun WelcomeScreenContent(
    onContinueWithOauthCLick: () -> Unit,
    onContinueWithPatClick: () -> Unit,
    onDemoClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FireFlowButtons.Text.OnSurface(text = "Oath", onClick = onContinueWithOauthCLick)
        FireFlowButtons.Text.OnSurface(text = "Pat", onClick = onContinueWithPatClick)
        FireFlowButtons.Text.OnSurface(text = "Demo", onClick = onDemoClick)
    }
}
