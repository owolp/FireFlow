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

import dev.zitech.core.common.domain.error.Error.BuildTypeUnsupported
import dev.zitech.core.common.domain.model.Work
import dev.zitech.core.common.domain.model.WorkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FakeRemoteConfigurator : RemoteConfigurator {

    var initResult: Work<Unit> = WorkError(BuildTypeUnsupported)
    var stringResult: Work<String> = WorkError(BuildTypeUnsupported)
    var booleanResult: Work<Boolean> = WorkError(BuildTypeUnsupported)
    var doubleResult: Work<Double> = WorkError(BuildTypeUnsupported)
    var longResult: Work<Long> = WorkError(BuildTypeUnsupported)

    override fun init(): Flow<Work<Unit>> = flowOf(initResult)

    override fun getString(key: String): Work<String> = stringResult

    override fun getBoolean(key: String): Work<Boolean> = booleanResult

    override fun getDouble(key: String): Work<Double> = doubleResult

    override fun getLong(key: String): Work<Long> = longResult
}
