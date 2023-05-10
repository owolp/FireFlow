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

import dev.zitech.fireflow.common.data.remote.rest.handleApi
import dev.zitech.fireflow.common.data.remote.rest.result.NetworkResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Implementation of the [Call] interface that wraps another [Call] instance and transforms its response
 * into a [NetworkResponse].
 *
 * @param proxy The underlying [Call] instance.
 * @param T The type of the response body.
 */
internal class NetworkResultCall<T : Any>(
    private val proxy: Call<T>
) : Call<NetworkResponse<T>> {

    override fun cancel() {
        proxy.cancel()
    }

    override fun clone(): Call<NetworkResponse<T>> = NetworkResultCall(proxy.clone())

    /**
     * Enqueues the request and notifies the provided [Callback] with a [Response] of [NetworkResponse] type.
     *
     * This method enqueues the original [Call] instance and defines a new [Callback] that handles the response.
     * The [Callback] provided to this method will be called with a [Response] of [NetworkResponse] type,
     * wrapping the original response.
     *
     * @param callback The [Callback] to be notified with the response.
     */
    override fun enqueue(callback: Callback<NetworkResponse<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val networkResult = handleApi { response }
                callback.onResponse(this@NetworkResultCall, Response.success(networkResult))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResult = NetworkResponse.Exception<T>(t)
                callback.onResponse(this@NetworkResultCall, Response.success(networkResult))
            }
        })
    }

    override fun execute(): Response<NetworkResponse<T>> = throw NotImplementedError()

    override fun isCanceled(): Boolean = proxy.isCanceled

    override fun isExecuted(): Boolean = proxy.isExecuted

    override fun request(): Request = proxy.request()

    override fun timeout(): Timeout = proxy.timeout()
}
