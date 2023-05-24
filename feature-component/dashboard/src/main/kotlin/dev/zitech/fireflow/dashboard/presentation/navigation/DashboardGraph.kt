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

package dev.zitech.fireflow.dashboard.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.zitech.fireflow.dashboard.presentation.dashboard.compose.DashboardRoute
import dev.zitech.fireflow.core.error.Error

fun NavGraphBuilder.dashboardGraph(
    navigateToAccounts: () -> Unit,
    navigateToError: (error: Error) -> Unit,
    navigateToWelcome: () -> Unit
) {
    composable(route = DashboardDestination.route) {
        DashboardRoute(
            navigateToAccounts = navigateToAccounts,
            navigateToError = navigateToError,
            navigateToWelcome = navigateToWelcome
        )
    }
}
