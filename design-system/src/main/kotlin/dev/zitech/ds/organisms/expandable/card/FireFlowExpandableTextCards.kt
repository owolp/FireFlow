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

@file:Suppress("MagicNumber")

package dev.zitech.ds.organisms.expandable.card

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.atoms.button.FireFlowButtons
import dev.zitech.ds.atoms.icon.FireFlowIcons
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.molecules.expandable.card.FireFlowExpandableCards
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.ds.theme.PreviewFireFlowTheme

object FireFlowExpandableTextCards {

    object Transparent {

        @Composable
        fun Icon(
            modifier: Modifier = Modifier,
            onClick: ((isExpanded: Boolean) -> Unit)? = null,
            topContent: @Composable () -> Unit,
            bottomContent: @Composable () -> Unit
        ) {
            var expandedState by remember { mutableStateOf(false) }
            val rotationState by animateFloatAsState(
                targetValue = if (expandedState) 180F else 0F
            )

            FireFlowExpandableCards.Transparent.ExtraLarge(
                modifier = modifier,
                expanded = expandedState,
                onClick = {
                    expandedState = !expandedState
                    onClick?.invoke(expandedState)
                },
                topContent = {
                    Row(
                        modifier = Modifier
                            .padding(start = FireFlowTheme.space.s),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        topContent()
                        FireFlowButtons.Icon(
                            modifier = Modifier
                                .rotate(rotationState),
                            image = FireFlowIcons.ExpandMore,
                            contentDescription = "",
                            onClick = {
                                expandedState = !expandedState
                                onClick?.invoke(expandedState)
                            }
                        )
                    }
                },
                bottomContent = bottomContent
            )
        }

        @Composable
        fun TitleAndIcon(
            title: String,
            modifier: Modifier = Modifier,
            onClick: ((isExpanded: Boolean) -> Unit)? = null,
            bottomContent: @Composable () -> Unit
        ) {
            var expandedState by remember { mutableStateOf(false) }
            val rotationState by animateFloatAsState(
                targetValue = if (expandedState) 180F else 0F
            )

            FireFlowExpandableCards.Transparent.ExtraLarge(
                modifier = modifier,
                expanded = expandedState,
                onClick = {
                    expandedState = !expandedState
                    onClick?.invoke(expandedState)
                },
                topContent = {
                    Row(
                        modifier = Modifier
                            .padding(start = FireFlowTheme.space.s),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FireFlowTexts.BodyMedium(
                            modifier = Modifier.weight(1F),
                            text = title,
                            maxLines = 1
                        )
                        FireFlowButtons.Icon(
                            modifier = Modifier
                                .rotate(rotationState),
                            image = FireFlowIcons.ExpandMore,
                            contentDescription = "",
                            onClick = {
                                expandedState = !expandedState
                                onClick?.invoke(expandedState)
                            }
                        )
                    }
                },
                bottomContent = bottomContent
            )
        }
    }
}

@Preview(
    name = "ExpandableTextCard Transparent Icon Light Theme",
    showBackground = true
)
@Preview(
    name = "ExpandableTextCard Transparent Icon Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun ExpandableTextCard_Transparent_Icon_Preview() {
    PreviewFireFlowTheme {
        FireFlowExpandableTextCards.Transparent.Icon(
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
    name = "ExpandableTextCard Transparent TitleAndIcon Light Theme",
    showBackground = true
)
@Preview(
    name = "ExpandableTextCard Transparent TitleAndIcon Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun ExpandableTextCard_Transparent_TitleAndIcon_Preview() {
    PreviewFireFlowTheme {
        FireFlowExpandableTextCards.Transparent.TitleAndIcon(
            title = "Top Content",
            bottomContent = {
                FireFlowTexts.BodyMedium(text = "Bottom Content")
            }
        )
    }
}
