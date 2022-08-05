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

package dev.zitech.core.common.domain.model

import androidx.annotation.StringRes
import dev.zitech.core.common.R
import java.util.*

enum class ApplicationLanguage(
    val id: Int,
    @StringRes val text: Int,
    val locale: Locale?
) {
    SYSTEM(0, R.string.application_language_system, null),
    ENGLISH(1, R.string.application_language_english, Locale.ENGLISH),
    BULGARIAN(2, R.string.application_language_bulgarian, Locale.forLanguageTag("bg-BG"))
}
