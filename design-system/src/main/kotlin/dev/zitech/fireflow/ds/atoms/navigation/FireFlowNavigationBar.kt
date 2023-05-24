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

package dev.zitech.fireflow.ds.atoms.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

object FireFlowNavigationBar {

    @Composable
    fun Primary(
        modifier: Modifier = Modifier,
        content: @Composable RowScope.() -> Unit
    ) {
        NavigationBar(
            modifier = modifier,
            contentColor = FireFlowNavigationColors.navigationContentColor(),
            tonalElevation = 0.dp,
            content = content,
            // Return 0 for each space, since it is already provided by the FireFlowApp Scaffold, because
            // of the connectivity FireFlowErrors
            windowInsets = object : WindowInsets {
                override fun getBottom(density: Density): Int = 0

                override fun getLeft(density: Density, layoutDirection: LayoutDirection): Int = 0

                override fun getRight(density: Density, layoutDirection: LayoutDirection): Int = 0

                override fun getTop(density: Density): Int = 0
            }
        )
    }
}
