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

package dev.zitech.ds.molecules.expandable.card

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.atoms.card.FireFlowCards
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.ds.theme.PreviewFireFlowTheme

object FireFlowExpandableCards {

    object Transparent {

        @Composable
        fun ExtraLarge(
            expanded: Boolean,
            modifier: Modifier = Modifier,
            onClick: () -> Unit,
            topContent: @Composable RowScope.() -> Unit,
            bottomContent: @Composable () -> Unit
        ) {
            FireFlowCards.Transparent.ExtraLarge(
                modifier = modifier
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    ),
                onClick = onClick
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(FireFlowTheme.space.s),
                    verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.s)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        content = topContent
                    )
                    if (expanded) {
                        bottomContent()
                    }
                }
            }
        }
    }
}

@Preview(
    name = "ExpandableCard Transparent ExtraLarge Expanded Light Theme",
    showBackground = true
)
@Preview(
    name = "ExpandableCard Transparent ExtraLarge Expanded Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun ExpandableCard_Transparent_ExtraLarge_Expanded_Preview() {
    PreviewFireFlowTheme {
        FireFlowExpandableCards.Transparent.ExtraLarge(
            expanded = true,
            onClick = {},
            topContent = {
                Row {
                    FireFlowTexts.BodyMedium(text = "Top Content")
                }
            },
            bottomContent = {
                FireFlowTexts.BodyMedium(text = "Bottom Content")
            }
        )
    }
}

@Preview(
    name = "ExpandableCard Transparent ExtraLarge NotExpanded Light Theme",
    showBackground = true
)
@Preview(
    name = "ExpandableCard Transparent ExtraLarge NotExpanded Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun ExpandableCard_Transparent_ExtraLarge_NotExpanded_Preview() {
    PreviewFireFlowTheme {
        FireFlowExpandableCards.Transparent.ExtraLarge(
            expanded = false,
            onClick = {},
            topContent = {
                Row {
                    FireFlowTexts.BodyMedium(text = "Top Content")
                }
            },
            bottomContent = {
                FireFlowTexts.BodyMedium(text = "Bottom Content")
            }
        )
    }
}
