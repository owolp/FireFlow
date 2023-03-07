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

package dev.zitech.core.remoteconfig.framework.configurator

import com.huawei.agconnect.remoteconfig.AGConnectConfig
import dev.zitech.core.common.domain.exception.FireFlowException
import dev.zitech.core.common.domain.exception.FireFlowException.FailedToFetch.Type.BOOLEAN
import dev.zitech.core.common.domain.exception.FireFlowException.FailedToFetch.Type.DOUBLE
import dev.zitech.core.common.domain.exception.FireFlowException.FailedToFetch.Type.INIT
import dev.zitech.core.common.domain.exception.FireFlowException.FailedToFetch.Type.LONG
import dev.zitech.core.common.domain.exception.FireFlowException.FailedToFetch.Type.STRING
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.DataError
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.domain.model.DataSuccess
import dev.zitech.core.remoteconfig.domain.usecase.GetDefaultConfigValuesUseCase
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
internal class RemoteConfiguratorImpl @Inject constructor(
    private val getDefaultConfigValuesUseCase: GetDefaultConfigValuesUseCase
) : RemoteConfigurator {

    companion object {
        private const val TAG = "RemoteConfigurator"
    }

    private val huaweiRemoteConfig = AGConnectConfig.getInstance()

    override fun init(): Flow<DataResult<Unit>> = callbackFlow {
        logInfo("Set default values")
        huaweiRemoteConfig
            .applyDefault(getDefaultConfigValuesUseCase())
        logInfo("Default values set")

        huaweiRemoteConfig.fetch()
            .addOnSuccessListener {
                logInfo("Fetch completed")

                huaweiRemoteConfig.apply(it)
                logInfo("Applied completed")

                if (!isClosedForSend) {
                    trySend(DataSuccess(Unit))
                } else {
                    logError("fetch addOnSuccessListener isClosedForSend=true")
                }

                close()
            }
            .addOnFailureListener {
                logError("Failed to fetch remote config")

                if (!isClosedForSend) {
                    trySend(
                        DataError(
                            FireFlowException.FailedToFetch(
                                type = INIT
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

    @Suppress("TooGenericExceptionCaught")
    override fun getString(key: String): DataResult<String> =
        try {
            DataSuccess(
                huaweiRemoteConfig.getValueAsString(key)
            )
        } catch (e: Exception) {
            logError("getString $key", e)
            DataError(
                FireFlowException.FailedToFetch(
                    key = key,
                    type = STRING
                )
            )
        }

    @Suppress("TooGenericExceptionCaught")
    override fun getBoolean(key: String): DataResult<Boolean> =
        try {
            DataSuccess(
                huaweiRemoteConfig.getValueAsBoolean(key)
            )
        } catch (e: Exception) {
            logError("getBoolean $key", e)
            DataError(
                FireFlowException.FailedToFetch(
                    key = key,
                    type = BOOLEAN
                )
            )
        }

    @Suppress("TooGenericExceptionCaught")
    override fun getDouble(key: String): DataResult<Double> =
        try {
            DataSuccess(
                huaweiRemoteConfig.getValueAsDouble(key)
            )
        } catch (e: Exception) {
            logError("getDouble $key", e)
            DataError(
                FireFlowException.FailedToFetch(
                    key = key,
                    type = DOUBLE
                )
            )
        }

    @Suppress("TooGenericExceptionCaught")
    override fun getLong(key: String): DataResult<Long> =
        try {
            DataSuccess(
                huaweiRemoteConfig.getValueAsLong(key)
            )
        } catch (e: Exception) {
            logError("getLong $key", e)
            DataError(
                FireFlowException.FailedToFetch(
                    key = key,
                    type = LONG
                )
            )
        }

    private fun logInfo(infoMessage: String) {
        Logger.i(TAG, infoMessage)
    }

    private fun logError(errorMessage: String, exception: Exception? = null) {
        Logger.e(TAG, exception, errorMessage)
    }
}
