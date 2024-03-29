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

import dev.zitech.fireflow.common.domain.model.featureflag.DevFeature
import dev.zitech.fireflow.common.domain.model.featureflag.Feature

/**
 * Repository for accessing and modifying developer features.
 */
interface DevFeatureFlag {

    /**
     * Retrieves the list of all available developer features.
     *
     * @return A [List] of [DevFeature] objects.
     */
    suspend fun getDevFeatures(): List<DevFeature>

    /**
     * Enables or disables the specified developer feature.
     *
     * @param feature The [Feature] to enable or disable.
     * @param enabled True if the feature should be enabled, false otherwise.
     */
    suspend fun setDevFeatureEnabled(feature: Feature, enabled: Boolean)
}
