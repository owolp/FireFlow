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

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.zitech.ds.atoms.animation.FireFlowAnimations
import dev.zitech.ds.atoms.button.FireFlowButtons
import dev.zitech.ds.templates.scaffold.FireFlowScaffolds
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.onboarding.presentation.welcome.viewmodel.WelcomeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WelcomeScreen(
    state: WelcomeState,
    onContinueWithOauthClick: () -> Unit,
    onContinueWithPatClick: () -> Unit,
    onDemoClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(enabled = true) {
        onBackClick()
    }

    FireFlowScaffolds.Primary(
        modifier = modifier
    ) { innerPadding ->
        WelcomeScreenContent(
            innerPadding,
            onContinueWithOauthClick,
            onContinueWithPatClick,
            onDemoClick
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun WelcomeScreenContent(
    innerPadding: PaddingValues,
    onContinueWithOauthCLick: () -> Unit,
    onContinueWithPatClick: () -> Unit,
    onDemoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(FireFlowTheme.space.m)
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding)
            .navigationBarsPadding()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FireFlowAnimations.MoneyTree(
            modifier = Modifier.weight(1F)
        )
        FireFlowButtons.Text.OnSurface(text = "Oath", onClick = onContinueWithOauthCLick)
        FireFlowButtons.Text.OnSurface(text = "Pat", onClick = onContinueWithPatClick)
        FireFlowButtons.Text.OnSurface(text = "Demo", onClick = onDemoClick)
    }
}
