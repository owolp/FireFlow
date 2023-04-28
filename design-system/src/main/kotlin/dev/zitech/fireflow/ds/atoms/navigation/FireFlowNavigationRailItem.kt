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

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object FireFlowNavigationRailItem {

    @Composable
    fun ColumnScope.Primary(
        selected: Boolean,
        onClick: () -> Unit,
        icon: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        selectedIcon: @Composable () -> Unit = icon,
        enabled: Boolean = true,
        label: @Composable (() -> Unit)? = null,
        alwaysShowLabel: Boolean = true
    ) {
        NavigationRailItem(
            selected = selected,
            onClick = onClick,
            icon = if (selected) selectedIcon else icon,
            modifier = modifier,
            enabled = enabled,
            label = label,
            alwaysShowLabel = alwaysShowLabel,
            colors = NavigationRailItemDefaults.colors(
                selectedIconColor = FireFlowNavigationColors.navigationSelectedItemColor(),
                unselectedIconColor = FireFlowNavigationColors.navigationContentColor(),
                selectedTextColor = FireFlowNavigationColors.navigationSelectedItemColor(),
                unselectedTextColor = FireFlowNavigationColors.navigationContentColor(),
                indicatorColor = FireFlowNavigationColors.navigationIndicatorColor()
            )
        )
    }
}
