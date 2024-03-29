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

package dev.zitech.fireflow.onboarding.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.onboarding.BuildConfig
import dev.zitech.fireflow.onboarding.presentation.oauth.compose.OAuthRoute
import dev.zitech.fireflow.onboarding.presentation.oauth.model.OAuthAuthentication
import dev.zitech.fireflow.onboarding.presentation.pat.compose.PatRoute
import dev.zitech.fireflow.onboarding.presentation.welcome.compose.WelcomeRoute

fun NavGraphBuilder.onboardingGraph(
    navigateToOAuth: () -> Unit,
    navigateToPat: () -> Unit,
    navigateToDashboard: () -> Unit,
    navigateOutOfApp: () -> Unit,
    navigateToError: (error: Error) -> Unit,
    navigateBack: () -> Unit
) {
    composable(
        route = OAuthDestination.route,
        arguments = listOf(
            navArgument(OAuthDestination.code) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            },
            navArgument(OAuthDestination.state) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = BuildConfig.data_scheme +
                    "://" +
                    BuildConfig.data_host_authentication +
                    "?code={" +
                    OAuthDestination.code +
                    "}" +
                    "&state=authentication_{" +
                    OAuthDestination.state +
                    "}"
            }
        )
    ) { navBackStackEntry ->
        val code = navBackStackEntry.arguments?.getString(OAuthDestination.code)
        val state = navBackStackEntry.arguments?.getString(OAuthDestination.state)

        OAuthRoute(
            oauthAuthentication = OAuthAuthentication(code, state),
            navigateBack = navigateBack,
            navigateToError = navigateToError,
            navigateToNext = navigateToDashboard
        )
    }
    composable(route = PatDestination.route) {
        PatRoute(
            navigateToNext = navigateToDashboard,
            navigateBack = navigateBack,
            navigateToError = navigateToError
        )
    }
    composable(
        route = "${WelcomeDestination.route}?" +
            "${WelcomeDestination.isBackNavigationSupported}={${WelcomeDestination.isBackNavigationSupported}}",
        arguments = listOf(
            navArgument(WelcomeDestination.isBackNavigationSupported) {
                type = NavType.BoolType
            }
        )
    ) { navBackStackEntry ->
        val isBackNavigationSupported =
            navBackStackEntry.arguments
                ?.getBoolean(WelcomeDestination.isBackNavigationSupported)
                ?: false

        WelcomeRoute(
            isBackNavigationSupported = isBackNavigationSupported,
            navigateToOAuth = navigateToOAuth,
            navigateToPat = navigateToPat,
            navigateToNext = navigateToDashboard,
            navigateOutOfApp = navigateOutOfApp,
            navigateToError = navigateToError,
            navigateBack = navigateBack
        )
    }
}
