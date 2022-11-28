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

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.zitech.dashboard.R
import dev.zitech.dashboard.presentation.dashboard.viewmodel.DashboardState
import dev.zitech.ds.molecules.topappbar.FireFlowTopAppBars
import dev.zitech.ds.molecules.topappbar.ScrollBehavior
import dev.zitech.ds.templates.scaffold.FireFlowScaffolds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardScreen(
    state: DashboardState,
    modifier: Modifier = Modifier
) {
    val topAppBarScrollBehavior = FireFlowTopAppBars.topAppBarScrollBehavior(
        ScrollBehavior.ExitUntilCollapsed
    )

    FireFlowScaffolds.Primary(
        modifier = modifier,
        topBar = {
            FireFlowTopAppBars.Collapsing.Primary(
                title = stringResource(id = R.string.dashboard),
                scrollBehavior = topAppBarScrollBehavior
            )
        }
    ) {
        DashboardScreenContent(state)
    }
}

@Suppress("ForbiddenComment")
@Composable
private fun DashboardScreenContent(
    state: DashboardState
) {
    // TODO
}
