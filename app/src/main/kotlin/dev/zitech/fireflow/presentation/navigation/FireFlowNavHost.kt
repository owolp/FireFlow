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

package dev.zitech.fireflow.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.zitech.dashboard.fireflow.presentation.navigation.DashboardDestination
import dev.zitech.dashboard.fireflow.presentation.navigation.dashboardGraph
import dev.zitech.fireflow.authentication.presentation.navigation.AccountsDestination
import dev.zitech.fireflow.authentication.presentation.navigation.authenticationGraph
import dev.zitech.fireflow.onboarding.presentation.navigation.OAuthDestination
import dev.zitech.fireflow.onboarding.presentation.navigation.PatDestination
import dev.zitech.fireflow.onboarding.presentation.navigation.WelcomeDestination
import dev.zitech.fireflow.onboarding.presentation.navigation.onboardingGraph
import dev.zitech.fireflow.settings.presentation.navigation.settingsGraph

@Suppress("ForbiddenComment")
@Composable
internal fun FireFlowNavHost(
    navController: NavHostController,
    onNavigateToDestination: (NavDirection) -> Unit,
    onBackClick: (NavDirection?) -> Unit,
    onCloseApplication: () -> Unit,
    onRestartApplication: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = DashboardDestination.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        authenticationGraph(
            navigateToDashboard = {
                onNavigateToDestination(
                    NavDirection(
                        destination = DashboardDestination,
                        inclusive = true
                    )
                )
            }
        )
        onboardingGraph(
            navigateToOAuth = {
                onNavigateToDestination(
                    NavDirection(
                        destination = OAuthDestination
                    )
                )
            },
            navigateToPat = {
                onNavigateToDestination(
                    NavDirection(
                        destination = PatDestination
                    )
                )
            },
            navigateToDemo = { onBackClick(null) },
            navigateToDashboard = {
                onBackClick(
                    NavDirection(
                        destination = DashboardDestination
                    )
                )
            },
            navigateOutOfApp = onCloseApplication,
            navigateToError = { TODO() },
            navigateBack = { onBackClick(null) }
        )
        dashboardGraph(
            navigateToAccounts = {
                onNavigateToDestination(
                    NavDirection(
                        destination = AccountsDestination,
                        inclusive = true
                    )
                )
            },
            navigateToError = { TODO() },
            navigateToWelcome = {
                onNavigateToDestination(
                    NavDirection(
                        destination = WelcomeDestination,
                        inclusive = true
                    )
                )
            }
        )
        settingsGraph(
            navigateToAccounts = {
                onNavigateToDestination(
                    NavDirection(
                        destination = AccountsDestination,
                        inclusive = true
                    )
                )
            },
            navigateToError = { TODO() },
            navigateToWelcome = {
                onNavigateToDestination(
                    NavDirection(
                        destination = WelcomeDestination,
                        inclusive = true
                    )
                )
            },
            restartApplication = onRestartApplication
        )
    }
}
