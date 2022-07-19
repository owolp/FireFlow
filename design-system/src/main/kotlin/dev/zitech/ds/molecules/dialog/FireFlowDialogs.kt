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

package dev.zitech.ds.molecules.dialog

import android.content.res.Configuration
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.R
import dev.zitech.ds.atoms.button.FireFlowButtons
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.theme.FireFlowTheme

object FireFlowDialogs {

    @Composable
    fun Alert(
        title: String,
        text: String,
        onConfirmButtonClick: () -> Unit,
        modifier: Modifier = Modifier,
        confirmButton: String = stringResource(id = R.string.dialog_button_ok),
        onDismissRequest: (() -> Unit)? = null
    ) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = { onDismissRequest?.invoke() },
            confirmButton = {
                FireFlowButtons.Text(
                    text = confirmButton
                ) { onConfirmButtonClick() }
            },
            title = {
                FireFlowTexts.TitleMedium(
                    text = title
                )
            },
            text = {
                FireFlowTexts.BodyMedium(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    text = text
                )
            }
        )
    }
}

@Preview(
    name = "Alert Light Theme",
    showBackground = true
)
@Preview(
    name = "Alert Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Alert_Preview() {
    FireFlowTheme {
        FireFlowDialogs.Alert(
            title = "Alert Title",
            text = "Alert Text",
            onConfirmButtonClick = {}
        )
    }
}
