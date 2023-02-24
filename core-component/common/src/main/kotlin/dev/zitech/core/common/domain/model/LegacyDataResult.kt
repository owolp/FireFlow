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

@Deprecated("Deprecated", ReplaceWith("DataResult"))
sealed class LegacyDataResult<out T : Any> {
    data class Success<out T : Any>(val value: T) : LegacyDataResult<T>()
    data class Error(
        val message: String? = null,
        val cause: Exception? = null
    ) : LegacyDataResult<Nothing>()
}

sealed interface DataResult<T : Any>

data class DataSuccess<T : Any>(val data: T) : DataResult<T>
data class DataError(
    val statusCode: StatusCode,
    val message: String? = null
) : DataResult<Nothing>

data class DataException(val exception: Throwable) : DataResult<Nothing>

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
