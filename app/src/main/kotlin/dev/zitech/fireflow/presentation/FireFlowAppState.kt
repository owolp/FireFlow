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
import com.jakewharton.processphoenix.ProcessPhoenix
import dev.zitech.dashboard.fireflow.presentation.navigation.DashboardDestination
import dev.zitech.ds.atoms.icon.FireFlowIcons
import dev.zitech.ds.atoms.icon.Icon
import dev.zitech.fireflow.common.presentation.navigation.destination.FireFlowNavigationDestination
import dev.zitech.fireflow.common.presentation.navigation.destination.TopLevelDestination
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.dashboard.R as dashboardR
import dev.zitech.fireflow.presentation.navigation.NavDirection.Companion.DEFAULT_STATE_INCLUSIVE
import dev.zitech.fireflow.presentation.navigation.NavDirection.Companion.DEFAULT_STATE_RESTORE_STATE
import dev.zitech.settings.R as settingsR
import dev.zitech.settings.presentation.navigation.SettingsDestination

@Composable
internal fun rememberFireFlowAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController,
    splash: Boolean
): FireFlowAppState = remember(navController, splash, windowSizeClass) {
    FireFlowAppState(navController, splash, windowSizeClass)
}

@Stable
internal class FireFlowAppState(
    val navController: NavHostController,
    private val splash: Boolean,
    private val windowSizeClass: WindowSizeClass
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        @Composable get() = (
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
            ) && isCurrentDestinationTopLevelDestination() && !splash

    val shouldShowNavRail: Boolean
        @Composable get() = !(
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
            ) && isCurrentDestinationTopLevelDestination() && !splash

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

    fun closeApplication() {
        (navController.context as? AppCompatActivity)?.finish()
    }

    fun goBack(
        destination: FireFlowNavigationDestination?,
        inclusive: Boolean? = DEFAULT_STATE_INCLUSIVE
    ) {
        if (destination != null && inclusive != null) {
            try {
                val id = navController.getBackStackEntry(destination.route).destination.id
                navController.popBackStack(id, inclusive)
            } catch (e: IllegalArgumentException) {
                Logger.e("FireFlowAppState", e)
                restartApplication()
            }
        } else {
            navController.popBackStack()
        }
    }

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

    fun restartApplication() {
        ProcessPhoenix.triggerRebirth(navController.context)
    }

    @Composable
    private fun isCurrentDestinationTopLevelDestination(): Boolean =
        topLevelDestinations.any { it.route == currentDestination?.route }
}
