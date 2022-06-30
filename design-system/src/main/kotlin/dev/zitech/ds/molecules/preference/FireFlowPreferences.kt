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

package dev.zitech.ds.molecules.preference

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.zitech.ds.atoms.checkbox.FIreFlowCheckboxes
import dev.zitech.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.ds.atoms.switch.FireFlowSwitches
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.theme.FireFlowTheme

private val imageSize = 32.dp
private const val descriptionAlpha = 0.7F

object FireFlowPreferences {

    @Composable
    fun Simple(
        title: String,
        modifier: Modifier = Modifier,
        description: String? = null,
        onClick: Pair<String, (() -> Unit)>? = null
    ) {
        PreferenceItem(modifier = modifier) {
            Row(
                modifier = Modifier
                    .clickable(enabled = onClick != null, onClick = { onClick?.second?.invoke() })
                    .semantics {
                        stateDescription = onClick?.first.orEmpty()
                    }
                    .padding(FireFlowTheme.space.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.m + imageSize + FireFlowTheme.space.m
                )
                Column(
                    modifier = Modifier.weight(1.0f),
                    verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.s)
                ) {
                    FireFlowTexts.TitleMedium(text = title)
                    if (description != null) {
                        FireFlowTexts.TitleSmall(
                            text = description,
                            color = FireFlowTheme.colors.onBackground.copy(
                                alpha = descriptionAlpha
                            )
                        )
                    }
                }
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.m + imageSize + FireFlowTheme.space.m
                )
            }
        }
    }

    @Composable
    fun Icon(
        title: String,
        icon: ImageVector,
        modifier: Modifier = Modifier,
        description: String? = null,
        onClick: Pair<String, (() -> Unit)>? = null
    ) {
        PreferenceItem(modifier = modifier) {
            Row(
                modifier = Modifier
                    .clickable(enabled = onClick != null, onClick = { onClick?.second?.invoke() })
                    .semantics {
                        stateDescription = onClick?.first.orEmpty()
                    }
                    .padding(FireFlowTheme.space.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.m
                )
                Image(
                    modifier = Modifier.size(imageSize),
                    colorFilter = ColorFilter.tint(FireFlowTheme.colors.onBackground),
                    imageVector = icon,
                    contentDescription = null
                )
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.m
                )
                Column(
                    modifier = Modifier.weight(1.0f),
                    verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.s)
                ) {
                    FireFlowTexts.TitleMedium(text = title)
                    if (description != null) {
                        FireFlowTexts.TitleSmall(
                            text = description,
                            color = FireFlowTheme.colors.onBackground.copy(
                                alpha = descriptionAlpha
                            )
                        )
                    }
                }
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.m + imageSize + FireFlowTheme.space.m
                )
            }
        }
    }

    @Composable
    fun Switch(
        title: String,
        icon: ImageVector,
        checked: Boolean,
        cdDescriptionEnabled: String,
        cdDescriptionDisabled: String,
        onCheckedChanged: (checked: Boolean) -> Unit,
        modifier: Modifier = Modifier,
        description: String? = null
    ) {
        PreferenceItem(modifier = modifier) {
            Row(
                modifier = Modifier
                    .toggleable(
                        value = checked,
                        onValueChange = onCheckedChanged,
                        role = Role.Switch
                    )
                    .semantics {
                        stateDescription = if (checked) {
                            cdDescriptionEnabled
                        } else {
                            cdDescriptionDisabled
                        }
                    }
                    .padding(FireFlowTheme.space.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.m
                )
                Image(
                    modifier = Modifier.size(imageSize),
                    colorFilter = ColorFilter.tint(FireFlowTheme.colors.onBackground),
                    imageVector = icon,
                    contentDescription = null
                )
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.m
                )
                Column(
                    modifier = Modifier.weight(1.0f),
                    verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.s)
                ) {
                    FireFlowTexts.TitleMedium(text = title)
                    if (description != null) {
                        FireFlowTexts.TitleSmall(
                            text = description,
                            color = FireFlowTheme.colors.onBackground.copy(
                                alpha = descriptionAlpha
                            )
                        )
                    }
                }
                FireFlowSpacers.Horizontal(horizontalSpace = FireFlowTheme.space.m)
                FireFlowSwitches.Primary(
                    checked = checked,
                    onCheckedChange = null
                )
                FireFlowSpacers.Horizontal(horizontalSpace = FireFlowTheme.space.m)
            }
        }
    }

    @ExperimentalMaterial3Api
    @Composable
    fun Checkbox(
        title: String,
        icon: ImageVector,
        checked: Boolean,
        cdDescriptionEnabled: String,
        cdDescriptionDisabled: String,
        modifier: Modifier = Modifier,
        onCheckedChanged: (checked: Boolean) -> Unit,
        description: String? = null
    ) {
        PreferenceItem(modifier = modifier) {
            Row(
                modifier = Modifier
                    .toggleable(
                        value = checked,
                        onValueChange = onCheckedChanged,
                        role = Role.Checkbox
                    )
                    .semantics {
                        stateDescription = if (checked) {
                            cdDescriptionEnabled
                        } else {
                            cdDescriptionDisabled
                        }
                    }
                    .padding(FireFlowTheme.space.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.m
                )
                Image(
                    modifier = Modifier.size(imageSize),
                    colorFilter = ColorFilter.tint(FireFlowTheme.colors.onBackground),
                    imageVector = icon,
                    contentDescription = null
                )
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.l
                )
                Column(
                    modifier = Modifier.weight(1.0f),
                    verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.s)
                ) {
                    FireFlowTexts.TitleMedium(text = title)
                    if (description != null) {
                        FireFlowTexts.TitleSmall(
                            text = description,
                            color = FireFlowTheme.colors.onBackground.copy(
                                alpha = descriptionAlpha
                            )
                        )
                    }
                }
                FireFlowSpacers.Horizontal(horizontalSpace = FireFlowTheme.space.m)
                FIreFlowCheckboxes.Primary(
                    checked = checked,
                    onCheckedChange = null
                )
                FireFlowSpacers.Horizontal(horizontalSpace = FireFlowTheme.space.m)
            }
        }
    }

    @Composable
    private fun PreferenceItem(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp),
            color = FireFlowTheme.colors.background
        ) {
            content()
        }
    }
}

@Preview(
    name = "Simple Title Only Light Theme",
    showBackground = true
)
@Preview(
    name = "Simple Title Only Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun SimpleTitlePreview() {
    FireFlowTheme {
        FireFlowPreferences.Simple(
            title = "Title"
        )
    }
}

@Preview(
    name = "Simple Title and Description Light Theme",
    showBackground = true
)
@Preview(
    name = "Simple Title and Description Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun SimpleTitleDescriptionPreview() {
    FireFlowTheme {
        FireFlowPreferences.Simple(
            title = "Title",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
        )
    }
}

@Preview(
    name = "Icon Title Only Light Theme",
    showBackground = true
)
@Preview(
    name = "Icon Title Only Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun IconTitlePreview() {
    FireFlowTheme {
        FireFlowPreferences.Icon(
            title = "Title",
            icon = Icons.Outlined.Analytics
        )
    }
}

@Preview(
    name = "Icon Title and Description Light Theme",
    showBackground = true
)
@Preview(
    name = "Icon Title and Description Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun IconTitleDescriptionPreview() {
    FireFlowTheme {
        FireFlowPreferences.Icon(
            title = "Title",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            icon = Icons.Outlined.Analytics
        )
    }
}

@Preview(
    name = "Switch Title Only Light Theme",
    showBackground = true
)
@Preview(
    name = "Switch Title Only Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun SwitchTitlePreview() {
    FireFlowTheme {
        FireFlowPreferences.Switch(
            title = "Title",
            icon = Icons.Outlined.Analytics,
            checked = true,
            cdDescriptionEnabled = "",
            cdDescriptionDisabled = "",
            onCheckedChanged = {}
        )
    }
}

@Preview(
    name = "Switch Title and Description Light Theme",
    showBackground = true
)
@Preview(
    name = "Switch Title and Description Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun SwitchTitleDescriptionPreview() {
    FireFlowTheme {
        FireFlowPreferences.Switch(
            title = "Title",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            icon = Icons.Outlined.Analytics,
            checked = false,
            cdDescriptionEnabled = "",
            cdDescriptionDisabled = "",
            onCheckedChanged = {}
        )
    }
}

@Preview(
    name = "Checkbox Light Theme",
    showBackground = true
)
@Preview(
    name = "Checkbox Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@ExperimentalMaterial3Api
@Composable
private fun CheckboxPreview() {
    FireFlowTheme {
        FireFlowPreferences.Checkbox(
            title = "Title",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            icon = Icons.Outlined.Analytics,
            checked = false,
            cdDescriptionEnabled = "",
            cdDescriptionDisabled = "",
            onCheckedChanged = {}
        )
    }
}
