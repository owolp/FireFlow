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

package dev.zitech.ds.atoms.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object FireFlowNavigationRail {

    @Composable
    fun Simple(
        modifier: Modifier = Modifier,
        header: @Composable (ColumnScope.() -> Unit)? = null,
        content: @Composable ColumnScope.() -> Unit
    ) {
        NavigationRail(
            modifier = modifier,
            containerColor = FireFlowNavigationColors.navigationContainerColor(),
            header = header,
            content = content
        )
    }
}
