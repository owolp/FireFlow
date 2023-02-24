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

package dev.zitech.core.network.domain.model

sealed interface NetworkResult<T : Any>

suspend fun <T : Any> NetworkResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkSuccess<T>) {
        executable(data)
    }
}

suspend fun <T : Any> NetworkResult<T>.onError(
    executable: suspend (statusCode: StatusCode, message: String?) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkError<T>) {
        executable(statusCode, message)
    }
}

suspend fun <T : Any> NetworkResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkException<T>) {
        executable(exception)
    }
}

data class NetworkSuccess<T : Any>(val data: T) : NetworkResult<T>
data class NetworkError<T : Any>(
    val statusCode: StatusCode,
    val message: String?
) : NetworkResult<T>

data class NetworkException<T : Any>(val exception: Throwable) : NetworkResult<T>
