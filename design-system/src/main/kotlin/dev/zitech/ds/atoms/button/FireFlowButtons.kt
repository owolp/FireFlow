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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.atoms.radio.FireFlowRadioButtons
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.theme.FireFlowTheme

private const val TEXT_NOT_ENABLED_ALPHA = 0.7F

object FireFlowButtons {

    @Composable
    fun Text(
        text: String,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        onClick: () -> Unit
    ) {
        TextButton(
            modifier = modifier,
            enabled = enabled,
            onClick = { onClick() }
        ) {
            FireFlowTexts.BodyMedium(
                text = text,
                color = if (enabled) {
                    FireFlowTheme.colors.onBackground
                } else {
                    FireFlowTheme.colors.onBackground.copy(
                        alpha = TEXT_NOT_ENABLED_ALPHA
                    )
                }
            )
        }
    }

    @Composable
    fun Radio(
        text: String,
        selected: Boolean,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        onClick: () -> Unit
    ) {
        Row(
            modifier = modifier
                .selectable(
                    selected = selected,
                    onClick = { onClick() },
                    role = Role.RadioButton,
                    enabled = enabled
                )
                .padding(FireFlowTheme.space.s)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FireFlowRadioButtons.Primary(
                selected = selected,
                onClick = null,
                enabled = enabled
            )
            FireFlowTexts.BodyLarge(
                modifier = Modifier.padding(
                    start = FireFlowTheme.space.s
                ),
                text = text,
                color = if (enabled) {
                    FireFlowTheme.colors.onBackground
                } else {
                    FireFlowTheme.colors.onBackground.copy(
                        alpha = TEXT_NOT_ENABLED_ALPHA
                    )
                }
            )
        }
    }
}

@Preview(
    name = "Text Enabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Text Enabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Text_Enabled_Preview() {
    FireFlowTheme {
        FireFlowButtons.Text(
            text = "Text",
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "Text Disabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Text Disabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Text_Disabled_Preview() {
    FireFlowTheme {
        FireFlowButtons.Text(
            text = "Text",
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(
    name = "Radio Selected Enabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Radio Selected Enabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Radio_Selected_Enabled_Preview() {
    FireFlowTheme {
        FireFlowButtons.Radio(
            text = "Selected Enabled",
            selected = true,
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "Radio Selected Disabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Radio Selected Disabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Radio_Selected_Disabled_Preview() {
    FireFlowTheme {
        FireFlowButtons.Radio(
            text = "Selected Disabled",
            selected = true,
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(
    name = "Radio Not Selected Enabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Radio Not Selected Enabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Radio_NotSelected_Enabled_Preview() {
    FireFlowTheme {
        FireFlowButtons.Radio(
            text = "Not Selected Enabled",
            selected = false,
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "Radio Not Selected Disabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Radio Not Selected Disabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Radio_NotSelected_Disabled_Preview() {
    FireFlowTheme {
        FireFlowButtons.Radio(
            text = "Not Selected Disabled",
            selected = false,
            enabled = false,
            onClick = {}
        )
    }
}
