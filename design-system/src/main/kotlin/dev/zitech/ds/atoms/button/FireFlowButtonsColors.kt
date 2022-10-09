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

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import dev.zitech.ds.theme.FireFlowTheme

internal object FireFlowButtonsColors {

    val primary: ButtonColors
        @Composable
        get() = ButtonDefaults.textButtonColors(
            containerColor = FireFlowTheme.colors.primary,
            contentColor = FireFlowTheme.colors.inversePrimary,
            disabledContainerColor = FireFlowTheme.colors.inversePrimary,
            disabledContentColor = FireFlowTheme.colors.inverseOnSurface
        )
}
