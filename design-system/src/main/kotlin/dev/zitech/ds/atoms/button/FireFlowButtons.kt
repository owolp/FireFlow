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

package dev.zitech.ds.atoms.button

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zitech.ds.atoms.icon.FireFlowIcons
import dev.zitech.ds.atoms.loading.FireFlowProgressIndicators
import dev.zitech.ds.atoms.radio.FireFlowRadioButtons
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.ds.theme.PreviewFireFlowTheme

object FireFlowButtons {

    private val MIN_BUTTON_HEIGHT = 48.dp

    object Filled {
        @Composable
        fun OnSurfaceTint(
            text: String,
            modifier: Modifier = Modifier,
            enabled: Boolean = true,
            loading: Boolean = false,
            onClick: () -> Unit
        ) {
            Button(
                modifier = modifier.heightIn(min = MIN_BUTTON_HEIGHT),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FireFlowTheme.colors.surfaceTint
                ),
                enabled = enabled,
                shape = FireFlowTheme.shapes.extraLarge,
                onClick = { onClick() }
            ) {
                FireFlowTexts.TitleMedium(
                    text = text,
                    color = FireFlowTheme.colors.surface,
                    style = FireFlowTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )

                if (loading) {
                    FireFlowProgressIndicators.FlashingDots(
                        modifier = Modifier.padding(
                            start = FireFlowTheme.space.s
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun Icon(
        image: ImageVector,
        contentDescription: String,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        onClick: () -> Unit
    ) {
        IconButton(
            modifier = modifier.heightIn(min = MIN_BUTTON_HEIGHT),
            enabled = enabled,
            onClick = onClick
        ) {
            Icon(
                imageVector = image,
                contentDescription = contentDescription
            )
        }
    }

    object TextButton {

        @Composable
        fun OnSurface(
            text: String,
            modifier: Modifier = Modifier,
            enabled: Boolean = true,
            loading: Boolean = false,
            onClick: () -> Unit
        ) {
            TextButton(
                modifier = modifier.heightIn(min = MIN_BUTTON_HEIGHT),
                enabled = enabled,
                shape = FireFlowTheme.shapes.extraLarge,
                onClick = { onClick() }
            ) {
                FireFlowTexts.TitleMedium(
                    text = text,
                    color = FireFlowTheme.colors.onSurface,
                    style = FireFlowTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )

                if (loading) {
                    FireFlowProgressIndicators.FlashingDots(
                        modifier = Modifier.padding(
                            start = FireFlowTheme.space.s
                        )
                    )
                }
            }
        }
    }

    object Outlined {

        @Composable
        fun OnSurface(
            text: String,
            modifier: Modifier = Modifier,
            buttonColors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
            enabled: Boolean = true,
            onClick: () -> Unit
        ) {
            OutlinedButton(
                modifier = modifier.heightIn(min = MIN_BUTTON_HEIGHT),
                colors = buttonColors,
                shape = FireFlowTheme.shapes.extraLarge,
                border = BorderStroke(1.dp, FireFlowTheme.colors.onSurface),
                enabled = enabled,
                onClick = onClick
            ) {
                FireFlowTexts.TitleMedium(
                    text = text,
                    color = FireFlowTheme.colors.onSurface,
                    style = FireFlowTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }

    @Composable
    fun Radio(
        text: String,
        selected: Boolean,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        onClick: () -> Unit
    ) {
        Row(
            modifier = modifier
                .heightIn(min = MIN_BUTTON_HEIGHT)
                .selectable(
                    selected = selected,
                    onClick = { onClick() },
                    role = Role.RadioButton,
                    enabled = enabled
                )
                .padding(FireFlowTheme.space.s)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FireFlowRadioButtons.Primary(
                selected = selected,
                onClick = null,
                enabled = enabled
            )
            FireFlowTexts.TitleMedium(
                modifier = Modifier.padding(
                    start = FireFlowTheme.space.s
                ),
                text = text,
                style = FireFlowTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }

    object Text {

        @Composable
        fun OnSurface(
            text: String,
            modifier: Modifier = Modifier,
            enabled: Boolean = true,
            onClick: () -> Unit
        ) {
            TextButton(
                modifier = modifier.heightIn(min = MIN_BUTTON_HEIGHT),
                enabled = enabled,
                shape = FireFlowTheme.shapes.extraLarge,
                onClick = { onClick() }
            ) {
                FireFlowTexts.TitleMedium(
                    text = text,
                    color = FireFlowTheme.colors.onSurface,
                    style = FireFlowTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

        @Composable
        fun OnSurfaceInverse(
            text: String,
            modifier: Modifier = Modifier,
            enabled: Boolean = true,
            onClick: () -> Unit
        ) {
            TextButton(
                modifier = modifier.heightIn(min = MIN_BUTTON_HEIGHT),
                enabled = enabled,
                shape = FireFlowTheme.shapes.extraLarge,
                onClick = { onClick() }
            ) {
                FireFlowTexts.TitleMedium(
                    text = text,
                    color = FireFlowTheme.colors.inverseOnSurface,
                    style = FireFlowTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

        @Composable
        fun OnError(
            text: String,
            modifier: Modifier = Modifier,
            enabled: Boolean = true,
            onClick: () -> Unit
        ) {
            TextButton(
                modifier = modifier.heightIn(min = MIN_BUTTON_HEIGHT),
                enabled = enabled,
                shape = FireFlowTheme.shapes.extraLarge,
                onClick = { onClick() }
            ) {
                FireFlowTexts.TitleMedium(
                    text = text,
                    color = FireFlowTheme.colors.onErrorContainer,
                    style = FireFlowTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@Preview(
    name = "Filled OnSurfaceTint Button Light Theme Enabled",
    showBackground = true
)
@Preview(
    name = "Filled OnSurfaceTint Button Dark Theme Enabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Filled_OnSurfaceTint_Enabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Filled.OnSurfaceTint(
            text = "Filled OnSurfaceTint Enabled Button",
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "Filled OnSurfaceTint Button Light Theme Disabled",
    showBackground = true
)
@Preview(
    name = "Filled OnSurfaceTint Button Dark Theme Disabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Filled_OnSurfaceTint_Disabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Filled.OnSurfaceTint(
            text = "Filled OnSurfaceTint Disabled Button",
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(
    name = "Icon Button Light Theme Enabled",
    showBackground = true
)
@Preview(
    name = "Icon Button Dark Theme Enabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Icon_Enabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Icon(
            image = FireFlowIcons.ArrowBack,
            contentDescription = "",
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "Icon Button Light Theme Disabled",
    showBackground = true
)
@Preview(
    name = "Icon Button Dark Theme Disabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Icon_Disabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Icon(
            image = FireFlowIcons.ArrowBack,
            contentDescription = "",
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(
    name = "TextButton OnSurface Button Light Theme Enabled",
    showBackground = true
)
@Preview(
    name = "TextButton OnSurface Button Dark Theme Enabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun TextButton_OnSurface_Enabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.TextButton.OnSurface(
            text = "TextButton OnSurface Enabled Button",
            enabled = true,
            loading = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "TextButton OnSurface Button Light Theme Disabled",
    showBackground = true
)
@Preview(
    name = "TextButton OnSurface Button Dark Theme Disabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun TextButton_OnSurface_Disabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.TextButton.OnSurface(
            text = "TextButton OnSurface Disabled Button",
            enabled = false,
            loading = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "Outlined OnSurface Button Light Theme Enabled",
    showBackground = true
)
@Preview(
    name = "Outlined OnSurface Button Dark Theme Enabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Outlined_OnSurface_Enabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Outlined.OnSurface(
            text = "Outlined OnSurface Enabled Button",
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "Outlined OnSurface Button Light Theme Disabled",
    showBackground = true
)
@Preview(
    name = "Outlined OnSurface Button Dark Theme Disabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Outlined_OnSurface_Disabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Outlined.OnSurface(
            text = "Outlined OnSurface Disabled Button",
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(
    name = "Radio Selected Enabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Radio Selected Enabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Radio_Selected_Enabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Radio(
            text = "Selected Enabled",
            selected = true,
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "Radio Selected Disabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Radio Selected Disabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Radio_Selected_Disabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Radio(
            text = "Selected Disabled",
            selected = true,
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(
    name = "Radio Not Selected Enabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Radio Not Selected Enabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Radio_NotSelected_Enabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Radio(
            text = "Not Selected Enabled",
            selected = false,
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "Radio Not Selected Disabled Light Theme",
    showBackground = true
)
@Preview(
    name = "Radio Not Selected Disabled Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Radio_NotSelected_Disabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Radio(
            text = "Not Selected Disabled",
            selected = false,
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(
    name = "Text OnSurface Button Light Theme Disabled",
    showBackground = true
)
@Preview(
    name = "Text OnSurface Button Dark Theme Disabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Text_OnSurface_Disabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Text.OnSurface(
            text = "Text OnSurface Disabled Button",
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(
    name = "Text OnSurface Button Light Theme Enabled",
    showBackground = true
)
@Preview(
    name = "Text OnSurface Button Dark Theme Enabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Text_OnSurface_Enabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Text.OnSurface(
            text = "Text OnSurface Enabled Button",
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "Text OnSurfaceInverse Button Light Theme Disabled",
    showBackground = true
)
@Preview(
    name = "Text OnSurfaceInverse Button Dark Theme Disabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Text_OnSurfaceInverse_Disabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Text.OnSurfaceInverse(
            text = "Text OnSurfaceInverse Disabled Button",
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(
    name = "Text OnSurfaceInverse Button Light Theme Enabled",
    showBackground = true
)
@Preview(
    name = "Text OnSurfaceInverse Button Dark Theme Enabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Text_OnSurfaceInverse_Enabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Text.OnSurface(
            text = "Text OnSurfaceInverse Enabled Button",
            enabled = true,
            onClick = {}
        )
    }
}

@Preview(
    name = "Text OnError Button Light Theme Disabled",
    showBackground = true
)
@Preview(
    name = "Text OnError Button Dark Theme Disabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Text_OnError_Disabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Text.OnError(
            text = "Text OnError Disabled Button",
            enabled = false,
            onClick = {}
        )
    }
}

@Preview(
    name = "Text OnError Button Light Theme Enabled",
    showBackground = true
)
@Preview(
    name = "Text OnError Button Dark Theme Enabled",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Text_OnError_Enabled_Preview() {
    PreviewFireFlowTheme {
        FireFlowButtons.Text.OnSurface(
            text = "Text OnError Enabled Button",
            enabled = true,
            onClick = {}
        )
    }
}
