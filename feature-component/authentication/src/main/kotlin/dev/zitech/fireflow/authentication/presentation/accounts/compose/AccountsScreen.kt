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

package dev.zitech.fireflow.authentication.presentation.accounts.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.zitech.fireflow.authentication.R
import dev.zitech.fireflow.authentication.presentation.accounts.viewmodel.AccountsState
import dev.zitech.fireflow.ds.atoms.button.FireFlowButtons
import dev.zitech.fireflow.ds.molecules.topappbar.FireFlowTopAppBars
import dev.zitech.fireflow.ds.molecules.topappbar.ScrollBehavior
import dev.zitech.fireflow.ds.templates.scaffold.FireFlowScaffolds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountsScreen(
    accountsState: AccountsState,
    loginClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topAppBarScrollBehavior = FireFlowTopAppBars.topAppBarScrollBehavior(
        ScrollBehavior.Pinned
    )

    FireFlowScaffolds.Primary(
        modifier = modifier,
        topBar = {
            FireFlowTopAppBars.Primary(
                title = stringResource(R.string.accounts),
                scrollBehavior = topAppBarScrollBehavior
            )
        }
    ) { innerPadding ->
        AccountsContent(
            innerPadding = innerPadding,
            loginClicked = loginClicked,
            state = accountsState
        )
    }
}

@Composable
private fun AccountsContent(
    innerPadding: PaddingValues,
    loginClicked: () -> Unit,
    state: AccountsState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FireFlowButtons.Text.OnSurface(text = "Login", onClick = loginClicked)
    }
}
