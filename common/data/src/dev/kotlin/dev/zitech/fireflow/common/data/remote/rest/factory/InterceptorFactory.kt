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

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import dev.zitech.fireflow.common.data.remote.rest.debugger.NetworkDebugger
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Factory class for creating interceptors based on the provided [InterceptorType].
 *
 * @param context The application context.
 * @param authenticationInterceptor The interceptor for authentication purposes.
 */
internal class InterceptorFactory @Inject constructor(
    private val context: Context,
    private val authenticationInterceptor: Interceptor
) {

    companion object {
        private const val MAX_CONTENT_LENGTH = 250000L
    }

    /**
     * Creates and returns the appropriate interceptor based on the provided [type].
     *
     * @param type The type of interceptor to create.
     * @return The created interceptor.
     */
    operator fun invoke(type: InterceptorType): Interceptor =
        when (type) {
            Authentication -> authenticationInterceptor
            Debugger -> createDebuggerInterceptor()
            HttpInspector -> createHttpInspectorInterceptor()
            HttpLogging -> createHttpLoggingInterceptor()
        }

    private fun createDebuggerInterceptor(): Interceptor =
        FlipperOkhttpInterceptor(NetworkDebugger.networkFlipperPlugin, true)

    private fun createHttpInspectorInterceptor(): Interceptor {
        val chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        return ChuckerInterceptor.Builder(context)
            .collector(chuckerCollector)
            .maxContentLength(MAX_CONTENT_LENGTH)
            .build()
    }

    private fun createHttpLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
}
