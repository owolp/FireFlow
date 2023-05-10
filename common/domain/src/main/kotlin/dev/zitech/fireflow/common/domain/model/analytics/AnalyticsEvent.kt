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

package dev.zitech.fireflow.common.domain.model.analytics

/**
 * Interface for an analytics event that can be tracked.
 */
interface AnalyticsEvent {

    /**
     * The description of the analytics event.
     */
    val description: String

    /**
     * The name of the analytics event.
     */
    val name: String

    /**
     * The parameters associated with the analytics event.
     * It is represented as a map of parameter names to their corresponding values.
     */
    val params: Map<String, Any?>

    /**
     * The list of analytics providers that should receive this event.
     */
    val providers: List<AnalyticsProvider>
}
