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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import dev.zitech.ds.theme.PreviewFireFlowTheme

object FireFlowDialogs {

    @Composable
    fun Alert(
        text: String,
        onConfirmButtonClick: () -> Unit,
        modifier: Modifier = Modifier,
        title: String? = null,
        confirmButton: String = stringResource(R.string.dialog_button_ok),
        dismissButton: String = stringResource(R.string.dialog_button_cancel),
        onDismissRequest: (() -> Unit)? = null
    ) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = { onDismissRequest?.invoke() },
            confirmButton = {
                FireFlowButtons.Text.OnSurface(confirmButton) { onConfirmButtonClick() }
            },
            dismissButton = {
                if (onDismissRequest != null) {
                    FireFlowButtons.Text.OnSurface(dismissButton) { onDismissRequest() }
                }
            },
            title = {
                if (title != null) {
                    FireFlowTexts.TitleLarge(
                        text = title
                    )
                }
            },
            text = {
                FireFlowTexts.TitleMedium(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    text = text
                )
            }
        )
    }

    @Composable
    fun Radio(
        title: String,
        radioItems: List<DialogRadioItem>,
        onItemClick: (id: Int) -> Unit,
        modifier: Modifier = Modifier,
        onDismissRequest: (() -> Unit)? = null
    ) {
        AlertDialog(
            modifier = modifier,
            title = {
                FireFlowTexts.TitleLarge(
                    text = title
                )
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.s)
                ) {
                    items(radioItems, key = { it.id }) { item ->
                        FireFlowButtons.Radio(
                            text = item.text,
                            selected = item.selected,
                            onClick = { onItemClick(item.id) },
                            enabled = item.enabled
                        )
                    }
                }
            },
            confirmButton = {},
            onDismissRequest = { onDismissRequest?.invoke() }
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
    PreviewFireFlowTheme {
        FireFlowDialogs.Alert(
            title = "Alert Title",
            text = "Alert Text",
            onConfirmButtonClick = {}
        )
    }
}

@Preview(
    name = "Radio Light Theme",
    showBackground = true
)
@Preview(
    name = "Radio Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Radio_Preview() {
    PreviewFireFlowTheme {
        FireFlowDialogs.Radio(
            title = "Radio Title",
            radioItems = listOf(
                DialogRadioItem(
                    id = 0,
                    text = "Radio Item Selected Enabled",
                    selected = true,
                    enabled = true
                ),
                DialogRadioItem(
                    id = 1,
                    text = "Radio Item Selected Disabled",
                    selected = true,
                    enabled = false
                ),
                DialogRadioItem(
                    id = 2,
                    text = "Radio Item Not Selected Enabled",
                    selected = false,
                    enabled = true
                ),
                DialogRadioItem(
                    id = 3,
                    text = "Radio Item Not Selected Disabled",
                    selected = false,
                    enabled = false
                )
            ),
            onItemClick = {}
        )
    }
}
