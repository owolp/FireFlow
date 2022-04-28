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

package dev.zitech.core.remoteconfig.framework.source

import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.remoteconfig.domain.model.BooleanConfig
import dev.zitech.core.remoteconfig.domain.model.DoubleConfig
import dev.zitech.core.remoteconfig.domain.model.LongConfig
import dev.zitech.core.remoteconfig.domain.model.StringConfig
import dev.zitech.core.remoteconfig.framework.configurator.RemoteConfigurator
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ConfigProviderSourceImpl @Inject constructor(
    private val remoteConfigurator: RemoteConfigurator
) : ConfigProviderSource {

    override fun init(): Flow<DataResult<Unit>> =
        remoteConfigurator.init()

    override fun getBoolean(config: BooleanConfig): DataResult<Boolean> =
        remoteConfigurator.getBoolean(config.key)

    override fun getDouble(config: DoubleConfig): DataResult<Double> =
        remoteConfigurator.getDouble(config.key)

    override fun getLong(config: LongConfig): DataResult<Long> =
        remoteConfigurator.getLong(config.key)

    override fun getString(config: StringConfig): DataResult<String> =
        remoteConfigurator.getString(config.key)
}
