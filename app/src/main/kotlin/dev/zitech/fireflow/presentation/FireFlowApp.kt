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

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.fireflow.presentation.navigation.FireFlowNavHost

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FireFlowTheme.colors.background),
            contentAlignment = Alignment.TopCenter
        ) {
            if (!splash) {
                FireFlowNavHost(
                    navController = appState.navController,
                    onNavigateToDestination = appState::navigate,
                    onBackClick = appState::onBackClick
                )
            }
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
