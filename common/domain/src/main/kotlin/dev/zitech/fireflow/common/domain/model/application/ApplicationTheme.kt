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

/**
 * Enum class representing application themes.
 *
 * @property id The ID of the application theme.
 * @property text The string resource ID for displaying the theme name.
 */
enum class ApplicationTheme(
    val id: Int,
    val text: Int
) {
    /**
     * System theme. The application theme is determined by the system theme setting.
     */
    SYSTEM(0, R.string.application_theme_system),

    /**
     * Dark theme.
     */
    DARK(1, R.string.application_theme_dark),

    /**
     * Light theme.
     */
    LIGHT(2, R.string.application_theme_light);

    companion object {

        /**
         * Retrieves the [ApplicationTheme] enum based on the specified ID.
         *
         * @param id The ID of the application theme.
         * @return The corresponding [ApplicationTheme] enum value.
         */
        fun getApplicationTheme(id: Int): ApplicationTheme =
            when (id) {
                DARK.id -> DARK
                LIGHT.id -> LIGHT
                else -> SYSTEM
            }
    }
}
