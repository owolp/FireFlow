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

package dev.zitech.fireflow.common.data.remote.rest.result

import dev.zitech.fireflow.common.data.remote.rest.code.StatusCode
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.error.Error.Fatal.Type.NETWORK
import dev.zitech.fireflow.core.work.OperationResult

/**
 * Represents the network response of an operation, which can be a success, an error, or an exception.
 *
 * @param T The type of data associated with the network response.
 */
sealed interface NetworkResponse<out T : Any> {

    /**
     * Represents a successful network response.
     *
     * @property data The data associated with the success response.
     */
    data class Success<out T : Any>(val data: T) : NetworkResponse<T>

    /**
     * Represents an error network response.
     *
     * @property message The error message associated with the response.
     * @property statusCode The status code associated with the response.
     */
    data class Error<T : Any>(
        val message: String? = null,
        val statusCode: StatusCode
    ) : NetworkResponse<T>

    /**
     * Represents an exceptional network response.
     *
     * @property throwable The throwable associated with the exception.
     */
    data class Exception<out T : Any>(val throwable: Throwable) : NetworkResponse<T>
}

/**
 * Executes the specified [executable] block if the network response is a success.
 *
 * @param executable The block of code to execute if the network response is a success.
 * @return The original network response.
 */
suspend fun <T : Any> NetworkResponse<T>.onSuccess(
    executable: suspend (T) -> Unit
): NetworkResponse<T> = apply {
    if (this is NetworkResponse.Success<T>) {
        executable(data)
    }
}

/**
 * Executes the specified [executable] block if the network response is an error.
 *
 * @param executable The block of code to execute if the network response is an error.
 * @return The original network response.
 */
suspend fun <T : Any> NetworkResponse<T>.onError(
    executable: suspend (statusCode: StatusCode, message: String) -> Unit
): NetworkResponse<T> = apply {
    if (this is NetworkResponse.Error) {
        executable(statusCode, message.orEmpty())
    }
}

/**
 * Executes the specified [executable] block if the network response is an exception.
 *
 * @param executable The block of code to execute if the network response is an exception.
 * @return The original network response.
 */
suspend fun <T : Any> NetworkResponse<T>.onException(
    executable: suspend (throwable: Throwable) -> Unit
): NetworkResponse<T> = apply {
    if (this is NetworkResponse.Exception) {
        executable(throwable)
    }
}

/**
 * Maps the network response from type [T] to type [R] using the specified [transformSuccess] function.
 *
 * @param transformSuccess The transformation function to apply to the data of the success response.
 * @return The mapped operation result.
 */
fun <T : Any, R : Any> NetworkResponse<T>.mapToWork(
    transformSuccess: (T) -> R
): OperationResult<R> =
    when (this) {
        is NetworkResponse.Success -> OperationResult.Success(transformSuccess(data))
        is NetworkResponse.Error -> OperationResult.Failure(getError(statusCode, message))
        is NetworkResponse.Exception -> OperationResult.Failure(getError(throwable))
    }

/**
 * Gets the corresponding error object based on the provided [statusCode] and [message].
 *
 * @param statusCode The status code associated with the error.
 * @param message The error message.
 * @return The error object.
 */
fun getError(statusCode: StatusCode, message: String?): Error =
    when (statusCode) {
        StatusCode.Unauthorized -> Error.TokenFailed(message)
        else -> Error.UserVisible(message)
    }

/**
 * Gets the corresponding error object based on the provided [throwable].
 *
 * @param throwable The throwable associated with the error.
 * @return The error object.
 */
fun getError(throwable: Throwable): Error =
    Error.Fatal(throwable, NETWORK)
