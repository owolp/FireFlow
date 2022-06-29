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

package dev.zitech.ds.atoms.switch

import android.content.res.Configuration
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.theme.FireFlowTheme

object FireFlowSwitches {

    @Composable
    fun Primary(
        checked: Boolean,
        modifier: Modifier = Modifier,
        onCheckedChange: ((Boolean) -> Unit)? = null,
        enabled: Boolean = true
    ) {
        Switch(
            checked = checked,
            modifier = modifier,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = FireFlowSwitchesColors.primary
        )
    }
}

@Preview(
    name = "Primary Checked Enabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Primary Checked Enabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun PrimaryCheckedEnabledPreview() {
    FireFlowTheme {
        FireFlowSwitches.Primary(
            checked = true
        )
    }
}

@Preview(
    name = "Primary Checked Disabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Primary Checked Disabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun PrimaryCheckedDisabledPreview() {
    FireFlowTheme {
        FireFlowSwitches.Primary(
            checked = true,
            enabled = false
        )
    }
}

@Preview(
    name = "Primary Unchecked Enabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Primary Unchecked Enabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun PrimaryUncheckedEnabledPreview() {
    FireFlowTheme {
        FireFlowSwitches.Primary(
            checked = false
        )
    }
}

@Preview(
    name = "Primary Unchecked Disabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Primary Unchecked Disabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun PrimaryUncheckedDisabledPreview() {
    FireFlowTheme {
        FireFlowSwitches.Primary(
            checked = false,
            enabled = false
        )
    }
}
