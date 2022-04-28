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

package dev.zitech.core.remoteconfig.data.repository

import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.remoteconfig.domain.model.BooleanConfig
import dev.zitech.core.remoteconfig.domain.model.DoubleConfig
import dev.zitech.core.remoteconfig.domain.model.LongConfig
import dev.zitech.core.remoteconfig.domain.model.StringConfig
import dev.zitech.core.remoteconfig.domain.repository.ConfigRepository
import dev.zitech.core.remoteconfig.framework.source.ConfigProviderSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class ConfigRepositoryImpl @Inject constructor(
    private val configProviderSource: ConfigProviderSource
) : ConfigRepository {

    override fun init(): Flow<DataResult<Unit>> =
        configProviderSource.init()

    override fun getBooleanConfigs(): List<BooleanConfig> =
        BooleanConfig.values().toList()

    override fun getDoubleConfigs(): List<DoubleConfig> =
        DoubleConfig.values().toList()

    override fun getLongConfigs(): List<LongConfig> =
        LongConfig.values().toList()

    override fun getStringConfigs(): List<StringConfig> =
        StringConfig.values().toList()

    override suspend fun getBooleanValue(config: BooleanConfig): DataResult<Boolean> =
        configProviderSource.getBoolean(config)

    override suspend fun getDoubleValue(config: DoubleConfig): DataResult<Double> =
        configProviderSource.getDouble(config)

    override suspend fun getLongValue(config: LongConfig): DataResult<Long> =
        configProviderSource.getLong(config)

    override suspend fun getStringValue(config: StringConfig): DataResult<String> =
        configProviderSource.getString(config)
}
