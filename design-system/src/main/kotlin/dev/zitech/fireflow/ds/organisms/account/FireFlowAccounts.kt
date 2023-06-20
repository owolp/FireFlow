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

package dev.zitech.fireflow.ds.organisms.account

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zitech.fireflow.ds.R
import dev.zitech.fireflow.ds.atoms.button.FireFlowButtons
import dev.zitech.fireflow.ds.atoms.dropdown.DropDownMenuItem
import dev.zitech.fireflow.ds.atoms.dropdown.FireFlowMenu
import dev.zitech.fireflow.ds.atoms.icon.FireFlowIcons
import dev.zitech.fireflow.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.fireflow.ds.atoms.text.FireFlowTexts
import dev.zitech.fireflow.ds.theme.FireFlowTheme
import dev.zitech.fireflow.ds.theme.PreviewFireFlowTheme

object FireFlowAccounts {

    @Composable
    fun Primary(
        initial: Char,
        topInfo: String,
        bottomInfo: String?,
        isLogged: Boolean,
        menuItems: List<DropDownMenuItem>,
        more: Boolean,
        onMoreItemClick: (id: Int) -> Unit,
        onMoreClick: () -> Unit,
        onMoreDismiss: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier
                .height(64.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(48.dp)
            ) {
                FireFlowTexts.TitleLarge(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = FireFlowTheme.colors.surfaceVariant,
                            shape = CircleShape
                        )
                        .wrapContentHeight(),
                    text = initial.toString(),
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = FireFlowTheme.colors.surfaceTint
                )
                if (isLogged) {
                    FireFlowButtons.Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(FireFlowTheme.colors.primaryContainer)
                            .align(Alignment.BottomEnd)
                            .size(16.dp),
                        image = FireFlowIcons.Check,
                        contentDescription = stringResource(R.string.cd_account_profile_identification),
                        onClick = {}
                    )
                }
            }
            FireFlowSpacers.Horizontal(horizontalSpace = FireFlowTheme.space.m)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1.0F),
                verticalArrangement = Arrangement.Center
            ) {
                FireFlowTexts.TitleMedium(
                    text = topInfo,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.xss)
                if (bottomInfo != null) {
                    FireFlowTexts.TitleSmall(
                        text = bottomInfo,
                        maxLines = 1
                    )
                }
            }
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                FireFlowButtons.Icon(
                    modifier = Modifier.fillMaxHeight(),
                    image = FireFlowIcons.MoreVert,
                    contentDescription = stringResource(R.string.cd_account_more),
                    onClick = onMoreClick
                )
                FireFlowMenu.DropDown(
                    expanded = more,
                    items = menuItems,
                    onDismiss = onMoreDismiss,
                    onItemClick = onMoreItemClick
                )
            }
        }
    }
}

@Preview(
    name = "Accounts Light Theme",
    showBackground = true
)
@Preview(
    name = "Accounts Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Accounts_Primary_Preview() {
    PreviewFireFlowTheme {
        FireFlowAccounts.Primary(
            initial = 'Z',
            topInfo = "someone@mail.dev",
            bottomInfo = "http://192.168.1.1",
            isLogged = true,
            more = true,
            menuItems = listOf(
                DropDownMenuItem(
                    id = 1,
                    text = "First Drop Down Item"
                ),
                DropDownMenuItem(
                    id = 2,
                    text = "Second Drop Down Item"
                )
            ),
            onMoreItemClick = { _ -> },
            onMoreClick = { },
            onMoreDismiss = { }
        )
    }
}
