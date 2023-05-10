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

package dev.zitech.fireflow.common.data.reporter.analytics

/**
 * Interface for reporting analytics events and managing analytics settings.
 */
interface AnalyticsReporter {

    /**
     * Sets whether personalized ads are allowed or not.
     *
     * @param enabled `true` to allow personalized ads, `false` otherwise.
     */
    fun allowPersonalizedAds(enabled: Boolean)

    /**
     * Logs an analytics event with the specified event name and parameters.
     *
     * @param eventName The name of the event.
     * @param eventParams The parameters associated with the event.
     */
    fun logEvent(eventName: String, eventParams: Map<String, Any?>)

    /**
     * Sets whether data collection is enabled or disabled.
     *
     * @param enabled `true` to enable data collection, `false` to disable it.
     */
    fun setCollectionEnabled(enabled: Boolean)
}
