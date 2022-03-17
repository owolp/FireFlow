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
import kotlinx.coroutines.flow.Flow

interface GetPreferencesRepository {

    fun getBoolean(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: Boolean
    ): Flow<Boolean>

    fun getFloat(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: Float
    ): Flow<Float>

    fun getInt(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: Int
    ): Flow<Int>

    fun getLong(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: Long
    ): Flow<Long>

    fun getString(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: String?
    ): Flow<String?>
}
