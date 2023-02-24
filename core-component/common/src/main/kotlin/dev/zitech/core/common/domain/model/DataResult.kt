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

sealed interface DataResult<out T : Any>

data class DataSuccess<out T : Any>(val data: T) : DataResult<T>
data class DataError<T : Any>(
    val statusCode: StatusCode,
    val message: String? = null
) : DataResult<T>

data class DataException<out T : Any>(val exception: Throwable) : DataResult<T>

suspend fun <T : Any> DataResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): DataResult<T> = apply {
    if (this is DataSuccess<T>) {
        executable(data)
    }
}

suspend fun <T : Any> DataResult<T>.onError(
    executable: suspend (statusCode: StatusCode, message: String) -> Unit
): DataResult<T> = apply {
    if (this is DataError) {
        executable(statusCode, message.orEmpty())
    }
}

suspend fun <T : Any> DataResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): DataResult<T> = apply {
    if (this is DataException) {
        executable(exception)
    }
}

inline fun <T : Any, R : Any> DataResult<T>.mapToDataResult(
    transform: (T) -> R
): DataResult<R> =
    when (this) {
        is DataSuccess -> DataSuccess(transform(data))
        is DataError -> DataError(statusCode, message)
        is DataException -> DataException(exception)
    }
