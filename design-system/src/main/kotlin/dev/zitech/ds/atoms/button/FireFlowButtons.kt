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

package dev.zitech.ds.atoms.button

import android.content.res.Configuration
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.theme.FireFlowTheme

object FireFlowButtons {

    @Composable
    fun Text(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        TextButton(
            modifier = modifier,
            onClick = { onClick() }
        ) {
            FireFlowTexts.BodyMedium(
                text = text
            )
        }
    }
}

@Preview(
    name = "Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Text_Preview() {
    FireFlowTheme {
        FireFlowButtons.Text(
            text = "Text",
            onClick = {}
        )
    }
}
