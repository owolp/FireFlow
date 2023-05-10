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
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit

/**
 * A Retrofit [CallAdapter.Factory] that creates [CallAdapter] instances for network calls returning [NetworkResponse].
 *
 * This factory is responsible for creating [CallAdapter] instances that adapt network calls returning [NetworkResponse]
 * to the appropriate [Call] type.
 */
internal class NetworkResultCallAdapterFactory : CallAdapter.Factory() {

    /**
     * Creates a [CallAdapter] for the specified return type.
     *
     * This method is called by Retrofit to create a [CallAdapter] for a specific return type.
     * It checks if the return type is a [Call] and if the generic type is [NetworkResponse].
     * If the conditions are met, it creates a [NetworkResultCallAdapter] and returns it.
     *
     * @param returnType The return type of the network call.
     * @param annotations The annotations applied to the network call.
     * @param retrofit The Retrofit instance.
     * @return A [CallAdapter] for the specified return type, or null if it cannot be created.
     */
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != NetworkResponse::class.java) return null

        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
        return NetworkResultCallAdapter(resultType)
    }

    companion object {
        /**
         * Creates a new instance of [NetworkResultCallAdapterFactory].
         *
         * @return The created [NetworkResultCallAdapterFactory] instance.
         */
        fun create(): NetworkResultCallAdapterFactory = NetworkResultCallAdapterFactory()
    }
}
