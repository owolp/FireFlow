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

package dev.zitech.fireflow.ds.organisms.categoryprefrence

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.fireflow.ds.atoms.icon.FireFlowIcons
import dev.zitech.fireflow.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.fireflow.ds.molecules.preference.FireFlowPreferences
import dev.zitech.fireflow.ds.theme.FireFlowTheme
import dev.zitech.fireflow.ds.theme.PreviewFireFlowTheme

object FireFlowCategoryPreferences {

    @Composable
    fun Primary(
        categoryName: String,
        preferences: List<CategoryPreference>,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
        ) {
            FireFlowPreferences.Category(title = categoryName)
            FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.s)
            Column(
                verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.m)
            ) {
                preferences.forEach { categoryPreference ->
                    CategoryPreferenceItem(categoryPreference)
                }
            }
        }
    }

    @Composable
    private fun CategoryPreferenceItem(
        categoryPreference: CategoryPreference
    ) {
        when (categoryPreference) {
            is CategoryPreference.Checkbox -> {
                with(categoryPreference) {
                    FireFlowPreferences.Checkbox(
                        modifier = modifier,
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
                        modifier = modifier,
                        title = title,
                        icon = icon,
                        description = description,
                        onClick = onClick
                    )
                }
            }
            is CategoryPreference.Primary -> {
                with(categoryPreference) {
                    FireFlowPreferences.Primary(
                        modifier = modifier,
                        title = title,
                        description = description,
                        onClick = onClick
                    )
                }
            }
            is CategoryPreference.Switch -> {
                with(categoryPreference) {
                    FireFlowPreferences.Switch(
                        modifier = modifier,
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
@Composable
private fun Category_Preferences_Preview() {
    PreviewFireFlowTheme {
        FireFlowCategoryPreferences.Primary(
            categoryName = "Category Name",
            preferences = listOf(
                CategoryPreference.Primary(
                    title = "Primary Title",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed " +
                        "eiusmod tempor incididunt ut labore et dolore magna aliqua."
                ),
                CategoryPreference.Icon(
                    title = "Icon Title",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed " +
                        "eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    icon = FireFlowIcons.Analytics
                ),
                CategoryPreference.Switch(
                    title = "Switch Title",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed " +
                        "eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    icon = FireFlowIcons.Analytics,
                    checked = false,
                    cdDescriptionEnabled = "",
                    cdDescriptionDisabled = "",
                    onCheckedChanged = {}
                ),
                CategoryPreference.Checkbox(
                    title = "Checkbox Title",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed " +
                        "eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    icon = FireFlowIcons.Analytics,
                    checked = false,
                    cdDescriptionEnabled = "",
                    cdDescriptionDisabled = "",
                    onCheckedChanged = {}
                )
            )
        )
    }
}
