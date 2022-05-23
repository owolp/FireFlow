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

package dev.zitech.core.persistence.framework.preference.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences as DataStorePreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.zitech.core.common.domain.dispatcher.AppDispatchers
import dev.zitech.core.common.framework.logger.Logger
import dev.zitech.core.persistence.domain.source.preferences.PreferencesDataSource
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Suppress("TooManyFunctions")
internal class StandardPreferencesDataSource(
    private val appDispatchers: AppDispatchers,
    private val fileName: String,
    context: Context
) : PreferencesDataSource {

    private val Context.dataStore: DataStore<DataStorePreferences> by preferencesDataStore(
        name = fileName
    )

    private val preferenceDataStore: DataStore<DataStorePreferences> = context.dataStore

    override fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean> =
        getDataStorePreferences().map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: defaultValue
        }

    override fun getFloat(key: String, defaultValue: Float): Flow<Float> =
        getDataStorePreferences().map { preferences ->
            preferences[floatPreferencesKey(key)] ?: defaultValue
        }

    override fun getInt(key: String, defaultValue: Int): Flow<Int> =
        getDataStorePreferences().map { preferences ->
            preferences[intPreferencesKey(key)] ?: defaultValue
        }

    override fun getLong(key: String, defaultValue: Long): Flow<Long> =
        getDataStorePreferences().map { preferences ->
            preferences[longPreferencesKey(key)] ?: defaultValue
        }

    override fun getString(key: String, defaultValue: String?): Flow<String?> =
        getDataStorePreferences().map { preferences ->
            preferences[stringPreferencesKey(key)] ?: defaultValue
        }

    override fun containsBoolean(key: String): Flow<Boolean> =
        getDataStorePreferences().map { preferences ->
            preferences.contains(booleanPreferencesKey(key))
        }

    override fun containsFloat(key: String): Flow<Boolean> =
        getDataStorePreferences().map { preferences ->
            preferences.contains(floatPreferencesKey(key))
        }

    override fun containsInt(key: String): Flow<Boolean> =
        getDataStorePreferences().map { preferences ->
            preferences.contains(intPreferencesKey(key))
        }

    override fun containsLong(key: String): Flow<Boolean> =
        getDataStorePreferences().map { preferences ->
            preferences.contains(longPreferencesKey(key))
        }

    override fun containsString(key: String): Flow<Boolean> =
        getDataStorePreferences().map { preferences ->
            preferences.contains(stringPreferencesKey(key))
        }

    override suspend fun saveBoolean(key: String, value: Boolean) {
        withContext(appDispatchers.io) {
            preferenceDataStore.edit { preferences ->
                preferences[booleanPreferencesKey(key)] = value
            }
        }
    }

    override suspend fun saveFloat(key: String, value: Float) {
        withContext(appDispatchers.io) {
            preferenceDataStore.edit { preferences ->
                preferences[floatPreferencesKey(key)] = value
            }
        }
    }

    override suspend fun saveInt(key: String, value: Int) {
        withContext(appDispatchers.io) {
            preferenceDataStore.edit { preferences ->
                preferences[intPreferencesKey(key)] = value
            }
        }
    }

    override suspend fun saveLong(key: String, value: Long) {
        withContext(appDispatchers.io) {
            preferenceDataStore.edit { preferences ->
                preferences[longPreferencesKey(key)] = value
            }
        }
    }

    override suspend fun saveString(key: String, value: String) {
        withContext(appDispatchers.io) {
            preferenceDataStore.edit { preferences ->
                preferences[stringPreferencesKey(key)] = value
            }
        }
    }

    override suspend fun removeBoolean(key: String) {
        withContext(appDispatchers.io) {
            try {
                preferenceDataStore.edit { preferences ->
                    preferences.remove(booleanPreferencesKey(key))
                }
            } catch (exception: IOException) {
                Logger.e(fileName, exception)
            }
        }
    }

    override suspend fun removeFloat(key: String) {
        withContext(appDispatchers.io) {
            try {
                preferenceDataStore.edit { preferences ->
                    preferences.remove(floatPreferencesKey(key))
                }
            } catch (exception: IOException) {
                Logger.e(fileName, exception)
            }
        }
    }

    override suspend fun removeInt(key: String) {
        withContext(appDispatchers.io) {
            try {
                preferenceDataStore.edit { preferences ->
                    preferences.remove(intPreferencesKey(key))
                }
            } catch (exception: IOException) {
                Logger.e(fileName, exception)
            }
        }
    }

    override suspend fun removeLong(key: String) {
        withContext(appDispatchers.io) {
            try {
                preferenceDataStore.edit { preferences ->
                    preferences.remove(longPreferencesKey(key))
                }
            } catch (exception: IOException) {
                Logger.e(fileName, exception)
            }
        }
    }

    override suspend fun removeString(key: String) {
        withContext(appDispatchers.io) {
            try {
                preferenceDataStore.edit { preferences ->
                    preferences.remove(stringPreferencesKey(key))
                }
            } catch (exception: IOException) {
                Logger.e(fileName, exception)
            }
        }
    }

    override suspend fun removeAll() {
        withContext(appDispatchers.io) {
            try {
                preferenceDataStore.edit { preferences ->
                    preferences.clear()
                }
            } catch (exception: IOException) {
                Logger.e(fileName, exception)
            }
        }
    }

    private fun getDataStorePreferences(): Flow<DataStorePreferences> =
        preferenceDataStore.data.catch { exception ->
            if (exception is IOException) {
                Logger.e(fileName, exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.flowOn(appDispatchers.io)
}
