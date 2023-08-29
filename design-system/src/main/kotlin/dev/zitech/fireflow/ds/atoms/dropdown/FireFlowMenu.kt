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

package dev.zitech.fireflow.ds.atoms.dropdown

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.fireflow.ds.atoms.text.FireFlowTexts
import dev.zitech.fireflow.ds.theme.FireFlowTheme
import dev.zitech.fireflow.ds.theme.PreviewFireFlowTheme

object FireFlowMenu {

    @Composable
    fun DropDown(
        expanded: Boolean,
        items: List<DropDownMenuItem>,
        onDismiss: () -> Unit,
        onItemClick: (id: Int) -> Unit,
        modifier: Modifier = Modifier
    ) {
        DropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            items.forEach { dropDownItem ->
                DropdownMenuItem(
                    text = {
                        FireFlowTexts.TitleSmall(
                            modifier = Modifier.padding(FireFlowTheme.space.s),
                            text = dropDownItem.text
                        )
                    },
                    onClick = { onItemClick(dropDownItem.id) }
                )
            }
        }
    }
}

@Preview(
    name = "Menu DropDown Light Theme",
    showBackground = true
)
@Preview(
    name = "Menu DropDown Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun FireFlowMenu_DropDown() {
    PreviewFireFlowTheme {
        FireFlowMenu.DropDown(
            expanded = true,
            items = listOf(
                DropDownMenuItem(
                    id = 1,
                    text = "First Drop Down Item"
                ),
                DropDownMenuItem(
                    id = 2,
                    text = "Second Drop Down Item"
                )
            ),
            onDismiss = {},
            onItemClick = {}
        )
    }
}
