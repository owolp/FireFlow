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

import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.featureflag.domain.model.Feature
import dev.zitech.core.featureflag.domain.model.ProdFeature
import dev.zitech.core.featureflag.domain.provider.FeatureFlagProvider
import dev.zitech.core.remoteconfig.framework.configurator.RemoteConfigurator
import javax.inject.Inject

internal class RemoteFeatureFlagProvider @Inject constructor(
    private val remoteConfigurator: RemoteConfigurator
) : FeatureFlagProvider {

    override val priority: Int = PRIORITY_MAXIMUM

    override suspend fun isFeatureEnabled(feature: Feature): Boolean =
        when (val result = remoteConfigurator.getBoolean(feature.key)) {
            is DataResult.Success -> result.value
            is DataResult.Error -> feature.defaultValue
        }

    override suspend fun hasFeature(feature: Feature): Boolean =
        when (feature) {
            ProdFeature.APP_ACTIVE -> true
            else -> false
        }
}
