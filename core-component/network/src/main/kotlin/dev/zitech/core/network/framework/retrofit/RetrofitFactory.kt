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

import dev.zitech.core.network.data.factory.OkHttpClientFactory
import javax.inject.Inject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal class RetrofitFactory @Inject constructor(
    private val okHttpClientFactory: OkHttpClientFactory
) {

    operator fun invoke(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClientFactory.createOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(DataResultCallAdapterFactory.create())
            .build()
}
