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

package dev.zitech.core.featureflag.domain.model

/**
 * A prod feature is something that disappears over time (hence it is a tool to simplify
 * development)
 * e.g develop a feature, test it, release it, then remove it and the feature remains in the app
 */
enum class ProdFeature(
    override val key: String,
    override val title: String,
    override val explanation: String,
    override val defaultValue: Boolean
) : Feature {

    APP_ACTIVE(
        key = "app_active",
        title = "Is Application Active",
        explanation = "If false, application won't open",
        defaultValue = true
    )
}
