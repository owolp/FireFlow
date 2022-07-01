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

package dev.zitech.ds.atoms.spacer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.zitech.ds.theme.FireFlowTheme

object FireFlowSpacers {

    @Composable
    fun Horizontal(
        horizontalSpace: Dp,
        modifier: Modifier = Modifier
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
        verticalSpace: Dp,
        modifier: Modifier = Modifier
    ) {
        Spacer(
            modifier = modifier.size(
                width = 0.dp,
                height = verticalSpace
            )
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
private fun HorizontalPreview() {
    FireFlowTheme {
        FireFlowSpacers.Horizontal(10.dp)
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
private fun VerticalPreview() {
    FireFlowTheme {
        FireFlowSpacers.Vertical(10.dp)
    }
}
