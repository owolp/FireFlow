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

package dev.zitech.fireflow.common.domain.model.configurator

enum class BooleanConfig(
    override val defaultValue: Boolean,
    override val explanation: String,
    override val key: String,
    override val title: String
) : Config<Boolean> {

    APP_ACTIVE(
        key = "app_active",
        title = "Is Application Active",
        explanation = "If false, application won't open",
        defaultValue = true
    ),
    PERFORMANCE_COLLECTION_ENABLED(
        key = "performance_collection_enabled",
        title = "Is Performance Collection Enabled",
        explanation = "If true, application performance 3rd party collection tools will be enabled",
        defaultValue = false
    )
}
