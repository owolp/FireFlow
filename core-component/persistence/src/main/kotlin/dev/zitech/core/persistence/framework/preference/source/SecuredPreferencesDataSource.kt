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

package dev.zitech.core.persistence.framework.preference.source

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dev.zitech.core.common.domain.dispatcher.AppDispatchers
import dev.zitech.core.persistence.domain.source.preferences.PreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

internal class SecuredPreferencesDataSource(
    private val appDispatchers: AppDispatchers,
    fileName: String,
    context: Context
) : PreferencesDataSource {

    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    private val encryptedSecuredPreferences = EncryptedSharedPreferences.create(
        fileName,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean> =
        flowOf(encryptedSecuredPreferences.getBoolean(key, defaultValue))
            .flowOn(appDispatchers.io)

    override fun getFloat(key: String, defaultValue: Float): Flow<Float> =
        flowOf(encryptedSecuredPreferences.getFloat(key, defaultValue))
            .flowOn(appDispatchers.io)

    override fun getInt(key: String, defaultValue: Int): Flow<Int> =
        flowOf(encryptedSecuredPreferences.getInt(key, defaultValue))
            .flowOn(appDispatchers.io)

    override fun getLong(key: String, defaultValue: Long): Flow<Long> =
        flowOf(encryptedSecuredPreferences.getLong(key, defaultValue))
            .flowOn(appDispatchers.io)

    override fun getString(key: String, defaultValue: String?): Flow<String?> =
        flowOf(encryptedSecuredPreferences.getString(key, defaultValue))
            .flowOn(appDispatchers.io)

    override fun containsBoolean(key: String): Flow<Boolean> =
        flowOf(encryptedSecuredPreferences.contains(key))
            .flowOn(appDispatchers.io)

    override fun containsFloat(key: String): Flow<Boolean> =
        flowOf(encryptedSecuredPreferences.contains(key))
            .flowOn(appDispatchers.io)

    override fun containsInt(key: String): Flow<Boolean> =
        flowOf(encryptedSecuredPreferences.contains(key))
            .flowOn(appDispatchers.io)

    override fun containsLong(key: String): Flow<Boolean> =
        flowOf(encryptedSecuredPreferences.contains(key))
            .flowOn(appDispatchers.io)

    override fun containsString(key: String): Flow<Boolean> =
        flowOf(encryptedSecuredPreferences.contains(key))
            .flowOn(appDispatchers.io)

    override suspend fun saveBoolean(key: String, value: Boolean) {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences.edit(commit = true) {
                putBoolean(key, value)
            }
        }
    }

    override suspend fun saveFloat(key: String, value: Float) {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences.edit(commit = true) {
                putFloat(key, value)
            }
        }
    }

    override suspend fun saveInt(key: String, value: Int) {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences.edit(commit = true) {
                putInt(key, value)
            }
        }
    }

    override suspend fun saveLong(key: String, value: Long) {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences.edit(commit = true) {
                putLong(key, value)
            }
        }
    }

    override suspend fun saveString(key: String, value: String) {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences.edit(commit = true) {
                putString(key, value)
            }
        }
    }

    override suspend fun removeBoolean(key: String) {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences.edit(commit = true) {
                remove(key)
            }
        }
    }

    override suspend fun removeFloat(key: String) {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences.edit(commit = true) {
                remove(key)
            }
        }
    }

    override suspend fun removeInt(key: String) {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences.edit(commit = true) {
                remove(key)
            }
        }
    }

    override suspend fun removeLong(key: String) {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences.edit(commit = true) {
                remove(key)
            }
        }
    }

    override suspend fun removeString(key: String) {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences.edit(commit = true) {
                remove(key)
            }
        }
    }

    override suspend fun removeAll() {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences.edit(commit = true) {
                clear()
            }
        }
    }
}
