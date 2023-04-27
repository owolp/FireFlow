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

package dev.zitech.fireflow.common.domain.model.preferences

enum class BooleanPreference(
    override val key: String,
    override val title: String,
    override val explanation: String,
    override val defaultValue: Boolean
) : Preference<Boolean> {

    ANALYTICS_COLLECTION(
        key = "analytics_collection",
        title = "Analytics data collection",
        explanation = "In some cases, you may wish to temporarily or permanently disable " +
            "collection of Analytics data, such as to collect end-user consent or to fulfill " +
            "legal obligations.",
        defaultValue = false
    ),
    CRASH_REPORTER_COLLECTION(
        key = "crash_reporter_collection",
        title = "Crash data collection",
        explanation = "Automatically post crash reports to a report server",
        defaultValue = false
    ),
    PERFORMANCE_COLLECTION(
        key = "performance_collection",
        title = "Application performance data collection",
        explanation = "In some cases, you may wish to temporarily or permanently disable " +
            "collection of Application performance data, such as to collect end-user consent or " +
            "to fulfill legal obligations.",
        defaultValue = false
    ),
    PERSONALIZED_ADS(
        key = "personalized_ads",
        title = "Analytics personalized advertising features",
        explanation = "To programmatically control whether a user's Analytics data should be " +
            "used for personalized advertising, set the appropriate default behavior",
        defaultValue = false
    )
}
