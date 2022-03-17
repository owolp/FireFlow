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

interface SavePreferencesRepository {

    suspend fun saveBoolean(
        preferenceType: PreferenceType,
        key: String,
        value: Boolean
    )

    suspend fun saveFloat(
        preferenceType: PreferenceType,
        key: String,
        value: Float
    )

    suspend fun saveInt(
        preferenceType: PreferenceType,
        key: String,
        value: Int
    )

    suspend fun saveLong(
        preferenceType: PreferenceType,
        key: String,
        value: Long
    )

    suspend fun saveString(
        preferenceType: PreferenceType,
        key: String,
        value: String
    )
}
