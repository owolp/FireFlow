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

package dev.zitech.fireflow.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.zitech.authentication.presentation.navigation.authenticationGraph
import dev.zitech.dashboard.presentation.navigation.DashboardDestination
import dev.zitech.dashboard.presentation.navigation.dashboardGraph
import dev.zitech.navigation.FireFlowNavigationDestination
import dev.zitech.settings.presentation.navigation.settingsGraph

@Composable
fun FireFlowNavHost(
    navController: NavHostController,
    onNavigateToDestination: (FireFlowNavigationDestination, String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = DashboardDestination.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        authenticationGraph()
        dashboardGraph()
        settingsGraph()
    }
}
