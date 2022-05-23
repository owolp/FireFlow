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

package dev.zitech.core.persistence.framework.preferences

import dev.zitech.core.persistence.data.source.preferences.PreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakePreferencesDataSource : PreferencesDataSource {

    private val booleans = mutableMapOf<String, Boolean>()
    private val floats = mutableMapOf<String, Float>()
    private val ints = mutableMapOf<String, Int>()
    private val longs = mutableMapOf<String, Long>()
    private val strings = mutableMapOf<String, String>()

    override fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean> = flow {
        if (booleans.containsKey(key)) {
            emit(booleans[key]!!)
        } else {
            emit(defaultValue)
        }
    }

    override fun getFloat(key: String, defaultValue: Float): Flow<Float> = flow {
        if (floats.containsKey(key)) {
            emit(floats[key]!!)
        } else {
            emit(defaultValue)
        }
    }

    override fun getInt(key: String, defaultValue: Int): Flow<Int> = flow {
        if (ints.containsKey(key)) {
            emit(ints[key]!!)
        } else {
            emit(defaultValue)
        }
    }

    override fun getLong(key: String, defaultValue: Long): Flow<Long> = flow {
        if (longs.containsKey(key)) {
            emit(longs[key]!!)
        } else {
            emit(defaultValue)
        }
    }

    override fun getString(key: String, defaultValue: String?): Flow<String?> = flow {
        if (strings.containsKey(key)) {
            emit(strings[key]!!)
        } else {
            emit(defaultValue)
        }
    }

    override fun containsBoolean(key: String): Flow<Boolean> =
        flowOf(booleans.containsKey(key))

    override fun containsFloat(key: String): Flow<Boolean> =
        flowOf(floats.containsKey(key))

    override fun containsInt(key: String): Flow<Boolean> =
        flowOf(ints.containsKey(key))

    override fun containsLong(key: String): Flow<Boolean> =
        flowOf(longs.containsKey(key))

    override fun containsString(key: String): Flow<Boolean> =
        flowOf(strings.containsKey(key))

    override suspend fun saveBoolean(key: String, value: Boolean) {
        booleans[key] = value
    }

    override suspend fun saveFloat(key: String, value: Float) {
        floats[key] = value
    }

    override suspend fun saveInt(key: String, value: Int) {
        ints[key] = value
    }

    override suspend fun saveLong(key: String, value: Long) {
        longs[key] = value
    }

    override suspend fun saveString(key: String, value: String) {
        strings[key] = value
    }

    override suspend fun removeBoolean(key: String) {
        booleans.remove(key)
    }

    override suspend fun removeFloat(key: String) {
        floats.remove(key)
    }

    override suspend fun removeInt(key: String) {
        ints.remove(key)
    }

    override suspend fun removeLong(key: String) {
        longs.remove(key)
    }

    override suspend fun removeString(key: String) {
        strings.remove(key)
    }

    override suspend fun removeAll() {
        booleans.clear()
        floats.clear()
        ints.clear()
        longs.clear()
        strings.clear()
    }
}
