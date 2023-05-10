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

package dev.zitech.fireflow.common.data.source.configurator

import dev.zitech.fireflow.common.domain.model.configurator.BooleanConfig
import dev.zitech.fireflow.common.domain.model.configurator.DoubleConfig
import dev.zitech.fireflow.common.domain.model.configurator.LongConfig
import dev.zitech.fireflow.common.domain.model.configurator.StringConfig
import dev.zitech.fireflow.core.result.OperationResult
import kotlinx.coroutines.flow.Flow

/**
 * Interface for providing configuration values from a data source.
 */
internal interface ConfiguratorProviderSource {

    /**
     * Retrieves a boolean configuration value.
     *
     * @param config The boolean configuration to retrieve.
     * @return An [OperationResult] representing the result of the operation,
     *         containing the boolean configuration value on success or an error on failure.
     */
    fun getBoolean(config: BooleanConfig): OperationResult<Boolean>

    /**
     * Retrieves a double configuration value.
     *
     * @param config The double configuration to retrieve.
     * @return An [OperationResult] representing the result of the operation,
     *         containing the double configuration value on success or an error on failure.
     */
    fun getDouble(config: DoubleConfig): OperationResult<Double>

    /**
     * Retrieves a long configuration value.
     *
     * @param config The long configuration to retrieve.
     * @return An [OperationResult] representing the result of the operation,
     *         containing the long configuration value on success or an error on failure.
     */
    fun getLong(config: LongConfig): OperationResult<Long>

    /**
     * Retrieves a string configuration value.
     *
     * @param config The string configuration to retrieve.
     * @return An [OperationResult] representing the result of the operation,
     *         containing the string configuration value on success or an error on failure.
     */
    fun getString(config: StringConfig): OperationResult<String>

    /**
     * Initializes the configuration provider source.
     *
     * @return A [Flow] emitting [OperationResult] representing the initialization result,
     *         containing [Unit] on success or an error on failure.
     */
    fun init(): Flow<OperationResult<Unit>>
}
