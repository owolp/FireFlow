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
import java.util.concurrent.TimeUnit
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

        private const val RELEASE_MINIMUM_FETCH_INTERVAL_IN_HOURS = 12L
        private const val DEBUG_MINIMUM_FETCH_INTERVAL_IN_SECONDS = 1L
    }

    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = when (appConfigProvider.buildMode) {
            BuildMode.RELEASE -> TimeUnit.HOURS.toSeconds(RELEASE_MINIMUM_FETCH_INTERVAL_IN_HOURS)
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
                        }

                        close()
                    }
                    .addOnFailureListener {
                        logError("Failed to Fetch and Activate remote config")

                        if (!isClosedForSend) {
                            trySend(DataResult.Error())
                        } else {
                            logError("fetchAndActivate addOnFailureListener isClosedForSend=true")
                        }

                        close()
                    }
            }
            .addOnFailureListener {
                logError("Failed to set default value to remote config")

                if (!isClosedForSend) {
                    trySend(DataResult.Error())
                } else {
                    logError("init addOnFailureListener isClosedForSend=true")
                }

                close()
            }

        awaitClose()
    }

    @Suppress("TooGenericExceptionCaught")
    override fun getString(key: String): DataResult<String> =
        try {
            DataResult.Success(
                firebaseRemoteConfig.getString(key)
            )
        } catch (e: Exception) {
            logError("getString $key", e)
            DataResult.Error()
        }

    @Suppress("TooGenericExceptionCaught")
    override fun getBoolean(key: String): DataResult<Boolean> =
        try {
            DataResult.Success(
                firebaseRemoteConfig.getBoolean(key)
            )
        } catch (e: Exception) {
            logError("getBoolean $key", e)
            DataResult.Error()
        }

    @Suppress("TooGenericExceptionCaught")
    override fun getDouble(key: String): DataResult<Double> =
        try {
            DataResult.Success(
                firebaseRemoteConfig.getDouble(key)
            )
        } catch (e: Exception) {
            logError("getDouble $key", e)
            DataResult.Error()
        }

    @Suppress("TooGenericExceptionCaught")
    override fun getLong(key: String): DataResult<Long> =
        try {
            DataResult.Success(
                firebaseRemoteConfig.getLong(key)
            )
        } catch (e: Exception) {
            logError("getLong $key", e)
            DataResult.Error()
        }

    private fun logInfo(infoMessage: String) {
        Logger.i(TAG, infoMessage)
    }

    private fun logError(errorMessage: String, exception: Exception? = null) {
        Logger.e(TAG, exception, errorMessage)
    }
}
