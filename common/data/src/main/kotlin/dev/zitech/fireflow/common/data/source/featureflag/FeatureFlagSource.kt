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

package dev.zitech.fireflow.common.data.source.featureflag

import dev.zitech.fireflow.common.domain.model.featureflag.Feature
import dev.zitech.fireflow.core.result.OperationResult

/**
 * Interface for retrieving feature flag information from a data source
 *
 * Every source has an explicit priority so they can override each other.
 * Not every provider has to provide a flag value for every feature. This is to avoid implicitly
 * relying on build-in defaults (e.g. "Remote Config tool" returns false when no value for a
 * feature) and to avoid that every provider has to provide a value for every feature. (e.g. no
 * "Remote Config tool" configuration needed, unless you want the toggle to be remote)
 */
internal interface FeatureFlagSource {

    /**
     * The priority of the feature flag source.
     * Feature flag sources with higher priority are queried first.
     */
    val priority: Int

    /**
     * Checks if a feature is available.
     *
     * @param feature The feature to check.
     * @return `true` if the feature is available, `false` otherwise.
     */
    suspend fun hasFeature(feature: Feature): Boolean

    /**
     * Checks if a feature is enabled.
     *
     * @param feature The feature to check.
     * @return `true` if the feature is enabled, `false` otherwise.
     */
    suspend fun isFeatureEnabled(feature: Feature): OperationResult<Boolean>
}
