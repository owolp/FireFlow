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

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

sealed class CategoryPreference {

    data class Simple(
        val title: String,
        val modifier: Modifier = Modifier,
        val description: String? = null,
        val onClick: Pair<String, (() -> Unit)>? = null
    ) : CategoryPreference()

    data class Icon(
        val title: String,
        val icon: ImageVector,
        val modifier: Modifier = Modifier,
        val description: String? = null,
        val onClick: Pair<String, (() -> Unit)>? = null
    ) : CategoryPreference()

    data class Switch(
        val title: String,
        val icon: ImageVector,
        val checked: Boolean,
        val cdDescriptionEnabled: String,
        val cdDescriptionDisabled: String,
        val onCheckedChanged: (checked: Boolean) -> Unit,
        val modifier: Modifier = Modifier,
        val description: String? = null
    ) : CategoryPreference()

    data class Checkbox(
        val title: String,
        val icon: ImageVector,
        val checked: Boolean,
        val cdDescriptionEnabled: String,
        val cdDescriptionDisabled: String,
        val modifier: Modifier = Modifier,
        val onCheckedChanged: (checked: Boolean) -> Unit,
        val description: String? = null
    ) : CategoryPreference()
}
