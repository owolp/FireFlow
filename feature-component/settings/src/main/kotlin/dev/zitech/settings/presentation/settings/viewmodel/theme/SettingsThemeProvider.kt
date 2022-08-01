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

package dev.zitech.settings.presentation.settings.viewmodel.theme

import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.ds.molecules.dialog.DialogRadioItem
import dev.zitech.settings.R
import javax.inject.Inject

class SettingsThemeProvider @Inject constructor(
    private val stringsProvider: StringsProvider
) {

    internal fun getDialogThemeTitle(): String =
        stringsProvider(R.string.appearance_dialog_theme_title)

    internal fun getDialogThemes(applicationTheme: ApplicationTheme): List<DialogRadioItem> =
        ApplicationTheme.values().sortedBy { it.id }
            .map {
                DialogRadioItem(
                    id = it.id,
                    text = stringsProvider(it.text),
                    selected = applicationTheme.id == it.id,
                    enabled = true
                )
            }
}
