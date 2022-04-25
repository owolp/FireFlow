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

package dev.zitech.core.remoteconfig.framework.configurator

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.framework.logger.AppConfigProvider
import dev.zitech.core.common.framework.logger.BuildMode
import dev.zitech.core.common.framework.logger.Logger
import dev.zitech.core.remoteconfig.domain.usecase.GetDefaultConfigValuesUseCase
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
internal class RemoteConfiguratorImpl @Inject constructor(
    private val appConfigProvider: AppConfigProvider,
    private val getDefaultConfigValuesUseCase: GetDefaultConfigValuesUseCase
) : RemoteConfigurator {

    companion object {
        private const val TAG = "RemoteConfigurator"
        private const val RELEASE_MINIMUM_FETCH_INTERVAL_IN_SECONDS = 1 * 720000 * 60L // 12 hours
        private const val DEBUG_MINIMUM_FETCH_INTERVAL_IN_SECONDS = 1L
    }

    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = when (appConfigProvider.buildMode) {
            BuildMode.RELEASE -> RELEASE_MINIMUM_FETCH_INTERVAL_IN_SECONDS
            BuildMode.DEBUG -> DEBUG_MINIMUM_FETCH_INTERVAL_IN_SECONDS
        }
    }

    private val firebaseRemoteConfig = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(configSettings)
    }

    override fun init() = callbackFlow {
        firebaseRemoteConfig
            .setDefaultsAsync(getDefaultConfigValuesUseCase())
            .addOnSuccessListener {
                logInfo("Default values set")

                firebaseRemoteConfig.fetchAndActivate()
                    .addOnSuccessListener {
                        logInfo("Fetch and Activated completed")
                        if (!isClosedForSend) {
                            trySend(DataResult.Success(Unit))
                        } else {
                            logError("fetchAndActivate addOnSuccessListener isClosedForSend=true")
                            trySend(DataResult.Error())
                        }

                        close()
                    }
                    .addOnFailureListener {
                        logError("Failed to fetch and activate remote config")
                        if (!isClosedForSend) {
                            trySend(DataResult.Success(Unit))
                        } else {
                            logError("fetchAndActivate addOnFailureListener isClosedForSend=true")
                            trySend(DataResult.Error())
                        }
                        close()
                    }
            }
            .addOnFailureListener {
                logError("Failed to set default value to remote config")
                if (!this.isClosedForSend) {
                    trySend(DataResult.Success(Unit))
                } else {
                    logError("init addOnSuccessListener isClosedForSend=true")
                    trySend(DataResult.Error())
                }

                close()
            }

        awaitClose()
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getString(key: String): String? =
        try {
            firebaseRemoteConfig.getString(key)
        } catch (e: Exception) {
            Logger.e(TAG, e, "getString $key")
            null
        }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getBoolean(key: String): Boolean? =
        try {
            firebaseRemoteConfig.getBoolean(key)
        } catch (e: Exception) {
            Logger.e(TAG, e, "getBoolean $key")
            null
        }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getDouble(key: String): Double? =
        try {
            firebaseRemoteConfig.getDouble(key)
        } catch (e: Exception) {
            Logger.e(TAG, e, "getDouble $key")
            null
        }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getLong(key: String): Long? =
        try {
            firebaseRemoteConfig.getLong(key)
        } catch (e: Exception) {
            Logger.e(TAG, e, "getLong $key")
            null
        }

    private fun logInfo(infoMessage: String) {
        Logger.i(TAG, infoMessage)
    }

    private fun logError(errorMessage: String) {
        Logger.e(TAG, errorMessage)
    }
}
