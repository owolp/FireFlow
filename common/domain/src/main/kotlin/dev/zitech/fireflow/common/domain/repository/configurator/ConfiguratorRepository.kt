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

package dev.zitech.fireflow.common.domain.repository.configurator

import dev.zitech.fireflow.common.domain.model.configurator.BooleanConfig
import dev.zitech.fireflow.common.domain.model.configurator.DoubleConfig
import dev.zitech.fireflow.common.domain.model.configurator.LongConfig
import dev.zitech.fireflow.common.domain.model.configurator.StringConfig
import dev.zitech.fireflow.core.result.OperationResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing configurators.
 */
interface ConfiguratorRepository {

    /**
     * Retrieves a list of boolean configurations.
     *
     * @return List of boolean configurations.
     */
    fun getBooleanConfigs(): List<BooleanConfig>

    /**
     * Retrieves the boolean value for the specified configuration.
     *
     * @param config The boolean configuration.
     * @return A flow that emits the operation result containing the boolean value.
     */
    suspend fun getBooleanValue(config: BooleanConfig): OperationResult<Boolean>

    /**
     * Retrieves a list of double configurations.
     *
     * @return List of double configurations.
     */
    fun getDoubleConfigs(): List<DoubleConfig>

    /**
     * Retrieves the double value for the specified configuration.
     *
     * @param config The double configuration.
     * @return A flow that emits the operation result containing the double value.
     */
    suspend fun getDoubleValue(config: DoubleConfig): OperationResult<Double>

    /**
     * Retrieves a list of long configurations.
     *
     * @return List of long configurations.
     */
    fun getLongConfigs(): List<LongConfig>

    /**
     * Retrieves the long value for the specified configuration.
     *
     * @param config The long configuration.
     * @return A flow that emits the operation result containing the long value.
     */
    suspend fun getLongValue(config: LongConfig): OperationResult<Long>

    /**
     * Retrieves a list of string configurations.
     *
     * @return List of string configurations.
     */
    fun getStringConfigs(): List<StringConfig>

    /**
     * Retrieves the string value for the specified configuration.
     *
     * @param config The string configuration.
     * @return A flow that emits the operation result containing the string value.
     */
    suspend fun getStringValue(config: StringConfig): OperationResult<String>

    /**
     * Initializes the configurator repository.
     *
     * @return A flow that emits the operation result indicating the initialization status.
     */
    fun init(): Flow<OperationResult<Unit>>
}
