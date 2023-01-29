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

package dev.zitech.ds.atoms.text

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import dev.zitech.ds.theme.FireFlowTheme

object FireFlowClickableTexts {

    @Composable
    fun DisplayLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.displayLarge,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun DisplayMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.displayMedium,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun DisplaySmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.displaySmall,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun HeadlineLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.headlineLarge,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun HeadlineMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.headlineMedium,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun TitleLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.titleLarge,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun TitleMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.titleMedium,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun TitleSmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.titleSmall,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun HeadlineSmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.headlineSmall,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun BodyLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.bodyLarge,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun BodyMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.bodyMedium,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun BodySmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.bodySmall,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun LabelLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.labelLarge,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun LabelMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.labelMedium,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }

    @Composable
    fun LabelSmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.labelSmall,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = style.copy(color = color),
            onClick = onClick
        )
    }
}
