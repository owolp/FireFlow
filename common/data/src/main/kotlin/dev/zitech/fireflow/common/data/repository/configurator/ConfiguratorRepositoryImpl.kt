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

package dev.zitech.fireflow.common.data.repository.configurator

import dev.zitech.fireflow.common.data.source.configurator.ConfiguratorProviderSource
import dev.zitech.fireflow.common.domain.model.configurator.BooleanConfig
import dev.zitech.fireflow.common.domain.model.configurator.DoubleConfig
import dev.zitech.fireflow.common.domain.model.configurator.LongConfig
import dev.zitech.fireflow.common.domain.model.configurator.StringConfig
import dev.zitech.fireflow.common.domain.repository.configurator.ConfiguratorRepository
import dev.zitech.fireflow.core.work.OperationResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class ConfiguratorRepositoryImpl @Inject constructor(
    private val configuratorProviderSource: ConfiguratorProviderSource
) : ConfiguratorRepository {

    override fun getBooleanConfigs(): List<BooleanConfig> =
        BooleanConfig.values().toList()

    override suspend fun getBooleanValue(config: BooleanConfig): OperationResult<Boolean> =
        configuratorProviderSource.getBoolean(config)

    override fun getDoubleConfigs(): List<DoubleConfig> =
        DoubleConfig.values().toList()

    override suspend fun getDoubleValue(config: DoubleConfig): OperationResult<Double> =
        configuratorProviderSource.getDouble(config)

    override fun getLongConfigs(): List<LongConfig> =
        LongConfig.values().toList()

    override suspend fun getLongValue(config: LongConfig): OperationResult<Long> =
        configuratorProviderSource.getLong(config)

    override fun getStringConfigs(): List<StringConfig> =
        StringConfig.values().toList()

    override suspend fun getStringValue(config: StringConfig): OperationResult<String> =
        configuratorProviderSource.getString(config)

    override fun init(): Flow<OperationResult<Unit>> =
        configuratorProviderSource.init()
}
