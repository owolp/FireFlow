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

package dev.zitech.fireflow.common.data.source.preferences

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
import dev.zitech.fireflow.core.dispatcher.AppDispatchers
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.core.result.OperationResult
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class StandardPreferencesDataSource @Inject constructor(
    context: Context,
    private val appDispatchers: AppDispatchers,
    private val fileName: String
) : PreferencesDataSource {

    private val Context.dataStore: DataStore<DataStorePreferences> by preferencesDataStore(
        name = fileName
    )

    private val preferenceDataStore: DataStore<DataStorePreferences> = context.dataStore

    override fun containsBoolean(key: String): Flow<OperationResult<Boolean>> =
        getDataStorePreferences().map { preferences ->
            OperationResult.Success(
                preferences.contains(booleanPreferencesKey(key))
            )
        }

    override fun containsFloat(key: String): Flow<OperationResult<Boolean>> =
        getDataStorePreferences().map { preferences ->
            OperationResult.Success(
                preferences.contains(floatPreferencesKey(key))
            )
        }

    override fun containsInt(key: String): Flow<OperationResult<Boolean>> =
        getDataStorePreferences().map { preferences ->
            OperationResult.Success(
                preferences.contains(intPreferencesKey(key))
            )
        }

    override fun containsLong(key: String): Flow<OperationResult<Boolean>> =
        getDataStorePreferences().map { preferences ->
            OperationResult.Success(
                preferences.contains(longPreferencesKey(key))
            )
        }

    override fun containsString(key: String): Flow<OperationResult<Boolean>> =
        getDataStorePreferences().map { preferences ->
            OperationResult.Success(
                preferences.contains(stringPreferencesKey(key))
            )
        }

    override suspend fun getBoolean(
        key: String,
        defaultValue: Boolean
    ): Flow<OperationResult<Boolean>> =
        when (val result = containsBoolean(key).first()) {
            is OperationResult.Success -> {
                getDataStorePreferences().map { preferences ->
                    preferences[booleanPreferencesKey(key)]?.let { OperationResult.Success(it) }
                        ?: OperationResult.Failure(Error.PreferenceNotFound)
                }
            }
            is OperationResult.Failure -> flowOf(OperationResult.Failure(result.error))
        }

    override suspend fun getFloat(key: String, defaultValue: Float): Flow<OperationResult<Float>> =
        when (val result = containsFloat(key).first()) {
            is OperationResult.Success -> {
                getDataStorePreferences().map { preferences ->
                    preferences[floatPreferencesKey(key)]?.let { OperationResult.Success(it) }
                        ?: OperationResult.Failure(Error.PreferenceNotFound)
                }
            }
            is OperationResult.Failure -> flowOf(OperationResult.Failure(result.error))
        }

    override suspend fun getInt(key: String, defaultValue: Int): Flow<OperationResult<Int>> =
        when (val result = containsInt(key).first()) {
            is OperationResult.Success -> {
                getDataStorePreferences().map { preferences ->
                    preferences[intPreferencesKey(key)]?.let { OperationResult.Success(it) }
                        ?: OperationResult.Failure(Error.PreferenceNotFound)
                }
            }
            is OperationResult.Failure -> flowOf(OperationResult.Failure(result.error))
        }

    override suspend fun getLong(key: String, defaultValue: Long): Flow<OperationResult<Long>> =
        when (val result = containsLong(key).first()) {
            is OperationResult.Success -> {
                getDataStorePreferences().map { preferences ->
                    preferences[longPreferencesKey(key)]?.let { OperationResult.Success(it) }
                        ?: OperationResult.Failure(Error.PreferenceNotFound)
                }
            }
            is OperationResult.Failure -> flowOf(OperationResult.Failure(result.error))
        }

    override suspend fun getString(
        key: String,
        defaultValue: String?
    ): Flow<OperationResult<String>> =
        when (val result = containsString(key).first()) {
            is OperationResult.Success -> {
                getDataStorePreferences().map { preferences ->
                    preferences[stringPreferencesKey(key)]?.let { OperationResult.Success(it) }
                        ?: OperationResult.Failure(Error.PreferenceNotFound)
                }
            }
            is OperationResult.Failure -> flowOf(OperationResult.Failure(result.error))
        }

    override suspend fun removeAll(): OperationResult<Unit> = withContext(appDispatchers.io) {
        try {
            preferenceDataStore.edit { preferences ->
                preferences.clear()
            }
            OperationResult.Success(Unit)
        } catch (exception: IOException) {
            Logger.e(fileName, exception)
            OperationResult.Failure(
                Error.Fatal(
                    throwable = exception,
                    type = Error.Fatal.Type.DISK
                )
            )
        }
    }

    override suspend fun removeBoolean(key: String): OperationResult<Unit> =
        when (val result = getBoolean(key).first()) {
            is OperationResult.Success -> {
                try {
                    preferenceDataStore.edit { preferences ->
                        preferences.remove(booleanPreferencesKey(key))
                    }
                    OperationResult.Success(Unit)
                } catch (e: IOException) {
                    Logger.e(fileName, e)
                    OperationResult.Failure(
                        Error.Fatal(
                            throwable = e,
                            type = Error.Fatal.Type.DISK
                        )
                    )
                }
            }
            is OperationResult.Failure -> OperationResult.Failure(result.error)
        }

    override suspend fun removeFloat(key: String): OperationResult<Unit> =
        when (val result = getBoolean(key).first()) {
            is OperationResult.Success -> {
                try {
                    preferenceDataStore.edit { preferences ->
                        preferences.remove(floatPreferencesKey(key))
                    }
                    OperationResult.Success(Unit)
                } catch (e: IOException) {
                    Logger.e(fileName, e)
                    OperationResult.Failure(
                        Error.Fatal(
                            throwable = e,
                            type = Error.Fatal.Type.DISK
                        )
                    )
                }
            }
            is OperationResult.Failure -> OperationResult.Failure(result.error)
        }

    override suspend fun removeInt(key: String): OperationResult<Unit> =
        when (val result = getInt(key).first()) {
            is OperationResult.Success -> {
                try {
                    preferenceDataStore.edit { preferences ->
                        preferences.remove(intPreferencesKey(key))
                    }
                    OperationResult.Success(Unit)
                } catch (e: IOException) {
                    Logger.e(fileName, e)
                    OperationResult.Failure(
                        Error.Fatal(
                            throwable = e,
                            type = Error.Fatal.Type.DISK
                        )
                    )
                }
            }
            is OperationResult.Failure -> OperationResult.Failure(result.error)
        }

    override suspend fun removeLong(key: String): OperationResult<Unit> =
        when (val result = getLong(key).first()) {
            is OperationResult.Success -> {
                try {
                    preferenceDataStore.edit { preferences ->
                        preferences.remove(longPreferencesKey(key))
                    }
                    OperationResult.Success(Unit)
                } catch (e: IOException) {
                    Logger.e(fileName, e)
                    OperationResult.Failure(
                        Error.Fatal(
                            throwable = e,
                            type = Error.Fatal.Type.DISK
                        )
                    )
                }
            }
            is OperationResult.Failure -> OperationResult.Failure(result.error)
        }

    override suspend fun removeString(key: String): OperationResult<Unit> =
        when (val result = getString(key).first()) {
            is OperationResult.Success -> {
                try {
                    preferenceDataStore.edit { preferences ->
                        preferences.remove(stringPreferencesKey(key))
                    }
                    OperationResult.Success(Unit)
                } catch (e: IOException) {
                    Logger.e(fileName, e)
                    OperationResult.Failure(
                        Error.Fatal(
                            throwable = e,
                            type = Error.Fatal.Type.DISK
                        )
                    )
                }
            }
            is OperationResult.Failure -> OperationResult.Failure(result.error)
        }

    override suspend fun saveBoolean(key: String, value: Boolean): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                preferenceDataStore.edit { preferences ->
                    preferences[booleanPreferencesKey(key)] = value
                }
                OperationResult.Success(Unit)
            } catch (e: IOException) {
                Logger.e(fileName, e)
                OperationResult.Failure(
                    Error.Fatal(
                        throwable = e,
                        type = Error.Fatal.Type.DISK
                    )
                )
            }
        }

    override suspend fun saveFloat(key: String, value: Float): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                preferenceDataStore.edit { preferences ->
                    preferences[floatPreferencesKey(key)] = value
                }
                OperationResult.Success(Unit)
            } catch (e: IOException) {
                Logger.e(fileName, e)
                OperationResult.Failure(
                    Error.Fatal(
                        throwable = e,
                        type = Error.Fatal.Type.DISK
                    )
                )
            }
        }

    override suspend fun saveInt(key: String, value: Int): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                preferenceDataStore.edit { preferences ->
                    preferences[intPreferencesKey(key)] = value
                }
                OperationResult.Success(Unit)
            } catch (e: IOException) {
                Logger.e(fileName, e)
                OperationResult.Failure(
                    Error.Fatal(
                        throwable = e,
                        type = Error.Fatal.Type.DISK
                    )
                )
            }
        }

    override suspend fun saveLong(key: String, value: Long): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                preferenceDataStore.edit { preferences ->
                    preferences[longPreferencesKey(key)] = value
                }
                OperationResult.Success(Unit)
            } catch (e: IOException) {
                Logger.e(fileName, e)
                OperationResult.Failure(
                    Error.Fatal(
                        throwable = e,
                        type = Error.Fatal.Type.DISK
                    )
                )
            }
        }

    override suspend fun saveString(key: String, value: String) =
        withContext(appDispatchers.io) {
            try {
                preferenceDataStore.edit { preferences ->
                    preferences[stringPreferencesKey(key)] = value
                }
                OperationResult.Success(Unit)
            } catch (e: IOException) {
                Logger.e(fileName, e)
                OperationResult.Failure(
                    Error.Fatal(
                        throwable = e,
                        type = Error.Fatal.Type.DISK
                    )
                )
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
