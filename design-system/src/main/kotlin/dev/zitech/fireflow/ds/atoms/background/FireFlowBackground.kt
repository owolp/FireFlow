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

package dev.zitech.fireflow.ds.atoms.background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import dev.zitech.fireflow.ds.theme.FireFlowTheme

object FireFlowBackground {

    @Composable
    fun Primary(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        Surface(
            color = FireFlowTheme.colors.surface,
            tonalElevation = 2.dp,
            modifier = modifier
        ) {
            CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
                content()
            }
        }
    }

    @Composable
    fun Gradient(
        modifier: Modifier = Modifier,
        content: @Composable BoxScope.() -> Unit
    ) {
        Box(
            modifier = modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            FireFlowTheme.colors.primary,
                            FireFlowTheme.colors.surface
                        )
                    )
                )
        ) {
            CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
                content()
            }
        }
    }
}
