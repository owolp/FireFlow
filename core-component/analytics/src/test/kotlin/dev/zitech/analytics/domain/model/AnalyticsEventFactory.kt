/*
 * Copyright (C) 2022 Zitech Ltd.
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

package dev.zitech.analytics.domain.model

object AnalyticsEventFactory {

    private const val NAME = "Event Name"
    private const val DESCRIPTION = "Event Description"
    private const val PARAM_KEY_1 = "Event Param Key 1"
    private const val PARAM_VALUE_1 = "Event Param Value 1"
    private const val PARAM_KEY_2 = "Event Param Key 2"
    private const val PARAM_VALUE_2 = "Event Param Value 2"
    private val PROVIDER = AnalyticsProvider.HUAWEI

    fun createAnalyticsEvent(
        name: String = NAME,
        description: String = DESCRIPTION,
        params: Map<String, Any?> = mapOf(
            PARAM_KEY_1 to PARAM_VALUE_1,
            PARAM_KEY_2 to PARAM_VALUE_2
        ),
        providers: List<AnalyticsProvider> = listOf(PROVIDER)
    ) = object : AnalyticsEvent {
        override val name: String = name
        override val description: String = description
        override val params: Map<String, Any?> = params
        override val providers: List<AnalyticsProvider> = providers
    }
}
