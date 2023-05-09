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

package dev.zitech.fireflow.common.data.remote.configurator

import dev.zitech.fireflow.core.error.Error.BuildTypeUnsupported
import dev.zitech.fireflow.core.work.OperationResult
import dev.zitech.fireflow.core.work.OperationResult.Failure
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class RemoteConfiguratorImpl @Inject constructor() : RemoteConfigurator {

    override fun getBoolean(key: String): OperationResult<Boolean> = Failure(BuildTypeUnsupported)

    override fun getDouble(key: String): OperationResult<Double> = Failure(BuildTypeUnsupported)

    override fun getLong(key: String): OperationResult<Long> = Failure(BuildTypeUnsupported)

    override fun getString(key: String): OperationResult<String> = Failure(BuildTypeUnsupported)

    override fun init(): Flow<OperationResult<Unit>> =
        flowOf(Failure(BuildTypeUnsupported))
}
