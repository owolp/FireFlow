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

package dev.zitech.fireflow.common.domain.source.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FakePreferencesDataSource : PreferencesDataSource {

    private val preferencesMap: MutableMap<String, Any?> = mutableMapOf()

    override fun containsBoolean(key: String): Flow<Boolean> =
        flowOf(preferencesMap.containsKey(key) && preferencesMap[key] is Boolean)

    override fun containsFloat(key: String): Flow<Boolean> =
        flowOf(preferencesMap.containsKey(key) && preferencesMap[key] is Float)

    override fun containsInt(key: String): Flow<Boolean> =
        flowOf(preferencesMap.containsKey(key) && preferencesMap[key] is Int)

    override fun containsLong(key: String): Flow<Boolean> =
        flowOf(preferencesMap.containsKey(key) && preferencesMap[key] is Long)

    override fun containsString(key: String): Flow<Boolean> =
        flowOf(preferencesMap.containsKey(key) && preferencesMap[key] is String)

    override fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean> =
        flowOf(preferencesMap[key] as? Boolean ?: defaultValue)

    override fun getFloat(key: String, defaultValue: Float): Flow<Float> =
        flowOf(preferencesMap[key] as? Float ?: defaultValue)

    override fun getInt(key: String, defaultValue: Int): Flow<Int> =
        flowOf(preferencesMap[key] as? Int ?: defaultValue)

    override fun getLong(key: String, defaultValue: Long): Flow<Long> =
        flowOf(preferencesMap[key] as? Long ?: defaultValue)

    override fun getString(key: String, defaultValue: String?): Flow<String?> =
        flowOf(preferencesMap[key] as? String ?: defaultValue)

    override suspend fun removeAll() {
        preferencesMap.clear()
    }

    override suspend fun removeBoolean(key: String) {
        preferencesMap.remove(key)
    }

    override suspend fun removeFloat(key: String) {
        preferencesMap.remove(key)
    }

    override suspend fun removeInt(key: String) {
        preferencesMap.remove(key)
    }

    override suspend fun removeLong(key: String) {
        preferencesMap.remove(key)
    }

    override suspend fun removeString(key: String) {
        preferencesMap.remove(key)
    }

    override suspend fun saveBoolean(key: String, value: Boolean) {
        preferencesMap[key] = value
    }

    override suspend fun saveFloat(key: String, value: Float) {
        preferencesMap[key] = value
    }

    override suspend fun saveInt(key: String, value: Int) {
        preferencesMap[key] = value
    }

    override suspend fun saveLong(key: String, value: Long) {
        preferencesMap[key] = value
    }

    override suspend fun saveString(key: String, value: String) {
        preferencesMap[key] = value
    }
}
