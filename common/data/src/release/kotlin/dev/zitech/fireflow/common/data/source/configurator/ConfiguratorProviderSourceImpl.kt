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

import dev.zitech.fireflow.common.data.remote.configurator.RemoteConfigurator
import dev.zitech.fireflow.common.domain.model.configurator.BooleanConfig
import dev.zitech.fireflow.common.domain.model.configurator.DoubleConfig
import dev.zitech.fireflow.common.domain.model.configurator.LongConfig
import dev.zitech.fireflow.common.domain.model.configurator.StringConfig
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class ConfiguratorProviderSourceImpl @Inject constructor(
    private val remoteConfigurator: RemoteConfigurator
) : ConfiguratorProviderSource {

    override fun getBoolean(config: BooleanConfig): OperationResult<Boolean> =
        remoteConfigurator.getBoolean(config.key)

    override fun getDouble(config: DoubleConfig): OperationResult<Double> =
        remoteConfigurator.getDouble(config.key)

    override fun getLong(config: LongConfig): OperationResult<Long> =
        remoteConfigurator.getLong(config.key)

    override fun getString(config: StringConfig): OperationResult<String> =
        remoteConfigurator.getString(config.key)

    override fun init(): Flow<OperationResult<Unit>> =
        remoteConfigurator.init()
}
