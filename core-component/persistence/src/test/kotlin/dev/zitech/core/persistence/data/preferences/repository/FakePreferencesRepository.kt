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

package dev.zitech.core.persistence.data.preferences.repository

import dev.zitech.core.persistence.domain.model.preferences.PreferenceType
import dev.zitech.core.persistence.domain.repository.preferences.ContainsPreferencesRepository
import dev.zitech.core.persistence.domain.repository.preferences.GetPreferencesRepository
import dev.zitech.core.persistence.domain.repository.preferences.RemovePreferencesRepository
import dev.zitech.core.persistence.domain.repository.preferences.SavePreferencesRepository
import dev.zitech.core.persistence.framework.preferences.FakePreferencesDataSource
import kotlinx.coroutines.flow.Flow

internal class FakePreferencesRepository :
    ContainsPreferencesRepository,
    GetPreferencesRepository,
    RemovePreferencesRepository,
    SavePreferencesRepository {

    private val fakePreferencesDataSource = FakePreferencesDataSource()

    override fun containsBoolean(preferenceType: PreferenceType, key: String): Flow<Boolean> =
        fakePreferencesDataSource.containsBoolean(key)

    override fun containsFloat(preferenceType: PreferenceType, key: String): Flow<Boolean> =
        fakePreferencesDataSource.containsFloat(key)

    override fun containsInt(preferenceType: PreferenceType, key: String): Flow<Boolean> =
        fakePreferencesDataSource.containsInt(key)

    override fun containsLong(preferenceType: PreferenceType, key: String): Flow<Boolean> =
        fakePreferencesDataSource.containsLong(key)

    override fun containsString(preferenceType: PreferenceType, key: String): Flow<Boolean> =
        fakePreferencesDataSource.containsString(key)

    override fun getBoolean(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: Boolean
    ): Flow<Boolean> = fakePreferencesDataSource.getBoolean(key, defaultValue)

    override fun getFloat(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: Float
    ): Flow<Float> = fakePreferencesDataSource.getFloat(key, defaultValue)

    override fun getInt(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: Int
    ): Flow<Int> = fakePreferencesDataSource.getInt(key, defaultValue)

    override fun getLong(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: Long
    ): Flow<Long> = fakePreferencesDataSource.getLong(key, defaultValue)

    override fun getString(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: String?
    ): Flow<String?> = fakePreferencesDataSource.getString(key, defaultValue)

    override suspend fun removeBoolean(preferenceType: PreferenceType, key: String) =
        fakePreferencesDataSource.removeBoolean(key)

    override suspend fun removeFloat(preferenceType: PreferenceType, key: String) =
        fakePreferencesDataSource.removeFloat(key)

    override suspend fun removeInt(preferenceType: PreferenceType, key: String) =
        fakePreferencesDataSource.removeInt(key)

    override suspend fun removeLong(preferenceType: PreferenceType, key: String) =
        fakePreferencesDataSource.removeLong(key)

    override suspend fun removeString(preferenceType: PreferenceType, key: String) =
        fakePreferencesDataSource.removeString(key)

    override suspend fun removeAll(preferenceType: PreferenceType) =
        fakePreferencesDataSource.removeAll()

    override suspend fun saveBoolean(preferenceType: PreferenceType, key: String, value: Boolean) =
        fakePreferencesDataSource.saveBoolean(key, value)

    override suspend fun saveFloat(preferenceType: PreferenceType, key: String, value: Float) =
        fakePreferencesDataSource.saveFloat(key, value)

    override suspend fun saveInt(preferenceType: PreferenceType, key: String, value: Int) =
        fakePreferencesDataSource.saveInt(key, value)

    override suspend fun saveLong(preferenceType: PreferenceType, key: String, value: Long) =
        fakePreferencesDataSource.saveLong(key, value)

    override suspend fun saveString(preferenceType: PreferenceType, key: String, value: String) =
        fakePreferencesDataSource.saveString(key, value)
}
