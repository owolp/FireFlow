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

import com.huawei.agconnect.remoteconfig.AGConnectConfig
import dev.zitech.fireflow.common.data.remote.configurator.DefaultConfigValues.getDefaultConfigValues
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.error.Error.FailedToFetch.Type.BOOLEAN
import dev.zitech.fireflow.core.error.Error.FailedToFetch.Type.DOUBLE
import dev.zitech.fireflow.core.error.Error.FailedToFetch.Type.LONG
import dev.zitech.fireflow.core.error.Error.FailedToFetch.Type.STRING
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.core.work.Work
import dev.zitech.fireflow.core.work.WorkError
import dev.zitech.fireflow.core.work.WorkSuccess
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@OptIn(ExperimentalCoroutinesApi::class)
internal class RemoteConfiguratorImpl @Inject constructor() : RemoteConfigurator {

    private val huaweiRemoteConfig = AGConnectConfig.getInstance()

    @Suppress("TooGenericExceptionCaught")
    override fun getBoolean(key: String): Work<Boolean> =
        try {
            WorkSuccess(
                huaweiRemoteConfig.getValueAsBoolean(key)
            )
        } catch (e: Exception) {
            logError("getBoolean $key", e)
            WorkError(
                Error.FailedToFetch(
                    key = key,
                    type = BOOLEAN
                )
            )
        }

    @Suppress("TooGenericExceptionCaught")
    override fun getDouble(key: String): Work<Double> =
        try {
            WorkSuccess(
                huaweiRemoteConfig.getValueAsDouble(key)
            )
        } catch (e: Exception) {
            logError("getDouble $key", e)
            WorkError(
                Error.FailedToFetch(
                    key = key,
                    type = DOUBLE
                )
            )
        }

    @Suppress("TooGenericExceptionCaught")
    override fun getLong(key: String): Work<Long> =
        try {
            WorkSuccess(
                huaweiRemoteConfig.getValueAsLong(key)
            )
        } catch (e: Exception) {
            logError("getLong $key", e)
            WorkError(
                Error.FailedToFetch(
                    key = key,
                    type = LONG
                )
            )
        }

    @Suppress("TooGenericExceptionCaught")
    override fun getString(key: String): Work<String> =
        try {
            WorkSuccess(
                huaweiRemoteConfig.getValueAsString(key)
            )
        } catch (e: Exception) {
            logError("getString $key", e)
            WorkError(
                Error.FailedToFetch(
                    key = key,
                    type = STRING
                )
            )
        }

    override fun init(): Flow<Work<Unit>> = callbackFlow {
        logInfo("Set default values")
        huaweiRemoteConfig
            .applyDefault(getDefaultConfigValues())
        logInfo("Default values set")

        huaweiRemoteConfig.fetch()
            .addOnSuccessListener {
                logInfo("Fetch completed")

                huaweiRemoteConfig.apply(it)
                logInfo("Applied completed")

                if (!isClosedForSend) {
                    trySend(WorkSuccess(Unit))
                } else {
                    logError("fetch addOnSuccessListener isClosedForSend=true")
                }

                close()
            }
            .addOnFailureListener {
                logError("Failed to fetch remote config")

                if (!isClosedForSend) {
                    trySend(
                        WorkError(
                            Error.FailedToFetch(
                                type = Error.FailedToFetch.Type.INIT
                            )
                        )
                    )
                } else {
                    logError("fetch addOnFailureListener isClosedForSend=true")
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
    }
}
