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
import dev.zitech.core.remoteconfig.domain.source.ConfigProviderSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FakeConfigProviderSource : ConfigProviderSource {

    var initResult: DataResult<Unit> = DataResult.Error()
    var stringResult: DataResult<String> = DataResult.Error()
    var booleanResult: DataResult<Boolean> = DataResult.Error()
    var doubleResult: DataResult<Double> = DataResult.Error()
    var longResult: DataResult<Long> = DataResult.Error()

    override fun init(): Flow<DataResult<Unit>> = flowOf(initResult)

    override fun getString(config: StringConfig): DataResult<String> = stringResult

    override fun getBoolean(config: BooleanConfig): DataResult<Boolean> = booleanResult

    override fun getDouble(config: DoubleConfig): DataResult<Double> = doubleResult

    override fun getLong(config: LongConfig): DataResult<Long> = longResult
}
