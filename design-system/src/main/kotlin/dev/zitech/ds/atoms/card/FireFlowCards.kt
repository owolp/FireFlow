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

package dev.zitech.ds.atoms.card

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.ds.theme.PreviewFireFlowTheme

@OptIn(ExperimentalMaterial3Api::class)
object FireFlowCards {

    object Default {

        @Composable
        fun ExtraLarge(
            modifier: Modifier = Modifier,
            cardColors: CardColors = CardDefaults.cardColors(),
            onClick: (() -> Unit)? = null,
            content: @Composable ColumnScope.() -> Unit
        ) {
            Card(
                modifier = modifier,
                shape = FireFlowTheme.shapes.extraLarge,
                onClick = { onClick?.invoke() },
                colors = cardColors,
                content = content
            )
        }
    }

    object Elevated {

        @Composable
        fun ExtraLarge(
            modifier: Modifier = Modifier,
            cardColors: CardColors = CardDefaults.elevatedCardColors(),
            onClick: (() -> Unit)? = null,
            content: @Composable ColumnScope.() -> Unit
        ) {
            Card(
                modifier = modifier,
                shape = FireFlowTheme.shapes.extraLarge,
                onClick = { onClick?.invoke() },
                colors = cardColors,
                content = content
            )
        }
    }

    object Outlined {

        @Composable
        fun ExtraLarge(
            modifier: Modifier = Modifier,
            cardColors: CardColors = CardDefaults.outlinedCardColors(),
            onClick: (() -> Unit)? = null,
            content: @Composable ColumnScope.() -> Unit
        ) {
            Card(
                modifier = modifier,
                shape = FireFlowTheme.shapes.extraLarge,
                onClick = { onClick?.invoke() },
                colors = cardColors,
                content = content
            )
        }
    }

    object Transparent {

        @Composable
        fun ExtraLarge(
            modifier: Modifier = Modifier,
            cardColors: CardColors = CardDefaults.outlinedCardColors(
                containerColor = Color.Transparent
            ),
            onClick: (() -> Unit)? = null,
            content: @Composable ColumnScope.() -> Unit
        ) {
            Card(
                modifier = modifier,
                shape = FireFlowTheme.shapes.extraLarge,
                onClick = { onClick?.invoke() },
                colors = cardColors,
                border = BorderStroke(1.dp, FireFlowTheme.colors.surfaceVariant),
                content = content
            )
        }
    }
}

@Preview(
    name = "Card Default ExtraLarge Light Theme",
    showBackground = true
)
@Preview(
    name = "Card Default ExtraLarge Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Card_Default_ExtraLarge_Preview() {
    PreviewFireFlowTheme {
        FireFlowCards.Default.ExtraLarge(
            onClick = {}
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                FireFlowTexts.BodyLarge(text = "Card_Default_ExtraLarge_Preview")
            }
        }
    }
}

@Preview(
    name = "Card Elevated ExtraLarge Light Theme",
    showBackground = true
)
@Preview(
    name = "Card Elevated ExtraLarge Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Card_Elevated_ExtraLarge_Preview() {
    PreviewFireFlowTheme {
        FireFlowCards.Elevated.ExtraLarge(
            onClick = {}
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                FireFlowTexts.BodyLarge(text = "Card_Elevated_ExtraLarge_Preview")
            }
        }
    }
}

@Preview(
    name = "Card Outlined ExtraLarge Light Theme",
    showBackground = true
)
@Preview(
    name = "Card Outlined ExtraLarge Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Card_Outlined_ExtraLarge_Preview() {
    PreviewFireFlowTheme {
        FireFlowCards.Outlined.ExtraLarge(
            onClick = {}
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                FireFlowTexts.BodyLarge(text = "Card_Outlined_ExtraLarge_Preview")
            }
        }
    }
}

@Preview(
    name = "Card Transparent ExtraLarge Light Theme",
    showBackground = true
)
@Preview(
    name = "Card Transparent ExtraLarge Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Card_Transparent_ExtraLarge_Preview() {
    PreviewFireFlowTheme {
        FireFlowCards.Transparent.ExtraLarge(
            onClick = {}
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                FireFlowTexts.BodyLarge(text = "Card_Transparent_ExtraLarge_Preview")
            }
        }
    }
}
