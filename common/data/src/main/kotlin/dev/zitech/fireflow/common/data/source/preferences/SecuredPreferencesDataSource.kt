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
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.core.result.OperationResult
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.KeyStoreException
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

internal class SecuredPreferencesDataSource @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val context: Context,
    private val fileName: String,
    private val fallbackPreferencesDataSource: PreferencesDataSource
) : PreferencesDataSource {

    private var encryptedSecuredPreferences: SharedPreferences? = getEncryptedPreferences()
    private val tag = Logger.tag(this::class.java)

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

    override fun getBoolean(key: String, defaultValue: Boolean): Flow<OperationResult<Boolean>> =
        try {
            encryptedSecuredPreferences?.let {
                if (it.contains(key)) {
                    flowOf(OperationResult.Success(it.getBoolean(key, defaultValue)))
                } else {
                    flowOf(OperationResult.Failure(Error.PreferenceNotFound))
                }
            } ?: fallbackPreferencesDataSource.getBoolean(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getBoolean(key)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getBoolean(key)
        }.flowOn(appDispatchers.io)

    override fun getFloat(key: String, defaultValue: Float): Flow<OperationResult<Float>> =
        try {
            encryptedSecuredPreferences?.let {
                if (it.contains(key)) {
                    flowOf(OperationResult.Success(it.getFloat(key, defaultValue)))
                } else {
                    flowOf(OperationResult.Failure(Error.PreferenceNotFound))
                }
            } ?: fallbackPreferencesDataSource.getFloat(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getFloat(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getFloat(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override fun getInt(key: String, defaultValue: Int): Flow<OperationResult<Int>> =
        try {
            encryptedSecuredPreferences?.let {
                if (it.contains(key)) {
                    flowOf(OperationResult.Success(it.getInt(key, defaultValue)))
                } else {
                    flowOf(OperationResult.Failure(Error.PreferenceNotFound))
                }
            } ?: fallbackPreferencesDataSource.getInt(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getInt(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getInt(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override fun getLong(key: String, defaultValue: Long): Flow<OperationResult<Long>> =
        try {
            encryptedSecuredPreferences?.let {
                if (it.contains(key)) {
                    flowOf(OperationResult.Success(it.getLong(key, defaultValue)))
                } else {
                    flowOf(OperationResult.Failure(Error.PreferenceNotFound))
                }
            } ?: fallbackPreferencesDataSource.getLong(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getLong(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getLong(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override fun getString(key: String, defaultValue: String?): Flow<OperationResult<String>> =
        try {
            encryptedSecuredPreferences?.let {
                if (it.contains(key)) {
                    flowOf(OperationResult.Success(it.getString(key, defaultValue)!!))
                } else {
                    flowOf(OperationResult.Failure(Error.PreferenceNotFound))
                }
            } ?: fallbackPreferencesDataSource.getString(key, defaultValue)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getString(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getString(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override suspend fun removeAll(): Flow<OperationResult<Unit>> = callbackFlow {
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences?.edit(commit = true) {
                clear()
                trySend(OperationResult.Success(Unit))
                close()
            } ?: fallbackPreferencesDataSource.removeAll().also {
                trySend(OperationResult.Success(Unit))
                close()
            }
        }
        awaitClose()
    }

    override suspend fun removeBoolean(key: String): Flow<OperationResult<Unit>> = callbackFlow {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    remove(key)
                    trySend(OperationResult.Success(Unit))
                    close()
                } ?: fallbackPreferencesDataSource.removeBoolean(key).also {
                    trySend(OperationResult.Success(Unit))
                    close()
                }
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeBoolean(key)
                trySend(OperationResult.Success(Unit))
                close()
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeBoolean(key)
                trySend(OperationResult.Success(Unit))
                close()
            }
        }
        awaitClose()
    }

    override suspend fun removeFloat(key: String): Flow<OperationResult<Unit>> = callbackFlow {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    remove(key)
                    trySend(OperationResult.Success(Unit))
                    close()
                } ?: fallbackPreferencesDataSource.removeFloat(key).also {
                    trySend(OperationResult.Success(Unit))
                    close()
                }
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeFloat(key)
                trySend(OperationResult.Success(Unit))
                close()
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeFloat(key)
                trySend(OperationResult.Success(Unit))
                close()
            }
        }
        awaitClose()
    }

    override suspend fun removeInt(key: String): Flow<OperationResult<Unit>> = callbackFlow {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    remove(key)
                    trySend(OperationResult.Success(Unit))
                    close()
                } ?: fallbackPreferencesDataSource.removeInt(key).also {
                    trySend(OperationResult.Success(Unit))
                    close()
                }
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeInt(key)
                trySend(OperationResult.Success(Unit))
                close()
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeInt(key)
                trySend(OperationResult.Success(Unit))
                close()
            }
        }
        awaitClose()
    }

    override suspend fun removeLong(key: String): Flow<OperationResult<Unit>> = callbackFlow {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    remove(key)
                    trySend(OperationResult.Success(Unit))
                    close()
                } ?: fallbackPreferencesDataSource.removeLong(key).also {
                    trySend(OperationResult.Success(Unit))
                    close()
                }
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeLong(key)
                trySend(OperationResult.Success(Unit))
                close()
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeLong(key)
                trySend(OperationResult.Success(Unit))
                close()
            }
        }
        awaitClose()
    }

    override suspend fun removeString(key: String): Flow<OperationResult<Unit>> = callbackFlow {
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.edit(commit = true) {
                    remove(key)
                    trySend(OperationResult.Success(Unit))
                    close()
                } ?: fallbackPreferencesDataSource.removeString(key).also {
                    trySend(OperationResult.Success(Unit))
                    close()
                }
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeString(key)
                trySend(OperationResult.Success(Unit))
                close()
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeString(key)
                trySend(OperationResult.Success(Unit))
                close()
            }
        }
        awaitClose()
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
