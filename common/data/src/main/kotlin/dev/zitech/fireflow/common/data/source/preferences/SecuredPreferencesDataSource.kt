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
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dev.zitech.fireflow.core.dispatcher.AppDispatchers
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.core.result.OperationResult
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.KeyStoreException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

internal class SecuredPreferencesDataSource @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val context: Context,
    private val fileName: String,
    private val fallbackPreferencesDataSource: PreferencesDataSource
) : PreferencesDataSource {

    private var encryptedSecuredPreferences: SharedPreferences? = null
    private val tag = Logger.tag(this::class.java)

    init {
        getEncryptedPreferences()
    }

    override fun containsBoolean(key: String): Flow<OperationResult<Boolean>> =
        try {
            encryptedSecuredPreferences?.let { flowOf(OperationResult.Success(it.contains(key))) }
                ?: fallbackPreferencesDataSource.containsBoolean(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.containsBoolean(key)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.containsBoolean(key)
        }.flowOn(appDispatchers.io)

    override fun containsFloat(key: String): Flow<OperationResult<Boolean>> =
        try {
            encryptedSecuredPreferences?.let { flowOf(OperationResult.Success(it.contains(key))) }
                ?: fallbackPreferencesDataSource.containsFloat(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.containsFloat(key)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.containsFloat(key)
        }.flowOn(appDispatchers.io)

    override fun containsInt(key: String): Flow<OperationResult<Boolean>> =
        try {
            encryptedSecuredPreferences?.let { flowOf(OperationResult.Success(it.contains(key))) }
                ?: fallbackPreferencesDataSource.containsInt(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.containsInt(key)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.containsInt(key)
        }.flowOn(appDispatchers.io)

    override fun containsLong(key: String): Flow<OperationResult<Boolean>> =
        try {
            encryptedSecuredPreferences?.let { flowOf(OperationResult.Success(it.contains(key))) }
                ?: fallbackPreferencesDataSource.containsLong(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.containsLong(key)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.containsLong(key)
        }.flowOn(appDispatchers.io)

    override fun containsString(key: String): Flow<OperationResult<Boolean>> =
        try {
            encryptedSecuredPreferences?.let { flowOf(OperationResult.Success(it.contains(key))) }
                ?: fallbackPreferencesDataSource.containsString(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.containsString(key)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.containsString(key)
        }.flowOn(appDispatchers.io)

    override fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean> =
        try {
            encryptedSecuredPreferences?.let { flowOf(it.getBoolean(key, defaultValue)) }
                ?: fallbackPreferencesDataSource.getBoolean(key, defaultValue)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getBoolean(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getBoolean(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override fun getFloat(key: String, defaultValue: Float): Flow<Float> =
        try {
            encryptedSecuredPreferences?.let { flowOf(it.getFloat(key, defaultValue)) }
                ?: fallbackPreferencesDataSource.getFloat(key, defaultValue)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getFloat(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getFloat(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override fun getInt(key: String, defaultValue: Int): Flow<Int> =
        try {
            encryptedSecuredPreferences?.let { flowOf(it.getInt(key, defaultValue)) }
                ?: fallbackPreferencesDataSource.getInt(key, defaultValue)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getInt(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getInt(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override fun getLong(key: String, defaultValue: Long): Flow<Long> =
        try {
            encryptedSecuredPreferences?.let { flowOf(it.getLong(key, defaultValue)) }
                ?: fallbackPreferencesDataSource.getLong(key, defaultValue)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getLong(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getLong(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override fun getString(key: String, defaultValue: String?): Flow<String?> =
        try {
            encryptedSecuredPreferences?.let { flowOf(it.getString(key, defaultValue)) }
                ?: fallbackPreferencesDataSource.getString(key, defaultValue)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getString(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getString(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override suspend fun removeAll() {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences?.edit(commit = true) {
                clear()
            } ?: fallbackPreferencesDataSource.removeAll()
        }
    }

    override suspend fun removeBoolean(key: String) {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    remove(key)
                } ?: fallbackPreferencesDataSource.removeBoolean(key)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeBoolean(key)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeBoolean(key)
            }
        }
    }

    override suspend fun removeFloat(key: String) {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    remove(key)
                } ?: fallbackPreferencesDataSource.removeFloat(key)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeFloat(key)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeFloat(key)
            }
        }
    }

    override suspend fun removeInt(key: String) {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    remove(key)
                } ?: fallbackPreferencesDataSource.removeInt(key)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeInt(key)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeInt(key)
            }
        }
    }

    override suspend fun removeLong(key: String) {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    remove(key)
                } ?: fallbackPreferencesDataSource.removeLong(key)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeLong(key)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeLong(key)
            }
        }
    }

    override suspend fun removeString(key: String) {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    remove(key)
                } ?: fallbackPreferencesDataSource.removeString(key)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeString(key)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeString(key)
            }
        }
    }

    override suspend fun saveBoolean(key: String, value: Boolean) {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    putBoolean(key, value)
                } ?: fallbackPreferencesDataSource.saveBoolean(key, value)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveBoolean(key, value)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveBoolean(key, value)
            }
        }
    }

    override suspend fun saveFloat(key: String, value: Float) {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    putFloat(key, value)
                } ?: fallbackPreferencesDataSource.saveFloat(key, value)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveFloat(key, value)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveFloat(key, value)
            }
        }
    }

    override suspend fun saveInt(key: String, value: Int) {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    putInt(key, value)
                } ?: fallbackPreferencesDataSource.saveInt(key, value)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveInt(key, value)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveInt(key, value)
            }
        }
    }

    override suspend fun saveLong(key: String, value: Long) {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    putLong(key, value)
                } ?: fallbackPreferencesDataSource.saveLong(key, value)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveLong(key, value)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveLong(key, value)
            }
        }
    }

    override suspend fun saveString(key: String, value: String) {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    putString(key, value)
                } ?: fallbackPreferencesDataSource.saveString(key, value)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveString(key, value)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveString(key, value)
            }
        }
    }

    private fun getEncryptedPreferences(): SharedPreferences? =
        try {
            val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
            val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

            EncryptedSharedPreferences.create(
                fileName,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: GeneralSecurityException) {
            Logger.e(tag, e)
            null
        } catch (e: IOException) {
            Logger.e(tag, e)
            null
        }
}
