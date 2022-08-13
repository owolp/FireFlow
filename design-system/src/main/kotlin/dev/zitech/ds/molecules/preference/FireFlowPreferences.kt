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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Card
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
import dev.zitech.ds.atoms.checkbox.FireFlowCheckboxes
import dev.zitech.ds.atoms.icon.FireFlowIcons
import dev.zitech.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.ds.atoms.switch.FireFlowSwitches
import dev.zitech.ds.atoms.text.FireFlowTexts
import dev.zitech.ds.theme.FireFlowTheme

private val imageSize = 32.dp
private val actionSize = 56.dp
private const val DESCRIPTION_ALPHA = 0.7F

object FireFlowPreferences {

    @Composable
    fun Category(
        title: String,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FireFlowTexts.HeadlineMedium(
                text = title
            )
        }
    }

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
                    .fillMaxHeight()
                    .clickable(
                        enabled = onClick != null,
                        onClick = { onClick?.second?.invoke() }
                    )
                    .semantics {
                        stateDescription = onClick?.first.orEmpty()
                    }
                    .padding(FireFlowTheme.space.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.s + imageSize + FireFlowTheme.space.m
                )
                Column(
                    modifier = Modifier.weight(1.0f),
                    verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.xss)
                ) {
                    FireFlowTexts.TitleLarge(text = title)
                    if (description != null) {
                        FireFlowTexts.TitleMedium(
                            text = description,
                            color = FireFlowTheme.colors.onBackground.copy(
                                alpha = DESCRIPTION_ALPHA
                            )
                        )
                    }
                }
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.m + actionSize + FireFlowTheme.space.s
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
                    .fillMaxHeight()
                    .clickable(enabled = onClick != null, onClick = { onClick?.second?.invoke() })
                    .semantics {
                        stateDescription = onClick?.first.orEmpty()
                    }
                    .padding(FireFlowTheme.space.xs),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.s
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
                    verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.xss)
                ) {
                    FireFlowTexts.TitleLarge(text = title)
                    if (description != null) {
                        FireFlowTexts.TitleMedium(
                            text = description,
                            color = FireFlowTheme.colors.onBackground.copy(
                                alpha = DESCRIPTION_ALPHA
                            )
                        )
                    }
                }
                FireFlowSpacers.Horizontal(
                    horizontalSpace = FireFlowTheme.space.m + actionSize + FireFlowTheme.space.s
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
                    .fillMaxHeight()
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
                    horizontalSpace = FireFlowTheme.space.s
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
                    verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.xss)
                ) {
                    FireFlowTexts.TitleLarge(text = title)
                    if (description != null) {
                        FireFlowTexts.TitleMedium(
                            text = description,
                            color = FireFlowTheme.colors.onBackground.copy(
                                alpha = DESCRIPTION_ALPHA
                            )
                        )
                    }
                }
                FireFlowSpacers.Horizontal(horizontalSpace = FireFlowTheme.space.m)
                FireFlowSwitches.Primary(
                    modifier = Modifier.size(actionSize),
                    checked = checked,
                    onCheckedChange = null
                )
                FireFlowSpacers.Horizontal(horizontalSpace = FireFlowTheme.space.s)
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
                    .fillMaxHeight()
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
                    horizontalSpace = FireFlowTheme.space.s
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
                    verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.xss)
                ) {
                    FireFlowTexts.TitleLarge(text = title)
                    if (description != null) {
                        FireFlowTexts.TitleMedium(
                            text = description,
                            color = FireFlowTheme.colors.onBackground.copy(
                                alpha = DESCRIPTION_ALPHA
                            )
                        )
                    }
                }
                FireFlowSpacers.Horizontal(horizontalSpace = FireFlowTheme.space.m)
                FireFlowCheckboxes.Primary(
                    modifier = Modifier.size(actionSize),
                    checked = checked,
                    onCheckedChange = null
                )
                FireFlowSpacers.Horizontal(horizontalSpace = FireFlowTheme.space.s)
            }
        }
    }

    @SuppressWarnings("ForbiddenComment")
    @Composable
    private fun PreferenceItem(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        // TODO: Extract Card to atom
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .heightIn(min = 72.dp),
            shape = FireFlowTheme.shapes.extraLarge
        ) {
            content()
        }
    }
}

@Preview(
    name = "Category Light Theme",
    showBackground = true
)
@Preview(
    name = "Category Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun Category_Preview() {
    FireFlowTheme {
        FireFlowPreferences.Category(
            title = "Category Title"
        )
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
private fun Simple_Title_Preview() {
    FireFlowTheme {
        FireFlowPreferences.Simple(
            title = "Simple Title"
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
private fun Simple_Title_Description_Preview() {
    FireFlowTheme {
        FireFlowPreferences.Simple(
            title = "Simple Title",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua."
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
private fun Icon_Title_Preview() {
    FireFlowTheme {
        FireFlowPreferences.Icon(
            title = "Icon Title",
            icon = FireFlowIcons.Analytics
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
private fun Icon_Title_Description_Preview() {
    FireFlowTheme {
        FireFlowPreferences.Icon(
            title = "Icon Title",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua.",
            icon = FireFlowIcons.Analytics
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
private fun Switch_Title_Preview() {
    FireFlowTheme {
        FireFlowPreferences.Switch(
            title = "Switch Title",
            icon = FireFlowIcons.Analytics,
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
private fun Switch_Title_Description_Preview() {
    FireFlowTheme {
        FireFlowPreferences.Switch(
            title = "Switch Title",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua.",
            icon = FireFlowIcons.Analytics,
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
private fun Checkbox_Preview() {
    FireFlowTheme {
        FireFlowPreferences.Checkbox(
            title = "Checkbox Title",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua.",
            icon = FireFlowIcons.Analytics,
            checked = false,
            cdDescriptionEnabled = "",
            cdDescriptionDisabled = "",
            onCheckedChanged = {}
        )
    }
}
