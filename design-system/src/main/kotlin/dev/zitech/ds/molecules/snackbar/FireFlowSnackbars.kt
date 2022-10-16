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

package dev.zitech.ds.molecules.snackbar

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.atoms.button.FireFlowButtons
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.ds.theme.PreviewFireFlowTheme

object FireFlowSnackbars {

    @Composable
    fun Primary(
        message: String,
        modifier: Modifier = Modifier,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null
    ) {
        Snackbar(
            modifier = modifier.padding(FireFlowTheme.space.s),
            action = {
                if (actionLabel != null) {
                    FireFlowButtons.Text.OnSurfaceInverse(actionLabel) {
                        onAction?.invoke()
                    }
                }
            },
            shape = FireFlowTheme.shapes.extraLarge
        ) {
            FireFlowTexts.BodyMedium(text = message)
        }
    }

    @Composable
    fun Error(
        message: String,
        modifier: Modifier = Modifier,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null
    ) {
        Snackbar(
            modifier = modifier.padding(FireFlowTheme.space.s),
            action = {
                if (actionLabel != null) {
                    FireFlowButtons.Text.OnError(actionLabel) {
                        onAction?.invoke()
                    }
                }
            },
            shape = FireFlowTheme.shapes.extraLarge,
            containerColor = FireFlowTheme.colors.errorContainer,
            contentColor = FireFlowTheme.colors.onErrorContainer
        ) {
            FireFlowTexts.BodyMedium(
                text = message
            )
        }
    }
}

@Preview(
    name = "Primary Snackbar Light Theme",
    showBackground = true
)
@Preview(
    name = "Primary Snackbar Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
internal fun Primary_Preview() {
    PreviewFireFlowTheme {
        FireFlowSnackbars.Primary(
            message = "Primary"
        )
    }
}

@Preview(
    name = "Error Snackbar Light Theme",
    showBackground = true
)
@Preview(
    name = "Error Snackbar Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
internal fun Error_Preview() {
    PreviewFireFlowTheme {
        FireFlowSnackbars.Error(
            message = "Primary"
        )
    }
}
