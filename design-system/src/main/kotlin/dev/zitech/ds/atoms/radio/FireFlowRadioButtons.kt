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

package dev.zitech.ds.atoms.radio

import android.content.res.Configuration
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.theme.PreviewFireFlowTheme

object FireFlowRadioButtons {

    @Composable
    fun Primary(
        selected: Boolean,
        modifier: Modifier = Modifier,
        onClick: (() -> Unit)? = null,
        enabled: Boolean = true
    ) {
        RadioButton(
            modifier = modifier,
            selected = selected,
            onClick = if (onClick != null) {
                { onClick() }
            } else {
                null
            },
            enabled = enabled
        )
    }
}

@Preview(
    name = "Primary Selected Enabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Primary Selected Enabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Primary_Selected_Enabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowRadioButtons.Primary(
            selected = true
        )
    }
}

@Preview(
    name = "Primary Selected Disabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Primary Selected Dark Disabled Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Primary_Selected_Disabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowRadioButtons.Primary(
            selected = true,
            enabled = false
        )
    }
}

@Preview(
    name = "Primary Not Selected Enabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Primary Not Selected Enabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Primary_NotSelected_Enabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowRadioButtons.Primary(
            selected = false
        )
    }
}

@Preview(
    name = "Primary Not Selected Disabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Primary Not Selected Disabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Primary_NotSelected_Disabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowRadioButtons.Primary(
            selected = false,
            enabled = false
        )
    }
}
