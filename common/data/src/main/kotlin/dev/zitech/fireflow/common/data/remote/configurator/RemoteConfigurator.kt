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

import dev.zitech.fireflow.core.result.OperationResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface for remote configuration retrieval and initialization.
 */
internal interface RemoteConfigurator {

    /**
     * Retrieves a boolean configuration value associated with the given key.
     *
     * @param key The key of the configuration value.
     * @return An [OperationResult] containing the boolean configuration value if successful, or an error otherwise.
     */
    fun getBoolean(key: String): OperationResult<Boolean>

    /**
     * Retrieves a double configuration value associated with the given key.
     *
     * @param key The key of the configuration value.
     * @return An [OperationResult] containing the double configuration value if successful, or an error otherwise.
     */
    fun getDouble(key: String): OperationResult<Double>

    /**
     * Retrieves a long configuration value associated with the given key.
     *
     * @param key The key of the configuration value.
     * @return An [OperationResult] containing the long configuration value if successful, or an error otherwise.
     */
    fun getLong(key: String): OperationResult<Long>

    /**
     * Retrieves a string configuration value associated with the given key.
     *
     * @param key The key of the configuration value.
     * @return An [OperationResult] containing the string configuration value if successful, or an error otherwise.
     */
    fun getString(key: String): OperationResult<String>

    /**
     * Initializes the remote configurator and provides a [Flow] of [OperationResult] representing the initialization
     * status.
     *
     * @return A [Flow] of [OperationResult] indicating the initialization status.
     */
    fun init(): Flow<OperationResult<Unit>>
}
