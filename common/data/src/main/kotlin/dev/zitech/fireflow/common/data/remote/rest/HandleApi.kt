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
import dev.zitech.fireflow.common.data.remote.rest.result.NetworkResponse
import retrofit2.Response

/**
 * Handles API responses and returns a [NetworkResponse] based on the response status and body.
 *
 * This function executes the provided [execute] block, which should perform an API call and return a [Response].
 * It checks if the response is successful and contains a non-null body. If so, it returns a [NetworkResponse.Success]
 * wrapping the body. Otherwise, it returns a [NetworkResponse.Error] containing the status code and error message
 * retrieved from the response.
 *
 * If an exception occurs during the execution, it catches the exception and returns a [NetworkResponse.Exception]
 * wrapping the thrown [Throwable].
 *
 * @param execute The block of code that performs the API call and returns a [Response].
 * @return A [NetworkResponse] representing the result of the API call.
 */
@Suppress("TooGenericExceptionCaught")
internal fun <T : Any> handleApi(
    execute: () -> Response<T>
): NetworkResponse<T> = try {
    val response = execute()
    val body = response.body()
    if (response.isSuccessful && body != null) {
        NetworkResponse.Success(body)
    } else {
        NetworkResponse.Error(
            statusCode = getStatusCodeFromResponse(response),
            message = response.message()
        )
    }
} catch (throwable: Throwable) {
    NetworkResponse.Exception(
        throwable = throwable
    )
}

/**
 * Retrieves the [StatusCode] corresponding to the HTTP response code from the provided [response].
 *
 * This function maps the HTTP response code to the appropriate [StatusCode] enum value.
 * If no matching enum value is found, it returns [StatusCode.Unknown].
 *
 * @param response The HTTP response containing the response code.
 * @return The [StatusCode] enum value corresponding to the response code.
 */

internal fun <T> getStatusCodeFromResponse(response: Response<T>): StatusCode =
    StatusCode.values().find { it.code == response.code() } ?: StatusCode.Unknown
