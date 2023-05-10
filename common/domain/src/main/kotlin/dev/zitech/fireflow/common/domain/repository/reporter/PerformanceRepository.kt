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

package dev.zitech.fireflow.common.domain.repository.reporter

import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing performance monitoring.
 */
interface PerformanceRepository {

    /**
     * Retrieves the flag indicating whether performance collection is enabled.
     *
     * @return A [Flow] that emits the boolean value indicating whether performance collection is enabled.
     */
    fun getCollectionEnabled(): Flow<Boolean>

    /**
     * Sets the flag indicating whether performance collection is enabled.
     *
     * @param enabled The boolean value indicating whether performance collection is enabled.
     */
    suspend fun setCollectionEnabled(enabled: Boolean)
}
