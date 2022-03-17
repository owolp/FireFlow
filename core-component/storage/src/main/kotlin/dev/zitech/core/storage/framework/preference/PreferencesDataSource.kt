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

package dev.zitech.core.storage.framework.preference

import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
internal interface PreferencesDataSource {

    fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean>

    fun getFloat(key: String, defaultValue: Float): Flow<Float>

    fun getInt(key: String, defaultValue: Int): Flow<Int>

    fun getLong(key: String, defaultValue: Long): Flow<Long>

    fun getString(key: String, defaultValue: String?): Flow<String?>

    fun containsBoolean(key: String): Flow<Boolean>

    fun containsFloat(key: String): Flow<Boolean>

    fun containsInt(key: String): Flow<Boolean>

    fun containsLong(key: String): Flow<Boolean>

    fun containsString(key: String): Flow<Boolean>

    suspend fun saveBoolean(key: String, value: Boolean)

    suspend fun saveFloat(key: String, value: Float)

    suspend fun saveInt(key: String, value: Int)

    suspend fun saveLong(key: String, value: Long)

    suspend fun saveString(key: String, value: String)

    suspend fun removeBoolean(key: String)

    suspend fun removeFloat(key: String)

    suspend fun removeInt(key: String)

    suspend fun removeLong(key: String)

    suspend fun removeString(key: String)

    suspend fun removeAll()
}
