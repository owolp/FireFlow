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

package dev.zitech.fireflow.ds.atoms.text

import android.content.res.Configuration
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.fireflow.ds.theme.FireFlowTheme
import dev.zitech.fireflow.ds.theme.PreviewFireFlowTheme

@Suppress("LargeClass")
@OptIn(ExperimentalMaterial3Api::class)
object FireFlowInputTexts {

    @Composable
    fun DisplayLarge(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.displayLarge,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun DisplayMedium(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.displayMedium,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun DisplaySmall(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.displaySmall,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun HeadlineLarge(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.headlineLarge,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun HeadlineMedium(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.headlineMedium,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun HeadlineSmall(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.headlineSmall,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun TitleLarge(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.titleLarge,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun TitleMedium(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.titleMedium,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun TitleSmall(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.titleSmall,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun BodyLarge(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.bodyLarge,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun BodyMedium(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.bodyMedium,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun BodySmall(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.bodySmall,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun LabelLarge(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.labelLarge,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun LabelMedium(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.labelMedium,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
        )
    }

    @Composable
    fun LabelSmall(
        value: String,
        onValueChanged: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeHolderText: String? = null,
        supportingText: String? = null,
        singleLine: Boolean = true,
        enabled: Boolean = true,
        isError: Boolean = false,
        style: TextStyle = FireFlowTheme.typography.labelSmall,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        maxLines: Int = Int.MAX_VALUE
    ) {
        TextField(
            modifier = modifier,
            value = value,
            singleLine = singleLine,
            enabled = enabled,
            isError = isError,
            textStyle = style,
            shape = FireFlowTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = FireFlowTheme.colors.background,
                unfocusedIndicatorColor = FireFlowTheme.colors.background,
                disabledIndicatorColor = FireFlowTheme.colors.background
            ),
            onValueChange = onValueChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            placeholder = {
                placeHolderText?.let {
                    FireFlowTexts.DisplayLarge(text = placeHolderText)
                }
            },
            supportingText = {
                supportingText?.let {
                    FireFlowTexts.BodyMedium(text = supportingText)
                }
            }
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
        FireFlowInputTexts.DisplayLarge(
            value = "Display Large Text",
            onValueChanged = {}
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
        FireFlowInputTexts.DisplayMedium(
            value = "Display Medium Text",
            onValueChanged = {}
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
        FireFlowInputTexts.DisplaySmall(
            value = "Display Small Text",
            onValueChanged = {}
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
        FireFlowInputTexts.HeadlineLarge(
            value = "Headline Large Text",
            onValueChanged = {}
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
        FireFlowInputTexts.HeadlineMedium(
            value = "Headline Medium Text",
            onValueChanged = {}
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
        FireFlowInputTexts.HeadlineSmall(
            value = "Headline Small Text",
            onValueChanged = {}
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
        FireFlowInputTexts.TitleLarge(
            value = "Title Large Text",
            onValueChanged = {}
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
        FireFlowInputTexts.TitleMedium(
            value = "Title Medium Text",
            onValueChanged = {}
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
        FireFlowInputTexts.TitleSmall(
            value = "Title Small Text",
            onValueChanged = {}
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
        FireFlowInputTexts.BodyLarge(
            value = "Body Large Text",
            onValueChanged = {}
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
        FireFlowInputTexts.BodyMedium(
            value = "Body Medium Text",
            onValueChanged = {}
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
        FireFlowInputTexts.BodySmall(
            value = "Body Small Text",
            onValueChanged = {}
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
        FireFlowInputTexts.LabelLarge(
            value = "Label Large Text",
            onValueChanged = {}
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
        FireFlowInputTexts.LabelMedium(
            value = "Label Medium Text",
            onValueChanged = {}
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
        FireFlowInputTexts.LabelSmall(
            value = "Label Small Text",
            onValueChanged = {}
        )
    }
}
