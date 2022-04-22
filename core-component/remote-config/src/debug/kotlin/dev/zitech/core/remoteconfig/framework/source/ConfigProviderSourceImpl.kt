/*
 * Copyright (C) 2022 Zitech
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
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class ConfigProviderSourceImpl @Inject constructor() : ConfigProviderSource {

    override fun init(): Flow<DataResult<Unit>> = flowOf(DataResult.Success(Unit))

    override suspend fun getString(config: StringConfig): String = config.defaultValue

    override suspend fun getBoolean(config: BooleanConfig): Boolean = config.defaultValue

    override suspend fun getDouble(config: DoubleConfig): Double = config.defaultValue

    override suspend fun getLong(config: LongConfig): Long = config.defaultValue
}
