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

package dev.zitech.fireflow.common.domain.repository.featureflag

import dev.zitech.fireflow.common.domain.model.featureflag.Feature

/**
 * Repository for managing feature flags, combining both developer and production feature flags.
 */
interface FeatureFlagRepository : DevFeatureFlag, ProdFeatureFlag {

    /**
     * Initializes the feature flag repository.
     */
    fun init()

    /**
     * Checks if the specified feature is enabled.
     *
     * @param feature The [Feature] to check.
     * @return True if the feature is enabled, false otherwise.
     */
    suspend fun isFeatureEnabled(feature: Feature): Boolean
}
