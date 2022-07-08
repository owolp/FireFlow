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

import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import dev.zitech.ds.theme.FireFlowTheme

internal object FireFlowSwitchesColors {

    val primary: SwitchColors
        @Composable
        get() = SwitchDefaults.colors(
            checkedThumbColor = FireFlowTheme.colors.primary,
            checkedBorderColor = FireFlowTheme.colors.primary,
            checkedTrackColor = FireFlowTheme.colors.inversePrimary,
            checkedIconColor = FireFlowTheme.colors.inversePrimary,
            disabledCheckedBorderColor = FireFlowTheme.colors.primary,
            uncheckedThumbColor = FireFlowTheme.colors.secondary,
            uncheckedBorderColor = FireFlowTheme.colors.secondary,
            uncheckedTrackColor = FireFlowTheme.colors.inversePrimary,
            uncheckedIconColor = FireFlowTheme.colors.inversePrimary,
            disabledUncheckedBorderColor = FireFlowTheme.colors.secondary
        )
}
