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

package dev.zitech.fireflow.common.data.remote.rest.retrofit.model

import dev.zitech.fireflow.common.data.remote.rest.retrofit.RetrofitFactory
import dev.zitech.fireflow.core.concurrency.ControlledRunner
import dev.zitech.fireflow.core.logger.Logger
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import retrofit2.Retrofit

/**
 * Implementation of the [RetrofitModel] interface. It provides a [Retrofit] instance based on the given user ID and
 * server address. It ensures that only one [Retrofit] instance is created per user ID.
 *
 * @param controlledRunner The controlled runner used to manage concurrent [Retrofit] instance creation.
 * @param retrofitFactory The factory used to create [Retrofit] instances.
 */
internal class RetrofitModelImpl @Inject constructor(
    private val controlledRunner: ControlledRunner<Retrofit>,
    private val retrofitFactory: RetrofitFactory
) : RetrofitModel {

    private val retrofitMap = ConcurrentHashMap<Long, Retrofit>()
    private val tag = Logger.tag(this::class.java)

    /**
     * Retrieves a [Retrofit] instance based on the provided user ID and server address. It either returns an existing
     * [Retrofit] instance from the cache or creates a new one if not found.
     *
     * @param userId The ID of the user.
     * @param serverAddress The address of the server.
     * @return The [Retrofit] instance.
     */
    override suspend fun invoke(
        userId: Long,
        serverAddress: String
    ) = controlledRunner.joinPreviousOrRun {
        var retrofit = retrofitMap[userId]
        if (retrofit == null) {
            retrofit = retrofitFactory(serverAddress)
            retrofitMap[userId] = retrofit
            Logger.d(
                tag,
                "Created new retrofit instance '${retrofit.hashCode()} for userId:$userId"
            )
        } else {
            Logger.d(tag, "Found retrofit instance '${retrofit.hashCode()}\" for userId:$userId")
        }

        return@joinPreviousOrRun retrofit
    }
}
