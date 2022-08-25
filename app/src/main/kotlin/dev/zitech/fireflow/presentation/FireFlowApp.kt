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

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.ds.atoms.icon.Icon
import dev.zitech.ds.atoms.navigation.FireFlowNavigationBar
import dev.zitech.ds.atoms.navigation.FireFlowNavigationBarItem.Simple
import dev.zitech.ds.atoms.navigation.FireFlowNavigationRail
import dev.zitech.ds.atoms.navigation.FireFlowNavigationRailItem.Simple
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.fireflow.presentation.navigation.FireFlowNavHost
import dev.zitech.fireflow.presentation.navigation.TopLevelDestination

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
internal fun FireFlowApp(
    splash: Boolean,
    theme: ApplicationTheme?,
    windowSizeClass: WindowSizeClass,
    appState: FireFlowAppState = rememberFireFlowAppState(windowSizeClass)
) {
    FireFlowTheme(
        darkTheme = isDarkTheme(theme)
    ) {
        Scaffold(
            containerColor = FireFlowTheme.colors.background,
            contentColor = FireFlowTheme.colors.background,
            bottomBar = {
                if (!splash && appState.shouldShowBottomBar) {
                    FireFlowBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigate,
                        currentDestination = appState.currentDestination
                    )
                }
            }
        ) { padding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal
                        )
                    )
            ) {
                if (!splash) {
                    if (appState.shouldShowNavRail) {
                        FireFlowBottomBarNavRail(
                            destinations = appState.topLevelDestinations,
                            onNavigateToDestination = appState::navigate,
                            currentDestination = appState.currentDestination
                        )
                    }

                    FireFlowNavHost(
                        navController = appState.navController,
                        onNavigateToDestination = appState::navigate,
                        onBackClick = appState::onBackClick,
                        modifier = Modifier
                            .padding(padding)
                            .consumedWindowInsets(padding)
                    )
                }
            }
        }
    }
}

@Composable
private fun FireFlowBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    // Wrap the navigation bar in a surface so the color behind the system
    // navigation is equal to the container color of the navigation bar.
    Surface(
        color = FireFlowTheme.colors.surface
    ) {
        FireFlowNavigationBar.Simple(
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            )
        ) {
            destinations.forEach { destination ->
                val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                Simple(
                    selected = selected,
                    onClick = { onNavigateToDestination(destination) },
                    icon = {
                        val icon = if (selected) {
                            destination.selectedIcon
                        } else {
                            destination.unselectedIcon
                        }
                        when (icon) {
                            is Icon.ImageVectorIcon -> Icon(
                                imageVector = icon.imageVector,
                                contentDescription = null
                            )
                            is Icon.DrawableResourceIcon -> Icon(
                                painter = painterResource(id = icon.id),
                                contentDescription = null
                            )
                        }
                    },
                    label = {
                        FireFlowTexts.TitleSmall(
                            text = stringResource(id = destination.iconTextId)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun FireFlowBottomBarNavRail(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    FireFlowNavigationRail.Simple(modifier = modifier) {
        destinations.forEach { destination ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == destination.route } == true
            Simple(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }
                    when (icon) {
                        is Icon.ImageVectorIcon -> Icon(
                            imageVector = icon.imageVector,
                            contentDescription = null
                        )
                        is Icon.DrawableResourceIcon -> Icon(
                            painter = painterResource(id = icon.id),
                            contentDescription = null
                        )
                    }
                },
                label = {
                    FireFlowTexts.TitleSmall(
                        text = stringResource(id = destination.iconTextId)
                    )
                }
            )
        }
    }
}

@Composable
private fun isDarkTheme(applicationTheme: ApplicationTheme?): Boolean =
    when (applicationTheme) {
        ApplicationTheme.DARK -> true
        ApplicationTheme.LIGHT -> false
        else -> isSystemInDarkTheme()
    }
