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

package dev.zitech.fireflow.ds.molecules.error

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zitech.fireflow.ds.atoms.icon.FireFlowIcons
import dev.zitech.fireflow.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.fireflow.ds.atoms.text.FireFlowTexts
import dev.zitech.fireflow.ds.theme.FireFlowTheme
import dev.zitech.fireflow.ds.theme.PreviewFireFlowTheme

object FireFlowErrors {

    @Composable
    fun Primary(
        modifier: Modifier = Modifier,
        text: String
    ) {
        Row(
            modifier = modifier
                .background(FireFlowTheme.colors.error)
                .padding(FireFlowTheme.space.s),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(FireFlowTheme.colors.onError),
                imageVector = FireFlowIcons.Info,
                contentDescription = null
            )
            FireFlowSpacers.Horizontal(
                horizontalSpace = FireFlowTheme.space.s
            )
            FireFlowTexts.BodySmall(
                modifier = Modifier,
                text = text,
                color = FireFlowTheme.colors.onError,
                textAlign = TextAlign.Center
            )
        }
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
        FireFlowErrors.Primary(
            text = "Error Message"
        )
    }
}
