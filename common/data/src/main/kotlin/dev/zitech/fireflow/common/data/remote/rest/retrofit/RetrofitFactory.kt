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

package dev.zitech.fireflow.common.data.remote.rest.retrofit

import dev.zitech.fireflow.common.data.remote.rest.factory.OkHttpClientFactory
import javax.inject.Inject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Factory class for creating instances of Retrofit.
 *
 * This factory is responsible for creating instances of Retrofit with the desired configurations,
 * including the base URL, OkHttpClient, converter factory, and call adapter factory.
 *
 * @property okHttpClientFactory The factory for creating OkHttpClient instances.
 */
internal class RetrofitFactory @Inject constructor(
    private val okHttpClientFactory: OkHttpClientFactory
) {

    /**
     * Creates a Retrofit instance with the specified base URL.
     *
     * This method creates a new Retrofit instance using the provided base URL,
     * OkHttpClient from the [okHttpClientFactory], Moshi converter factory,
     * and the custom [NetworkResultCallAdapterFactory] for handling network responses.
     *
     * @param baseUrl The base URL for the Retrofit instance.
     * @return The created Retrofit instance.
     */
    operator fun invoke(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClientFactory.createOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .build()
}
