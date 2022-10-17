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

package dev.zitech.fireflow.presentation

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.zitech.dashboard.presentation.navigation.DashboardDestinations
import dev.zitech.ds.atoms.icon.FireFlowIcons
import dev.zitech.ds.atoms.icon.Icon
import dev.zitech.fireflow.presentation.navigation.TopLevelDestination
import dev.zitech.navigation.FireFlowNavigationDestination
import dev.zitech.settings.presentation.navigation.SettingsDestination
import dev.zitech.dashboard.R as dashboardR
import dev.zitech.settings.R as settingsR

@Composable
internal fun rememberFireFlowAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController()
): FireFlowAppState = remember(navController, windowSizeClass) {
    FireFlowAppState(navController, windowSizeClass)
}

@Stable
internal class FireFlowAppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
            windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    val topLevelDestinations: List<TopLevelDestination> = listOf(
        TopLevelDestination(
            route = DashboardDestinations.route,
            destination = DashboardDestinations.destination,
            selectedIcon = Icon.ImageVectorIcon(FireFlowIcons.Dashboard),
            unselectedIcon = Icon.ImageVectorIcon(FireFlowIcons.Dashboard),
            iconTextId = dashboardR.string.dashboard
        ),
        TopLevelDestination(
            route = SettingsDestination.route,
            destination = SettingsDestination.destination,
            selectedIcon = Icon.ImageVectorIcon(FireFlowIcons.Settings),
            unselectedIcon = Icon.ImageVectorIcon(FireFlowIcons.Settings),
            iconTextId = settingsR.string.settings
        )
    )

    fun navigate(destination: FireFlowNavigationDestination, route: String? = null) {
        if (destination is TopLevelDestination) {
            navController.navigate(route ?: destination.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        } else {
            navController.navigate(route ?: destination.route)
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }
}
