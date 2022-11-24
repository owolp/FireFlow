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

package dev.zitech.authentication.presentation.accounts.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.zitech.authentication.R
import dev.zitech.authentication.presentation.accounts.viewmodel.AccountsState
import dev.zitech.ds.atoms.button.FireFlowButtons
import dev.zitech.ds.molecules.topappbar.FireFlowTopAppBars
import dev.zitech.ds.molecules.topappbar.ScrollBehavior
import dev.zitech.ds.templates.scaffold.FireFlowScaffolds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountsScreen(
    state: AccountsState,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topAppBarScrollBehavior = FireFlowTopAppBars.topAppBarScrollBehavior(
        ScrollBehavior.Pinned
    )

    FireFlowScaffolds.Primary(
        modifier = modifier,
        topBar = {
            FireFlowTopAppBars.Primary(
                title = stringResource(id = R.string.accounts),
                scrollBehavior = topAppBarScrollBehavior
            )
        }
    ) { innerPadding ->
        AccountsContent(
            innerPadding,
            state,
            onLoginClick
        )
    }
}

@Composable
private fun AccountsContent(
    innerPadding: PaddingValues,
    state: AccountsState,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FireFlowButtons.Text.OnSurface(text = "Login", onClick = onLoginClick)
    }
}