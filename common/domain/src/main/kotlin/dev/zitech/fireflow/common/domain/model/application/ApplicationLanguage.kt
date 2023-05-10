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

package dev.zitech.fireflow.common.domain.model.application

import dev.zitech.fireflow.common.domain.R
import java.util.Locale

/**
 * Enum class representing application languages.
 *
 * @property id The ID of the application language.
 * @property text The string resource ID for displaying the language name.
 * @property locale The [Locale] associated with the language, or null if not applicable.
 */
enum class ApplicationLanguage(
    val id: Int,
    val text: Int,
    val locale: Locale?
) {
    /**
     * System language. The application language is determined by the system language setting.
     */
    SYSTEM(0, R.string.application_language_system, null),

    /**
     * English language.
     */
    ENGLISH(1, R.string.application_language_english, Locale.ENGLISH),

    /**
     * Bulgarian language.
     */
    BULGARIAN(2, R.string.application_language_bulgarian, Locale("bg", "BG"));

    companion object {

        /**
         * Retrieves the [ApplicationLanguage] enum based on the specified ID.
         *
         * @param id The ID of the application language.
         * @return The corresponding [ApplicationLanguage] enum value.
         */
        fun getApplicationLanguage(id: Int): ApplicationLanguage =
            when (id) {
                ENGLISH.id -> ENGLISH
                BULGARIAN.id -> BULGARIAN
                else -> SYSTEM
            }
    }
}
