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

package dev.zitech.fireflow.authentication.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.zitech.fireflow.authentication.presentation.accounts.compose.AccountsRoute
import dev.zitech.fireflow.core.error.Error

fun NavGraphBuilder.authenticationGraph(
    navigateToDashboard: () -> Unit,
    navigateOutOfApp: () -> Unit,
    navigateBack: () -> Unit,
    navigateToError: (error: Error) -> Unit
) {
    composable(
        route = "${AccountsDestination.route}?" +
            "${AccountsDestination.isBackNavigationSupported}={${AccountsDestination.isBackNavigationSupported}}",
        arguments = listOf(
            navArgument(AccountsDestination.isBackNavigationSupported) {
                type = NavType.BoolType
            }
        )
    ) { navBackStackEntry ->
        val isBackNavigationSupported =
            navBackStackEntry.arguments
                ?.getBoolean(AccountsDestination.isBackNavigationSupported)
                ?: false

        AccountsRoute(
            isBackNavigationSupported = isBackNavigationSupported,
            navigateToHome = navigateToDashboard,
            navigateOutOfApp = navigateOutOfApp,
            navigateBack = navigateBack,
            navigateToError = navigateToError
        )
    }
}
