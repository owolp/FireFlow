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

import dev.zitech.core.common.domain.model.DataException
import dev.zitech.core.common.domain.model.DataResult
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class DataResultCall<T : Any>(
    private val proxy: Call<T>
) : Call<DataResult<T>> {

    override fun clone(): Call<DataResult<T>> = DataResultCall(proxy.clone())

    override fun execute(): Response<DataResult<T>> = throw NotImplementedError()

    override fun isExecuted(): Boolean = proxy.isExecuted

    override fun cancel() {
        proxy.cancel()
    }

    override fun isCanceled(): Boolean = proxy.isCanceled

    override fun request(): Request = proxy.request()

    override fun timeout(): Timeout = proxy.timeout()

    override fun enqueue(callback: Callback<DataResult<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val dataResult = handleApi { response }
                callback.onResponse(this@DataResultCall, Response.success(dataResult))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val dataResult = DataException<T>(t)
                callback.onResponse(this@DataResultCall, Response.success(dataResult))
            }
        })
    }
}
