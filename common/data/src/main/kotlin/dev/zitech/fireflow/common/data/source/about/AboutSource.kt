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

package dev.zitech.fireflow.common.data.source.about

import dev.zitech.fireflow.common.domain.model.profile.FireflyProfile
import dev.zitech.fireflow.core.result.OperationResult

/**
 * Interface for retrieving information about the Firefly profile.
 */
internal interface AboutSource {

    /**
     * Retrieves the Firefly profile information.
     *
     * @return An [OperationResult] representing the result of the operation,
     *         containing the Firefly profile on success or an error on failure.
     */
    suspend fun getFireflyProfile(): OperationResult<FireflyProfile>
}
