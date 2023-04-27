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

import dev.zitech.fireflow.common.data.memory.cache.InMemoryCache
import dev.zitech.fireflow.common.data.remote.rest.service.AboutService
import dev.zitech.fireflow.common.data.remote.rest.service.OAuthService
import dev.zitech.fireflow.common.data.repository.user.NetworkDetails
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

internal class ServiceModelImpl @Inject constructor(
    private val networkDetailsInMemoryCache: InMemoryCache<NetworkDetails>,
    private val retrofitModel: RetrofitModel
) : ServiceModel {

    override val aboutService = getService<AboutService>()

    override val oAuthService = getService<OAuthService>()

    private inline fun <reified T> getService(): T = runBlocking {
        retrofitModel.invoke(
            userId = networkDetailsInMemoryCache.data!!.userId,
            serverAddress = networkDetailsInMemoryCache.data!!.serverAddress
        ).create(T::class.java)
    }
}
