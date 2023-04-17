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
import dev.zitech.fireflow.core.work.Work
import kotlinx.coroutines.flow.Flow

interface ConfiguratorRepository {

    fun getBooleanConfigs(): List<BooleanConfig>
    suspend fun getBooleanValue(config: BooleanConfig): Work<Boolean>
    fun getDoubleConfigs(): List<DoubleConfig>
    suspend fun getDoubleValue(config: DoubleConfig): Work<Double>
    fun getLongConfigs(): List<LongConfig>
    suspend fun getLongValue(config: LongConfig): Work<Long>
    fun getStringConfigs(): List<StringConfig>
    suspend fun getStringValue(config: StringConfig): Work<String>
    fun init(): Flow<Work<Unit>>
}