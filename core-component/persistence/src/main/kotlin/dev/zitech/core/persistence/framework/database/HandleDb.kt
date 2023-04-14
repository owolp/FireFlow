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

package dev.zitech.core.persistence.framework.database

import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.domain.error.Error.Fatal.Type.DISK
import dev.zitech.core.common.domain.model.Work
import dev.zitech.core.common.domain.model.WorkError
import dev.zitech.core.common.domain.model.WorkSuccess

@Deprecated("Modules")
@Suppress("TooGenericExceptionCaught")
internal suspend fun <T : Any> handleDb(
    execute: suspend () -> T
): Work<T> = try {
    WorkSuccess(execute())
} catch (throwable: Throwable) {
    WorkError(Error.Fatal(throwable, DISK))
}
