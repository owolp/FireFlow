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

package dev.zitech.fireflow.common.data.remote.configurator

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dev.zitech.fireflow.common.data.remote.configurator.DefaultConfigValues.getDefaultConfigValues
import dev.zitech.fireflow.core.applicationconfig.AppConfigProvider
import dev.zitech.fireflow.core.applicationconfig.BuildMode
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.error.Error.FailedToFetch.Type.BOOLEAN
import dev.zitech.fireflow.core.error.Error.FailedToFetch.Type.DOUBLE
import dev.zitech.fireflow.core.error.Error.FailedToFetch.Type.INIT
import dev.zitech.fireflow.core.error.Error.FailedToFetch.Type.LONG
import dev.zitech.fireflow.core.error.Error.FailedToFetch.Type.STRING
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal class RemoteConfiguratorImpl @Inject constructor(
    private val appConfigProvider: AppConfigProvider
) : RemoteConfigurator {

    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = when (appConfigProvider.buildMode) {
            BuildMode.RELEASE -> TimeUnit.HOURS.toSeconds(RELEASE_MINIMUM_FETCH_INTERVAL_IN_HOURS)
            BuildMode.DEBUG -> DEBUG_MINIMUM_FETCH_INTERVAL_IN_SECONDS
        }
    }

    private val firebaseRemoteConfig = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(configSettings)
    }

    @Suppress("TooGenericExceptionCaught")
    override fun getBoolean(key: String): OperationResult<Boolean> =
        try {
            Success(
                firebaseRemoteConfig.getBoolean(key)
            )
        } catch (e: Exception) {
            logError("getBoolean $key", e)
            Failure(
                Error.FailedToFetch(
                    key = key,
                    type = BOOLEAN
                )
            )
        }

    @Suppress("TooGenericExceptionCaught")
    override fun getDouble(key: String): OperationResult<Double> =
        try {
            Success(
                firebaseRemoteConfig.getDouble(key)
            )
        } catch (e: Exception) {
            logError("getDouble $key", e)
            Failure(
                Error.FailedToFetch(
                    key = key,
                    type = DOUBLE
                )
            )
        }

    @Suppress("TooGenericExceptionCaught")
    override fun getLong(key: String): OperationResult<Long> =
        try {
            Success(
                firebaseRemoteConfig.getLong(key)
            )
        } catch (e: Exception) {
            logError("getLong $key", e)
            Failure(
                Error.FailedToFetch(
                    key = key,
                    type = LONG
                )
            )
        }

    @Suppress("TooGenericExceptionCaught")
    override fun getString(key: String): OperationResult<String> =
        try {
            Success(
                firebaseRemoteConfig.getString(key)
            )
        } catch (e: Exception) {
            logError("getString $key", e)
            Failure(
                Error.FailedToFetch(
                    key = key,
                    type = STRING
                )
            )
        }

    override fun init(): Flow<OperationResult<Unit>> = callbackFlow {
        firebaseRemoteConfig
            .setDefaultsAsync(getDefaultConfigValues())
            .addOnSuccessListener {
                logInfo("Default values set")

                firebaseRemoteConfig.fetchAndActivate()
                    .addOnSuccessListener {
                        logInfo("Fetch and Activated completed")

                        if (!isClosedForSend) {
                            trySend(
                                Failure(
                                    Error.FailedToFetch(
                                        type = INIT
                                    )
                                )
                            )
                        } else {
                            logError("fetchAndActivate addOnSuccessListener isClosedForSend=true")
                        }

                        close()
                    }
                    .addOnFailureListener {
                        logError("Failed to Fetch and Activate remote config")

                        if (!isClosedForSend) {
                            trySend(
                                Failure(
                                    Error.FailedToFetch(
                                        type = INIT
                                    )
                                )
                            )
                        } else {
                            logError("fetchAndActivate addOnFailureListener isClosedForSend=true")
                        }

                        close()
                    }
            }
            .addOnFailureListener {
                logError("Failed to set default value to remote config")

                if (!isClosedForSend) {
                    trySend(
                        Failure(
                            Error.FailedToFetch(
                                type = INIT
                            )
                        )
                    )
                } else {
                    logError("init addOnFailureListener isClosedForSend=true")
                }

                close()
            }

        awaitClose()
    }

    private fun logError(errorMessage: String, exception: Exception? = null) {
        Logger.e(TAG, exception, errorMessage)
    }

    private fun logInfo(infoMessage: String) {
        Logger.i(TAG, infoMessage)
    }

    companion object {
        private const val TAG = "RemoteConfigurator"

        private const val RELEASE_MINIMUM_FETCH_INTERVAL_IN_HOURS = 12L
        private const val DEBUG_MINIMUM_FETCH_INTERVAL_IN_SECONDS = 1L
    }
}
