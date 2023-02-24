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

package dev.zitech.core.common.domain.database

import dev.zitech.core.common.domain.code.StatusCode
import dev.zitech.core.common.domain.model.DataError
import dev.zitech.core.common.domain.model.DataException
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.domain.model.DataSuccess

sealed interface DatabaseResult<T : Any>

data class DatabaseSuccess<T : Any>(val data: T) : DatabaseResult<T>
data class DatabaseError<T : Any>(
    val statusCode: StatusCode,
    val message: String?
) : DatabaseResult<T>

data class DatabaseException<T : Any>(val exception: Throwable) : DatabaseResult<T>

inline fun <T : Any, R : Any> DatabaseResult<T>.mapToDataResult(
    transform: (T) -> R
): DataResult<out R> =
    when (this) {
        is DatabaseSuccess -> DataSuccess(transform(data))
        is DatabaseError -> DataError(statusCode, message)
        is DatabaseException -> DataException(exception)
    }
