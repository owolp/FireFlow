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

package dev.zitech.ds.atoms.spacer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.zitech.ds.theme.PreviewFireFlowTheme

object FireFlowSpacers {

    @Composable
    fun Horizontal(
        modifier: Modifier = Modifier,
        horizontalSpace: Dp = 0.dp
    ) {
        Spacer(
            modifier = modifier.size(
                width = horizontalSpace,
                height = 0.dp
            )
        )
    }

    @Composable
    fun Vertical(
        modifier: Modifier = Modifier,
        verticalSpace: Dp = 0.dp
    ) {
        Spacer(
            modifier = modifier.size(
                width = 0.dp,
                height = verticalSpace
            )
        )
    }

    @Composable
    fun Square(
        size: Dp,
        modifier: Modifier = Modifier
    ) {
        Spacer(
            modifier = modifier.size(size)
        )
    }
}

@Preview(
    name = "Horizontal Light Theme",
    showBackground = true
)
@Preview(
    name = "Horizontal Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Horizontal_Preview() {
    PreviewFireFlowTheme {
        FireFlowSpacers.Horizontal(horizontalSpace = 10.dp)
    }
}

@Preview(
    name = "Vertical Light Theme",
    showBackground = true
)
@Preview(
    name = "Vertical Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Vertical_Preview() {
    PreviewFireFlowTheme {
        FireFlowSpacers.Vertical(verticalSpace = 10.dp)
    }
}

@Preview(
    name = "Square Light Theme",
    showBackground = true
)
@Preview(
    name = "Square Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Square_Preview() {
    PreviewFireFlowTheme {
        FireFlowSpacers.Square(10.dp)
    }
}
