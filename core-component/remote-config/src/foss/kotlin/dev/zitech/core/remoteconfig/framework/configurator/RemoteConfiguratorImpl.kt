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

package dev.zitech.core.remoteconfig.framework.configurator

import dev.zitech.core.common.domain.exception.FireFlowException.BuildTypeUnsupported
import dev.zitech.core.common.domain.model.DataError
import dev.zitech.core.common.domain.model.DataResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class RemoteConfiguratorImpl @Inject constructor() : RemoteConfigurator {

    override fun init(): Flow<DataResult<Unit>> =
        flowOf(DataError(BuildTypeUnsupported))

    override fun getString(key: String): DataResult<String> = DataError(BuildTypeUnsupported)

    override fun getBoolean(key: String): DataResult<Boolean> = DataError(BuildTypeUnsupported)

    override fun getDouble(key: String): DataResult<Double> = DataError(BuildTypeUnsupported)

    override fun getLong(key: String): DataResult<Long> = DataError(BuildTypeUnsupported)
}
