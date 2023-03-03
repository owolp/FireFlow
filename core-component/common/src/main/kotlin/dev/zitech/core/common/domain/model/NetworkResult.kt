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

package dev.zitech.core.common.domain.model

import dev.zitech.core.common.domain.code.StatusCode
import dev.zitech.core.common.domain.exception.FireFlowException

sealed interface NetworkResult<out T : Any>

data class NetworkSuccess<out T : Any>(val data: T) : NetworkResult<T>
data class NetworkError<T : Any>(
    val statusCode: StatusCode,
    val message: String? = null
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

fun <T : Any, R : Any> NetworkResult<T>.mapToDataResult(
    transformSuccess: (T) -> R
): DataResult<R> =
    when (this) {
        is NetworkSuccess -> DataSuccess(transformSuccess(data))
        is NetworkError -> DataError(getFireFlowException(statusCode, message))
        is NetworkException -> DataError(getFireFlowException(throwable))
    }

fun getFireFlowException(statusCode: StatusCode, message: String?): FireFlowException =
    when (statusCode) {
        StatusCode.Unauthorized -> FireFlowException.TokenRefreshFailed(message)
        else -> FireFlowException.DataError(statusCode, message)
    }

fun getFireFlowException(throwable: Throwable): FireFlowException =
    FireFlowException.DataException(throwable)
