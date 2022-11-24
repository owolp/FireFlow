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
import dev.zitech.authentication.presentation.navigation.AccountsDestination
import dev.zitech.authentication.presentation.navigation.authenticationGraph
import dev.zitech.dashboard.presentation.navigation.DashboardDestination
import dev.zitech.dashboard.presentation.navigation.dashboardGraph
import dev.zitech.onboarding.presentation.login.model.LoginType
import dev.zitech.onboarding.presentation.navigation.LoginDestination
import dev.zitech.onboarding.presentation.navigation.WelcomeDestination
import dev.zitech.onboarding.presentation.navigation.onboardingGraph
import dev.zitech.settings.presentation.navigation.settingsGraph

@Suppress("ForbiddenComment")
@Composable
internal fun FireFlowNavHost(
    navController: NavHostController,
    onNavigateToDestination: (NavDirection) -> Unit,
    onBackClick: () -> Unit,
    onCloseApplication: () -> Unit,
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
            navigateToOath = {
                onNavigateToDestination(
                    NavDirection(
                        destination = LoginDestination,
                        route = LoginDestination.createNavigationRoute(LoginType.OAUTH)
                    )
                )
            },
            navigateToPat = {
                onNavigateToDestination(
                    NavDirection(
                        destination = LoginDestination,
                        route = LoginDestination.createNavigationRoute(LoginType.PAT)
                    )
                )
            },
            navigateToDemo = {
                onBackClick()
            },
            navigateToDashboard = {
//                onNavigateToDestination(
//                    NavDirection(
//                        destination = DashboardDestination,
//                        restoreState = false,
//                        popUpToDestination = DashboardDestination
//                    )
//                )
                // TODO: Hack, otherwise when navigating in click on Settings, Click on Dashboard,
                //  goes to Onbarding. Should be fixed
                onBackClick()
                onBackClick()
            },
            navigateOutOfApp = {
                onCloseApplication()
            }
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
            navigateToError = {
                TODO()
            },
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
            navigateToError = {
                TODO()
            },
            navigateToWelcome = {
                onNavigateToDestination(
                    NavDirection(
                        destination = WelcomeDestination,
                        inclusive = true
                    )
                )
            }
        )
    }
}
