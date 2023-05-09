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

package dev.zitech.fireflow.common.data.remote.rest

import dev.zitech.fireflow.common.data.remote.rest.code.StatusCode
import dev.zitech.fireflow.common.data.remote.rest.result.NetworkError
import dev.zitech.fireflow.common.data.remote.rest.result.NetworkException
import dev.zitech.fireflow.common.data.remote.rest.result.NetworkResponse
import dev.zitech.fireflow.common.data.remote.rest.result.NetworkSuccess
import retrofit2.Response

@Suppress("TooGenericExceptionCaught")
internal fun <T : Any> handleApi(
    execute: () -> Response<T>
): NetworkResponse<T> = try {
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
} catch (throwable: Throwable) {
    NetworkException(
        throwable = throwable
    )
}

internal fun <T> getStatusCodeFromResponse(response: Response<T>): StatusCode =
    StatusCode.values().find { it.code == response.code() } ?: StatusCode.Unknown
