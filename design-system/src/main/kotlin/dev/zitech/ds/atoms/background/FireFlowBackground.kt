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

package dev.zitech.ds.atoms.background

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zitech.ds.theme.FireFlowTheme

object FireFlowBackground {

    @Composable
    fun Simple(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        Surface(
            color = FireFlowTheme.colors.surface,
            tonalElevation = 2.dp,
            modifier = modifier.fillMaxSize()
        ) {
            CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
                content()
            }
        }
    }
}
