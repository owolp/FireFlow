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

package dev.zitech.core.remoteconfig.framework.source

import dev.zitech.core.common.domain.model.LegacyDataResult
import dev.zitech.core.remoteconfig.domain.model.BooleanConfig
import dev.zitech.core.remoteconfig.domain.model.DoubleConfig
import dev.zitech.core.remoteconfig.domain.model.LongConfig
import dev.zitech.core.remoteconfig.domain.model.StringConfig
import dev.zitech.core.remoteconfig.domain.source.ConfigProviderSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FakeConfigProviderSource : ConfigProviderSource {

    var initResult: LegacyDataResult<Unit> = LegacyDataResult.Error()
    var stringResult: LegacyDataResult<String> = LegacyDataResult.Error()
    var booleanResult: LegacyDataResult<Boolean> = LegacyDataResult.Error()
    var doubleResult: LegacyDataResult<Double> = LegacyDataResult.Error()
    var longResult: LegacyDataResult<Long> = LegacyDataResult.Error()

    override fun init(): Flow<LegacyDataResult<Unit>> = flowOf(initResult)

    override fun getString(config: StringConfig): LegacyDataResult<String> = stringResult

    override fun getBoolean(config: BooleanConfig): LegacyDataResult<Boolean> = booleanResult

    override fun getDouble(config: DoubleConfig): LegacyDataResult<Double> = doubleResult

    override fun getLong(config: LongConfig): LegacyDataResult<Long> = longResult
}
