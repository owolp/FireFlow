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

package dev.zitech.fireflow.common.domain.model.preferences

/**
 * Interface representing a preference.
 *
 * @param T The type of the preference value.
 */
internal interface Preference<T> {

    /**
     * The key associated with the preference.
     */
    val key: String

    /**
     * The title or name of the preference.
     */
    val title: String

    /**
     * A description or explanation of the preference.
     */
    val explanation: String

    /**
     * The default value of the preference.
     */
    val defaultValue: T
}
