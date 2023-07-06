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

package dev.zitech.fireflow.onboarding.data.source

import dev.zitech.fireflow.core.result.OperationResult

/**
 * Interface for retrieving resources.
 *
 * This interface defines methods for retrieving different types of resources,
 * such as adjectives and nouns.
 */
internal interface ResourceSource {

    /**
     * Retrieves a list of adjectives.
     *
     * @return An [OperationResult] representing the result of the retrieval operation,
     *         containing a list of adjectives if successful, or an error if unsuccessful.
     */
    suspend fun getAdjectives(): OperationResult<List<String>>

    /**
     * Retrieves a list of nouns.
     *
     * @return An [OperationResult] representing the result of the retrieval operation,
     *         containing a list of nouns if successful, or an error if unsuccessful.
     */
    suspend fun getNouns(): OperationResult<List<String>>
}
