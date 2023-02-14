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

package dev.zitech.core.network.data.factory

import dev.zitech.core.network.data.factory.InterceptorFactory.Companion.Type.HTTP_INSPECTOR
import dev.zitech.core.network.data.factory.InterceptorFactory.Companion.Type.HTTP_LOGGING
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import okhttp3.OkHttpClient

internal class OkHttpClientFactory @Inject constructor(
    private val interceptorFactory: InterceptorFactory
) {

    private companion object {
        const val SERVICE_TIMEOUT_SECONDS = 30L
    }

    fun createOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(SERVICE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(SERVICE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(SERVICE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        with(okHttpClient) {
            addInterceptor(interceptorFactory(HTTP_INSPECTOR))
            addInterceptor(interceptorFactory(HTTP_LOGGING))
        }

        return okHttpClient.build()
    }
}
