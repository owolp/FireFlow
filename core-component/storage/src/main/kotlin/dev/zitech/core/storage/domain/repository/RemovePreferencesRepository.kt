/*
 * Copyright (C) 2022 Zitech
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

package dev.zitech.core.storage.domain.repository

import dev.zitech.core.storage.domain.model.PreferenceType

interface RemovePreferencesRepository {

    suspend fun removeBoolean(
        preferenceType: PreferenceType,
        key: String
    )

    suspend fun removeFloat(
        preferenceType: PreferenceType,
        key: String
    )

    suspend fun removeInt(
        preferenceType: PreferenceType,
        key: String
    )

    suspend fun removeLong(
        preferenceType: PreferenceType,
        key: String
    )

    suspend fun removeString(
        preferenceType: PreferenceType,
        key: String
    )

    suspend fun removeAll(preferenceType: PreferenceType)
}
