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

package dev.zitech.fireflow.core.work

import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.work.OperationResult.Failure
import dev.zitech.fireflow.core.work.OperationResult.Success

sealed interface OperationResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : OperationResult<T>

    data class Failure<T : Any>(val error: Error) : OperationResult<T>
}

suspend fun <T : Any> OperationResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): OperationResult<T> = apply {
    if (this is Success<T>) {
        executable(data)
    }
}

suspend fun <T : Any> OperationResult<T>.onFailure(
    executable: suspend (error: Error) -> Unit
): OperationResult<T> = apply {
    if (this is Failure) {
        executable(error)
    }
}

inline fun <T : Any, R : Any> OperationResult<T>.mapToWork(
    transform: (T) -> R
): OperationResult<R> =
    when (this) {
        is Success -> Success(transform(data))
        is Failure -> Failure(error)
    }
