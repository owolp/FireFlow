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
import dev.zitech.fireflow.core.work.Work
import dev.zitech.fireflow.core.work.WorkError
import dev.zitech.fireflow.core.work.WorkSuccess

sealed interface NetworkResult<out T : Any>

data class NetworkSuccess<out T : Any>(val data: T) : NetworkResult<T>

data class NetworkError<T : Any>(
    val message: String? = null,
    val statusCode: StatusCode
) : NetworkResult<T>

data class NetworkException<out T : Any>(val throwable: Throwable) : NetworkResult<T>

suspend fun <T : Any> NetworkResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkSuccess<T>) {
        executable(data)
    }
}

suspend fun <T : Any> NetworkResult<T>.onError(
    executable: suspend (statusCode: StatusCode, message: String) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkError) {
        executable(statusCode, message.orEmpty())
    }
}

suspend fun <T : Any> NetworkResult<T>.onException(
    executable: suspend (throwable: Throwable) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkException) {
        executable(throwable)
    }
}

fun <T : Any, R : Any> NetworkResult<T>.mapToWork(
    transformSuccess: (T) -> R
): Work<R> =
    when (this) {
        is NetworkSuccess -> WorkSuccess(transformSuccess(data))
        is NetworkError -> WorkError(getError(statusCode, message))
        is NetworkException -> WorkError(getError(throwable))
    }

fun getError(statusCode: StatusCode, message: String?): Error =
    when (statusCode) {
        StatusCode.Unauthorized -> Error.TokenFailed(message)
        else -> Error.UserVisible(message)
    }

fun getError(throwable: Throwable): Error =
    Error.Fatal(throwable, NETWORK)
