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

package dev.zitech.ds.atoms.text

import android.content.res.Configuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.atoms.text.FireFlowTexts.BodyMedium
import dev.zitech.ds.atoms.text.FireFlowTexts.BodySmall
import dev.zitech.ds.atoms.text.FireFlowTexts.DisplayLarge
import dev.zitech.ds.atoms.text.FireFlowTexts.DisplayMedium
import dev.zitech.ds.atoms.text.FireFlowTexts.DisplaySmall
import dev.zitech.ds.atoms.text.FireFlowTexts.HeadlineLarge
import dev.zitech.ds.atoms.text.FireFlowTexts.HeadlineMedium
import dev.zitech.ds.atoms.text.FireFlowTexts.HeadlineSmall
import dev.zitech.ds.atoms.text.FireFlowTexts.LabelLarge
import dev.zitech.ds.atoms.text.FireFlowTexts.LabelMedium
import dev.zitech.ds.atoms.text.FireFlowTexts.LabelSmall
import dev.zitech.ds.atoms.text.FireFlowTexts.TitleLarge
import dev.zitech.ds.atoms.text.FireFlowTexts.TitleMedium
import dev.zitech.ds.atoms.text.FireFlowTexts.TitleSmall
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.ds.theme.PreviewFireFlowTheme

object FireFlowTexts {

    @Composable
    fun DisplayLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.displayLarge
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun DisplayLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.displayLarge
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun DisplayMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.displayMedium
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun DisplayMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.displayMedium
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun DisplaySmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.displaySmall
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun DisplaySmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.displaySmall
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun HeadlineLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.headlineLarge
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun HeadlineLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.headlineLarge
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun HeadlineMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.headlineMedium
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun HeadlineMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.headlineMedium
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun HeadlineSmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.headlineSmall
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun HeadlineSmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.headlineSmall
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun TitleLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.titleLarge
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun TitleLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.titleLarge
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun TitleMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.titleMedium
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun TitleMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.titleMedium
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun TitleSmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.titleSmall
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun TitleSmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.titleSmall
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun BodyLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.bodyLarge
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun BodyLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.bodyLarge
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun BodyMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.bodyMedium
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun BodyMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.bodyMedium
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun BodySmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.bodySmall
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun BodySmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.bodySmall
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun LabelLarge(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.labelLarge
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun LabelLarge(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.labelLarge
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun LabelMedium(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.labelMedium
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun LabelMedium(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.labelMedium
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun LabelSmall(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.labelSmall
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
        )
    }

    @Composable
    fun LabelSmall(
        text: AnnotatedString,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,
        style: TextStyle = FireFlowTheme.typography.labelSmall
    ) {
        Text(
            modifier = modifier,
            text = text,
            color = color,
            style = style
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
        DisplayLarge(
            text = "Display Large Text"
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
        DisplayMedium(
            text = "Display Medium Text"
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
        DisplaySmall(
            text = "Display Small Text"
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
        HeadlineLarge(
            text = "Headline Large Text"
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
        HeadlineMedium(
            text = "Headline Medium Text"
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
        HeadlineSmall(
            text = "Headline Small Text"
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
        TitleLarge(
            text = "Title Large Text"
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
        TitleMedium(
            text = "Title Medium Text"
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
        TitleSmall(
            text = "Title Small Text"
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
        FireFlowTexts.BodyLarge(
            text = "Body Large Text"
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
        BodyMedium(
            text = "Body Medium Text"
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
        BodySmall(
            text = "Body Small Text"
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
        LabelLarge(
            text = "Label Large Text"
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
        LabelMedium(
            text = "Label Medium Text"
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
        LabelSmall(
            text = "Label Small Text"
        )
    }
}
