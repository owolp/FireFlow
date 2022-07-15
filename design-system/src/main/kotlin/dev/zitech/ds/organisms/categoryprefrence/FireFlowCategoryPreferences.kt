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

package dev.zitech.ds.organisms.categoryprefrence

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.ds.molecules.preference.FireFlowPreferences
import dev.zitech.ds.theme.FireFlowTheme

object FireFlowCategoryPreferences {

    @ExperimentalMaterial3Api
    @Composable
    fun Simple(
        categoryName: String,
        preferences: List<CategoryPreference>,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .background(FireFlowTheme.colors.background)
        ) {
            FireFlowPreferences.Category(title = categoryName)
            preferences.forEach { categoryPreference ->
                when (categoryPreference) {
                    is CategoryPreference.Checkbox -> {
                        with(categoryPreference) {
                            FireFlowPreferences.Checkbox(
                                modifier = categoryPreference.modifier,
                                title = title,
                                icon = icon,
                                checked = checked,
                                cdDescriptionEnabled = cdDescriptionEnabled,
                                cdDescriptionDisabled = cdDescriptionDisabled,
                                onCheckedChanged = onCheckedChanged,
                                description = description
                            )
                        }
                    }
                    is CategoryPreference.Icon -> {
                        with(categoryPreference) {
                            FireFlowPreferences.Icon(
                                modifier = categoryPreference.modifier,
                                title = title,
                                icon = icon,
                                description = description
                            )
                        }
                    }
                    is CategoryPreference.Simple -> {
                        with(categoryPreference) {
                            FireFlowPreferences.Simple(
                                modifier = categoryPreference.modifier,
                                title = title,
                                description = description
                            )
                        }
                    }
                    is CategoryPreference.Switch -> {
                        with(categoryPreference) {
                            FireFlowPreferences.Switch(
                                modifier = categoryPreference.modifier,
                                title = title,
                                icon = icon,
                                checked = checked,
                                cdDescriptionEnabled = cdDescriptionEnabled,
                                cdDescriptionDisabled = cdDescriptionDisabled,
                                onCheckedChanged = onCheckedChanged,
                                description = description
                            )
                        }
                    }
                }
                FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.l)
            }
        }
    }
}

@Preview(
    name = "Category Preferences Light Theme",
    showBackground = true
)
@Preview(
    name = "Category Preferences Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@ExperimentalMaterial3Api
@Composable
private fun CategoryPreferences_Preview() {
    FireFlowTheme {
        FireFlowCategoryPreferences.Simple(
            categoryName = "Category Name",
            preferences = listOf(
                CategoryPreference.Simple(
                    title = "Simple Title",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
                        "eiusmod tempor incididunt ut labore et dolore magna aliqua."
                ),
                CategoryPreference.Icon(
                    title = "Icon Title",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
                        "eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    icon = Icons.Outlined.Analytics
                ),
                CategoryPreference.Switch(
                    title = "Switch Title",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
                        "eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    icon = Icons.Outlined.Analytics,
                    checked = false,
                    cdDescriptionEnabled = "",
                    cdDescriptionDisabled = "",
                    onCheckedChanged = {}
                ),
                CategoryPreference.Checkbox(
                    title = "Checkbox Title",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
                        "eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    icon = Icons.Outlined.Analytics,
                    checked = false,
                    cdDescriptionEnabled = "",
                    cdDescriptionDisabled = "",
                    onCheckedChanged = {}
                )
            )
        )
    }
}
