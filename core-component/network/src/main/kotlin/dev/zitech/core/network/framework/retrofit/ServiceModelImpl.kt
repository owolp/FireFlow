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

import dev.zitech.authenticator.data.remote.service.OAuthService
import dev.zitech.core.common.domain.cache.InMemoryCache
import dev.zitech.core.network.data.service.AboutService
import dev.zitech.core.network.domain.retrofit.RetrofitModel
import dev.zitech.core.network.domain.retrofit.ServiceModel
import dev.zitech.core.persistence.domain.model.cache.NetworkDetails
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

internal class ServiceModelImpl @Inject constructor(
    private val retrofitModel: RetrofitModel,
    private val networkDetailsInMemoryCache: InMemoryCache<NetworkDetails>
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
