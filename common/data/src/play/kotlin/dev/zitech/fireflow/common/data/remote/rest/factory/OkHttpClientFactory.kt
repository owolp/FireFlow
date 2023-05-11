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

package dev.zitech.fireflow.common.data.remote.rest.factory

import java.util.concurrent.TimeUnit
import javax.inject.Inject
import okhttp3.Authenticator
import okhttp3.OkHttpClient

/**
 * Factory class for creating an instance of OkHttpClient.
 *
 * @param interceptorFactory The factory for creating interceptors.
 * @param refreshTokenAuthenticator The authenticator for refreshing the authentication token.
 */
internal class OkHttpClientFactory @Inject constructor(
    private val interceptorFactory: InterceptorFactory,
    private val refreshTokenAuthenticator: Authenticator
) {

    /**
     * Creates and configures an instance of OkHttpClient.
     *
     * @return The created OkHttpClient instance.
     */
    fun createOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(SERVICE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(SERVICE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(SERVICE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        with(okHttpClient) {
            addInterceptor(interceptorFactory(Authentication))
            authenticator(refreshTokenAuthenticator)
        }

        return okHttpClient.build()
    }

    private companion object {
        const val SERVICE_TIMEOUT_SECONDS = 60L
    }
}
