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

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.flow.flowOf

@Suppress("TooManyFunctions")
internal class SecuredPreferencesDataSource(
    context: Context,
    fileName: String
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

    override fun getBoolean(key: String, defaultValue: Boolean) =
        flowOf(encryptedSecuredPreferences.getBoolean(key, defaultValue))

    override fun getFloat(key: String, defaultValue: Float) =
        flowOf(encryptedSecuredPreferences.getFloat(key, defaultValue))

    override fun getInt(key: String, defaultValue: Int) =
        flowOf(encryptedSecuredPreferences.getInt(key, defaultValue))

    override fun getLong(key: String, defaultValue: Long) =
        flowOf(encryptedSecuredPreferences.getLong(key, defaultValue))

    override fun getString(key: String, defaultValue: String?) =
        flowOf(encryptedSecuredPreferences.getString(key, defaultValue))

    override fun containsBoolean(key: String) = flowOf(encryptedSecuredPreferences.contains(key))

    override fun containsFloat(key: String) = flowOf(encryptedSecuredPreferences.contains(key))

    override fun containsInt(key: String) = flowOf(encryptedSecuredPreferences.contains(key))

    override fun containsLong(key: String) = flowOf(encryptedSecuredPreferences.contains(key))

    override fun containsString(key: String) = flowOf(encryptedSecuredPreferences.contains(key))

    override suspend fun saveBoolean(key: String, value: Boolean) {
        encryptedSecuredPreferences.edit(commit = true) {
            putBoolean(key, value)
        }
    }

    override suspend fun saveFloat(key: String, value: Float) {
        encryptedSecuredPreferences.edit(commit = true) {
            putFloat(key, value)
        }
    }

    override suspend fun saveInt(key: String, value: Int) {
        encryptedSecuredPreferences.edit(commit = true) {
            putInt(key, value)
        }
    }

    override suspend fun saveLong(key: String, value: Long) {
        encryptedSecuredPreferences.edit(commit = true) {
            putLong(key, value)
        }
    }

    override suspend fun saveString(key: String, value: String) {
        encryptedSecuredPreferences.edit(commit = true) {
            putString(key, value)
        }
    }

    override suspend fun removeBoolean(key: String) {
        encryptedSecuredPreferences.edit(commit = true) {
            remove(key)
        }
    }

    override suspend fun removeFloat(key: String) {
        encryptedSecuredPreferences.edit(commit = true) {
            remove(key)
        }
    }

    override suspend fun removeInt(key: String) {
        encryptedSecuredPreferences.edit(commit = true) {
            remove(key)
        }
    }

    override suspend fun removeLong(key: String) {
        encryptedSecuredPreferences.edit(commit = true) {
            remove(key)
        }
    }

    override suspend fun removeString(key: String) {
        encryptedSecuredPreferences.edit(commit = true) {
            remove(key)
        }
    }

    override suspend fun removeAll() {
        encryptedSecuredPreferences.edit(commit = true) {
            clear()
        }
    }
}
