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

package dev.zitech.fireflow.common.domain.repository.reporter

import dev.zitech.fireflow.core.result.OperationResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing crash reporting.
 */
interface CrashRepository {

    /**
     * Retrieves the flag indicating whether crash collection is enabled.
     *
     * @return A [Flow] that emits the boolean value indicating whether crash collection is enabled.
     */
    fun getCollectionEnabled(): Flow<OperationResult<Boolean>>

    /**
     * Initializes the crash reporting system.
     */
    fun init()

    /**
     * Logs a message for crash reporting.
     *
     * @param message The message to log.
     */
    fun log(message: String)

    /**
     * Records an exception for crash reporting.
     *
     * @param throwable The throwable to record.
     */
    fun recordException(throwable: Throwable)

    /**
     * Sets the flag indicating whether crash collection is enabled.
     *
     * @param enabled The boolean value indicating whether crash collection is enabled.
     */
    suspend fun setCollectionEnabled(enabled: Boolean)

    /**
     * Sets a custom key-value pair for crash reporting.
     *
     * @param key The key of the custom data.
     * @param value The value of the custom data.
     */
    fun setCustomKey(key: String, value: Any)
}
