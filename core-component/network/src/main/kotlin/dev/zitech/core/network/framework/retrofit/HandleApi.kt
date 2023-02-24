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

import dev.zitech.core.common.domain.network.NetworkError
import dev.zitech.core.common.domain.network.NetworkException
import dev.zitech.core.common.domain.network.NetworkResult
import dev.zitech.core.common.domain.network.NetworkSuccess
import retrofit2.Response

@Suppress("TooGenericExceptionCaught")
internal fun <T : Any> handleApi(
    execute: () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkSuccess(body)
        } else {
            NetworkError(
                statusCode = getStatusCodeFromResponse(response),
                message = response.message()
            )
        }
    } catch (exception: Throwable) {
        NetworkException(
            exception = exception
        )
    }
}