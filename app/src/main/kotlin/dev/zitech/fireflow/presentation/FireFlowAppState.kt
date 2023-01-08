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

import androidx.appcompat.app.AppCompatActivity
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
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.dashboard.R as dashboardR
import dev.zitech.dashboard.presentation.navigation.DashboardDestination
import dev.zitech.ds.atoms.icon.FireFlowIcons
import dev.zitech.ds.atoms.icon.Icon
import dev.zitech.fireflow.presentation.navigation.NavDirection.Companion.DEFAULT_STATE_INCLUSIVE
import dev.zitech.fireflow.presentation.navigation.NavDirection.Companion.DEFAULT_STATE_RESTORE_STATE
import dev.zitech.navigation.presentation.model.FireFlowNavigationDestination
import dev.zitech.navigation.presentation.model.TopLevelDestination
import dev.zitech.settings.R as settingsR
import dev.zitech.settings.presentation.navigation.SettingsDestination

@Composable
internal fun rememberFireFlowAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController
): FireFlowAppState = remember(navController, windowSizeClass) {
    FireFlowAppState(navController, windowSizeClass)
}

@Stable
internal class FireFlowAppState(
    val navController: NavHostController,
    private val windowSizeClass: WindowSizeClass
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        @Composable get() = (
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
            ) && isCurrentDestinationTopLevelDestination()

    val shouldShowNavRail: Boolean
        @Composable get() = !(
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
            ) && isCurrentDestinationTopLevelDestination()

    val topLevelDestinations: List<TopLevelDestination> = listOf(
        TopLevelDestination(
            route = DashboardDestination.route,
            destination = DashboardDestination.destination,
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

    fun navigate(
        destination: FireFlowNavigationDestination,
        route: String? = null,
        inclusive: Boolean = DEFAULT_STATE_INCLUSIVE,
        popUpToDestination: FireFlowNavigationDestination? = null,
        restoreState: Boolean = DEFAULT_STATE_RESTORE_STATE
    ) {
        if (destination is TopLevelDestination) {
            navController.navigate(route ?: destination.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                    this.inclusive = inclusive
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                this.restoreState = restoreState
            }
        } else {
            val navRoute = route ?: destination.route
            navController.navigate(navRoute) {
                popUpTo(popUpToDestination?.route ?: navRoute) {
                    this.inclusive = inclusive
                }
            }
        }
    }

    fun onBackClick(
        destination: FireFlowNavigationDestination?,
        inclusive: Boolean? = DEFAULT_STATE_INCLUSIVE
    ) {
        if (destination != null && inclusive != null) {
            try {
                val id = navController.getBackStackEntry(destination.route).destination.id
                navController.popBackStack(id, inclusive)
            } catch (e: IllegalArgumentException) {
                Logger.e("FireFlowAppState", e)
                navController.popBackStack()
            }
        } else {
            navController.popBackStack()
        }
    }

    fun onCloseApplication() {
        (navController.context as? AppCompatActivity)?.finish()
    }

    @Composable
    private fun isCurrentDestinationTopLevelDestination(): Boolean =
        topLevelDestinations.any { it.route == currentDestination?.route }
}
