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
import kotlinx.coroutines.flow.first
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

    override suspend fun getBoolean(
        key: String,
        defaultValue: Boolean
    ): Flow<OperationResult<Boolean>> =
        try {
            encryptedSecuredPreferences?.let {
                when (val result = containsBoolean(key).first()) {
                    is OperationResult.Success -> {
                        flowOf(OperationResult.Success(it.getBoolean(key, defaultValue)))
                    }
                    is OperationResult.Failure -> flowOf(OperationResult.Failure(result.error))
                }
            } ?: fallbackPreferencesDataSource.getBoolean(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getBoolean(key)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getBoolean(key)
        }.flowOn(appDispatchers.io)

    override suspend fun getFloat(key: String, defaultValue: Float): Flow<OperationResult<Float>> =
        try {
            encryptedSecuredPreferences?.let {
                when (val result = containsFloat(key).first()) {
                    is OperationResult.Success -> {
                        flowOf(OperationResult.Success(it.getFloat(key, defaultValue)))
                    }
                    is OperationResult.Failure -> flowOf(OperationResult.Failure(result.error))
                }
            } ?: fallbackPreferencesDataSource.getFloat(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getFloat(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getFloat(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override suspend fun getInt(key: String, defaultValue: Int): Flow<OperationResult<Int>> =
        try {
            encryptedSecuredPreferences?.let {
                when (val result = containsInt(key).first()) {
                    is OperationResult.Success -> {
                        flowOf(OperationResult.Success(it.getInt(key, defaultValue)))
                    }
                    is OperationResult.Failure -> flowOf(OperationResult.Failure(result.error))
                }
            } ?: fallbackPreferencesDataSource.getInt(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getInt(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getInt(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override suspend fun getLong(key: String, defaultValue: Long): Flow<OperationResult<Long>> =
        try {
            encryptedSecuredPreferences?.let {
                when (val result = containsLong(key).first()) {
                    is OperationResult.Success -> {
                        flowOf(OperationResult.Success(it.getLong(key, defaultValue)))
                    }
                    is OperationResult.Failure -> flowOf(OperationResult.Failure(result.error))
                }
            } ?: fallbackPreferencesDataSource.getLong(key)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getLong(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getLong(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override suspend fun getString(
        key: String,
        defaultValue: String?
    ): Flow<OperationResult<String>> =
        try {
            encryptedSecuredPreferences?.let {
                when (val result = containsString(key).first()) {
                    is OperationResult.Success -> {
                        flowOf(OperationResult.Success(it.getString(key, defaultValue)!!))
                    }
                    is OperationResult.Failure -> flowOf(OperationResult.Failure(result.error))
                }
            } ?: fallbackPreferencesDataSource.getString(key, defaultValue)
        } catch (e: KeyStoreException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getString(key, defaultValue)
        } catch (e: SecurityException) {
            Logger.e(tag, e)
            fallbackPreferencesDataSource.getString(key, defaultValue)
        }.flowOn(appDispatchers.io)

    override suspend fun removeAll(): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            encryptedSecuredPreferences?.edit(commit = true) {
                clear()
            }?.run {
                OperationResult.Success(Unit)
            } ?: fallbackPreferencesDataSource.removeAll()
        }

    override suspend fun removeBoolean(key: String): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.let {
                    when (val result = getBoolean(key).first()) {
                        is OperationResult.Success -> {
                            it.edit(commit = true) {
                                remove(key)
                            }
                            OperationResult.Success(Unit)
                        }
                        is OperationResult.Failure -> OperationResult.Failure(result.error)
                    }
                } ?: fallbackPreferencesDataSource.removeBoolean(key)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeBoolean(key)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeBoolean(key)
            }
        }

    override suspend fun removeFloat(key: String): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.let {
                    when (val result = getFloat(key).first()) {
                        is OperationResult.Success -> {
                            it.edit(commit = true) {
                                remove(key)
                            }
                            OperationResult.Success(Unit)
                        }
                        is OperationResult.Failure -> OperationResult.Failure(result.error)
                    }
                } ?: fallbackPreferencesDataSource.removeFloat(key)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeFloat(key)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeFloat(key)
            }
        }

    override suspend fun removeInt(key: String): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.let {
                    when (val result = getInt(key).first()) {
                        is OperationResult.Success -> {
                            it.edit(commit = true) {
                                remove(key)
                            }
                            OperationResult.Success(Unit)
                        }
                        is OperationResult.Failure -> OperationResult.Failure(result.error)
                    }
                } ?: fallbackPreferencesDataSource.removeInt(key)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeInt(key)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeInt(key)
            }
        }

    override suspend fun removeLong(key: String): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.let {
                    when (val result = getLong(key).first()) {
                        is OperationResult.Success -> {
                            it.edit(commit = true) {
                                remove(key)
                            }
                            OperationResult.Success(Unit)
                        }
                        is OperationResult.Failure -> OperationResult.Failure(result.error)
                    }
                } ?: fallbackPreferencesDataSource.removeLong(key)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeLong(key)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeLong(key)
            }
        }

    override suspend fun removeString(key: String): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.let {
                    when (val result = getString(key).first()) {
                        is OperationResult.Success -> {
                            it.edit(commit = true) {
                                remove(key)
                            }
                            OperationResult.Success(Unit)
                        }
                        is OperationResult.Failure -> OperationResult.Failure(result.error)
                    }
                } ?: fallbackPreferencesDataSource.removeString(key)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeString(key)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.removeString(key)
            }
        }

    override suspend fun saveBoolean(key: String, value: Boolean): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.let {
                    it.edit(commit = true) {
                        putBoolean(key, value)
                    }
                    OperationResult.Success(Unit)
                } ?: fallbackPreferencesDataSource.saveBoolean(key, value)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveBoolean(key, value)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveBoolean(key, value)
            }
        }

    override suspend fun saveFloat(key: String, value: Float): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.let {
                    it.edit(commit = true) {
                        putFloat(key, value)
                    }
                    OperationResult.Success(Unit)
                } ?: fallbackPreferencesDataSource.saveFloat(key, value)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveFloat(key, value)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveFloat(key, value)
            }
        }

    override suspend fun saveInt(key: String, value: Int): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.let {
                    it.edit(commit = true) {
                        putInt(key, value)
                    }
                    OperationResult.Success(Unit)
                } ?: fallbackPreferencesDataSource.saveInt(key, value)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveInt(key, value)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveInt(key, value)
            }
        }

    override suspend fun saveLong(key: String, value: Long): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.let {
                    it.edit(commit = true) {
                        putLong(key, value)
                    }
                    OperationResult.Success(Unit)
                } ?: fallbackPreferencesDataSource.saveLong(key, value)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveLong(key, value)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveLong(key, value)
            }
        }

    override suspend fun saveString(key: String, value: String): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            try {
                encryptedSecuredPreferences?.let {
                    it.edit(commit = true) {
                        putString(key, value)
                    }
                    OperationResult.Success(Unit)
                } ?: fallbackPreferencesDataSource.saveString(key, value)
            } catch (e: KeyStoreException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveString(key, value)
            } catch (e: SecurityException) {
                Logger.e(tag, e)
                fallbackPreferencesDataSource.saveString(key, value)
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
