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

package dev.zitech.core.network.framework.retrofit

import dev.zitech.core.common.domain.concurrency.ControlledRunner
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.network.domain.retrofit.RetrofitModel
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import retrofit2.Retrofit

internal class RetrofitModelImpl @Inject constructor(
    private val retrofitFactory: RetrofitFactory,
    private val controlledRunner: ControlledRunner<Retrofit>
) : RetrofitModel {

    private val tag = Logger.tag(this::class.java)

    private val retrofitMap = ConcurrentHashMap<String, Retrofit>()

    override suspend fun invoke(
        serverAddress: String
    ) = controlledRunner.joinPreviousOrRun {
        var retrofit = retrofitMap[serverAddress]
        if (retrofit == null) {
            retrofit = retrofitFactory(serverAddress)
            retrofitMap[serverAddress] = retrofit
            Logger.d(tag, "Created new $serverAddress retrofit instance '${retrofit.hashCode()}")
        } else {
            Logger.d(tag, "Found retrofit instance '${retrofit.hashCode()}\" for $serverAddress")
        }

        return@joinPreviousOrRun retrofit
    }
}
