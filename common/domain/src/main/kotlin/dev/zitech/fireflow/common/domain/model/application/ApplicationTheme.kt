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

enum class ApplicationTheme(
    val id: Int,
    val text: Int
) {

    SYSTEM(0, R.string.application_theme_system),
    DARK(1, R.string.application_theme_dark),
    LIGHT(2, R.string.application_theme_light);

    companion object {
        fun getApplicationTheme(id: Int): ApplicationTheme =
            when (id) {
                DARK.id -> DARK
                LIGHT.id -> LIGHT
                else -> SYSTEM
            }
    }
}
