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

import dev.zitech.fireflow.common.domain.model.analytics.AnalyticsEvent
import dev.zitech.fireflow.core.result.OperationResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing analytics events and settings.
 */
interface AnalyticsRepository {

    /**
     * Retrieves the flag indicating whether personalized ads are allowed.
     *
     * @return A [Flow] that emits the boolean value indicating whether personalized ads are allowed.
     */
    fun getAllowPersonalizedAds(): Flow<OperationResult<Boolean>>

    /**
     * Retrieves the flag indicating whether data collection is enabled.
     *
     * @return A [Flow] that emits the boolean value indicating whether data collection is enabled.
     */
    fun getCollectionEnabled(): Flow<OperationResult<Boolean>>

    /**
     * Logs an analytics event.
     *
     * @param analyticsEvent The analytics event to log.
     */
    fun logEvent(analyticsEvent: AnalyticsEvent)

    /**
     * Sets the flag indicating whether personalized ads are allowed.
     *
     * @param enabled The boolean value indicating whether personalized ads are allowed.
     */
    suspend fun setAllowPersonalizedAds(enabled: Boolean)

    /**
     * Sets the flag indicating whether data collection is enabled.
     *
     * @param enabled The boolean value indicating whether data collection is enabled.
     */
    suspend fun setCollectionEnabled(enabled: Boolean)
}
