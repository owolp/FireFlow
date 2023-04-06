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

import dev.zitech.core.common.domain.error.Error

@Deprecated("Modules")
sealed interface Work<out T : Any>

@Deprecated("Modules")
data class WorkSuccess<out T : Any>(val data: T) : Work<T>

@Deprecated("Modules")
data class WorkError<T : Any>(val error: Error) : Work<T>

suspend fun <T : Any> Work<T>.onSuccess(
    executable: suspend (T) -> Unit
): Work<T> = apply {
    if (this is WorkSuccess<T>) {
        executable(data)
    }
}

suspend fun <T : Any> Work<T>.onError(
    executable: suspend (error: Error) -> Unit
): Work<T> = apply {
    if (this is WorkError) {
        executable(error)
    }
}

inline fun <T : Any, R : Any> Work<T>.mapToWork(
    transform: (T) -> R
): Work<R> =
    when (this) {
        is WorkSuccess -> WorkSuccess(transform(data))
        is WorkError -> WorkError(error)
    }
