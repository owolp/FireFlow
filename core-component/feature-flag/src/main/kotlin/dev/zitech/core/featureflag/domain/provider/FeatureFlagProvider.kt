/*
 * Copyright (C) 2022 Zitech
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

package dev.zitech.core.featureflag.domain.provider

import dev.zitech.core.featureflag.domain.model.Feature

/**
 * Every provider has an explicit priority so they can override each other
 *
 * Not every provider has to provide a flag value for every feature. This is to avoid implicitly
 * relying on build-in defaults (e.g. "Remote Config tool" returns false when no value for a
 * feature) and to avoid that every provider has to provide a value for every feature. (e.g. no
 * "Remote Config tool" configuration needed, unless you want the toggle to be remote)
 */
internal interface FeatureFlagProvider {

    val priority: Int
    suspend fun isFeatureEnabled(feature: Feature): Boolean
    suspend fun hasFeature(feature: Feature): Boolean
}
