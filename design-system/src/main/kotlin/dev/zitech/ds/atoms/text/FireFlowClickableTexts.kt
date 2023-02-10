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

import android.content.res.Configuration
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.ds.theme.PreviewFireFlowTheme

object FireFlowClickableTexts {

    @Composable
    fun DisplayLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.displayLarge.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun DisplayMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.displayMedium.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun DisplaySmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.displaySmall.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun HeadlineLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.headlineLarge.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun HeadlineMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.headlineMedium.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun HeadlineSmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.headlineSmall.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun TitleLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.titleLarge.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun TitleMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.titleMedium.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun TitleSmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.titleSmall.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun BodyLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.bodyLarge.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun BodyMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.bodyMedium.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun BodySmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.bodySmall.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun LabelLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.labelLarge.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun LabelMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.labelMedium.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }

    @Composable
    fun LabelSmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign = TextAlign.Start,
        onClick: (Int) -> Unit
    ) {
        ClickableText(
            modifier = modifier,
            text = text,
            style = FireFlowTheme.typography.labelSmall.copy(
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            onClick = onClick
        )
    }
}

@Preview(
    name = "Display Large Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Display Large Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Display_Large_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.DisplayLarge(
            text = AnnotatedString("Display Large Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Display Medium Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Display Medium Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Display_Medium_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.DisplayMedium(
            text = AnnotatedString("Display Medium Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Display Small Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Display Small Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Display_Small_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.DisplaySmall(
            text = AnnotatedString("Display Small Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Headline Large Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Headline Large Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Headline_Large_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.HeadlineLarge(
            text = AnnotatedString("Headline Large Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Headline Medium Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Headline Medium Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Headline_Medium_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.HeadlineMedium(
            text = AnnotatedString("Headline Medium Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Headline Small Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Headline Small Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Headline_Small_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.HeadlineSmall(
            text = AnnotatedString("Headline Small Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Title Large Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Title Large Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Title_Large_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.TitleLarge(
            text = AnnotatedString("Title Large Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Title Medium Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Title Medium Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Title_Medium_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.TitleMedium(
            text = AnnotatedString("Title Medium Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Title Small Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Title Small Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Title_Small_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.TitleSmall(
            text = AnnotatedString("Title Small Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Body Large Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Body Large Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Body_Large_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.BodyLarge(
            text = AnnotatedString("Body Large Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Body Medium Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Body Medium Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Body_Medium_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.BodyMedium(
            text = AnnotatedString("Body Medium Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Body Small Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Body Small Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Body_Small_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.BodySmall(
            text = AnnotatedString("Body Small Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Label Large Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Label Large Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Label_Large_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.LabelLarge(
            text = AnnotatedString("Label Large Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Label Medium Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Label Medium Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Label_Medium_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.LabelMedium(
            text = AnnotatedString("Label Medium Text"),
            onClick = {}
        )
    }
}

@Preview(
    name = "Label Small Text Light Theme",
    showBackground = true
)
@Preview(
    name = "Label Small Text Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Label_Small_Preview() {
    PreviewFireFlowTheme {
        FireFlowClickableTexts.LabelSmall(
            text = AnnotatedString("Label Small Text"),
            onClick = {}
        )
    }
}
