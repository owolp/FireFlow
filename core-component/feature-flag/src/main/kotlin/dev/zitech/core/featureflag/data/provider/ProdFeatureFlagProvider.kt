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

package dev.zitech.core.featureflag.data.provider

import dev.zitech.core.featureflag.domain.model.DevFeature
import dev.zitech.core.featureflag.domain.model.Feature
import dev.zitech.core.featureflag.domain.model.ProdFeature
import dev.zitech.core.featureflag.domain.provider.FeatureFlagProvider
import javax.inject.Inject

internal class ProdFeatureFlagProvider @Inject constructor() : FeatureFlagProvider {

    override val priority: Int = PRIORITY_MINIMUM

    override suspend fun isFeatureEnabled(feature: Feature): Boolean =
        when (feature) {
            is DevFeature -> {
                false // DevFeature should never be shipped to users
            }
            is ProdFeature -> {
                TODO() // No prod features yet
            }
            else -> throw NotImplementedError("Feature not handled")
        }

    override suspend fun hasFeature(feature: Feature): Boolean = true
}
