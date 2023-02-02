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

package dev.zitech.onboarding.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.zitech.onboarding.presentation.oauth.compose.OauthRoute
import dev.zitech.onboarding.presentation.pat.compose.PatRoute
import dev.zitech.onboarding.presentation.welcome.compose.WelcomeRoute

@Suppress("LongParameterList")
fun NavGraphBuilder.onboardingGraph(
    navigateToOath: () -> Unit,
    navigateToPat: () -> Unit,
    navigateToDemo: () -> Unit,
    navigateToDashboard: () -> Unit,
    navigateOutOfApp: () -> Unit,
    navigateToError: () -> Unit,
    navigateBack: () -> Unit
) {
    composable(route = OauthDestination.route) {
        OauthRoute(
            navigateToDashboard = navigateToDashboard,
            navigateBack = navigateBack
        )
    }
    composable(route = PatDestination.route) {
        PatRoute(
            navigateToDashboard = navigateToDashboard,
            navigateBack = navigateBack
        )
    }
    composable(route = WelcomeDestination.route) {
        WelcomeRoute(
            navigateToOath = navigateToOath,
            navigateToPat = navigateToPat,
            navigateToDemo = navigateToDemo,
            navigateOutOfApp = navigateOutOfApp,
            navigateToError = navigateToError
        )
    }
}
