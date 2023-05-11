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

import dev.zitech.fireflow.common.data.remote.rest.result.NetworkResponse
import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter

/**
 * A Retrofit [CallAdapter] that adapts a [Call] to a [Call] of [NetworkResponse] type.
 *
 * This class is responsible for adapting a Retrofit [Call] to a [Call] of [NetworkResponse] type.
 *
 * @param resultType The type of the result expected from the network call.
 */
class NetworkResultCallAdapter(
    private val resultType: Type
) : CallAdapter<Type, Call<NetworkResponse<Type>>> {

    /**
     * Adapts the original [Call] to a [Call] of [NetworkResponse] type.
     *
     * This method wraps the original [Call] instance with a [NetworkResultCall] to provide a [Call]
     * that returns a [NetworkResponse] of the expected result type.
     *
     * @param call The original [Call] instance.
     * @return A [Call] of [NetworkResponse] type.
     */
    override fun adapt(call: Call<Type>): Call<NetworkResponse<Type>> = NetworkResultCall(call)

    /**
     * Returns the type of the result expected from the network call.
     *
     * This method returns the result type specified during the creation of the adapter.
     *
     * @return The type of the result.
     */
    override fun responseType(): Type = resultType
}
