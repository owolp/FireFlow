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

package dev.zitech.fireflow.ds.atoms.divider

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.zitech.fireflow.ds.theme.FireFlowTheme
import dev.zitech.fireflow.ds.theme.PreviewFireFlowTheme

object FireFlowDividers {

    @Composable
    fun Primary(
        modifier: Modifier = Modifier,
        color: Color = FireFlowTheme.colors.secondary,
        startIndent: Dp = 0.dp,
        endIndent: Dp = 0.dp
    ) {
        Divider(
            modifier = modifier.padding(
                start = startIndent,
                end = endIndent
            ),
            color = color,
            thickness = 1.dp
        )
    }
}

@Preview(
    name = "Primary Light Theme",
    showBackground = true
)
@Preview(
    name = "Primary Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Primary_Preview() {
    PreviewFireFlowTheme {
        FireFlowDividers.Primary()
    }
}
