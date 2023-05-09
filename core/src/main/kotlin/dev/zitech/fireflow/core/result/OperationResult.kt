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

package dev.zitech.fireflow.core.result

import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success

/**
 * Represents the result of an operation that can either be successful or a failure.
 *
 * @param T The type of data associated with the success result.
 */
sealed interface OperationResult<out T : Any> {

    /**
     * Represents a successful result of the operation.
     *
     * @property data The data associated with the success result.
     */
    data class Success<out T : Any>(val data: T) : OperationResult<T>

    /**
     * Represents a failure result of the operation.
     *
     * @property error The error associated with the failure result.
     */
    data class Failure<T : Any>(val error: Error) : OperationResult<T>
}

/**
 * Executes the specified [executable] block if the operation result is a success.
 *
 * @param executable The block of code to execute if the operation result is a success.
 * @return The original operation result.
 */
suspend fun <T : Any> OperationResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): OperationResult<T> = apply {
    if (this is Success<T>) {
        executable(data)
    }
}

/**
 * Executes the specified [executable] block if the operation result is a failure.
 *
 * @param executable The block of code to execute if the operation result is a failure.
 * @return The original operation result.
 */
suspend fun <T : Any> OperationResult<T>.onFailure(
    executable: suspend (error: Error) -> Unit
): OperationResult<T> = apply {
    if (this is Failure) {
        executable(error)
    }
}

/**
 * Transforms the operation result from type [T] to type [R] using the specified [transform] function.
 *
 * @param transform The transformation function to apply to the data of the success result.
 * @return The transformed operation result.
 */
inline fun <T : Any, R : Any> OperationResult<T>.mapToWork(
    transform: (T) -> R
): OperationResult<R> =
    when (this) {
        is Success -> Success(transform(data))
        is Failure -> Failure(error)
    }
