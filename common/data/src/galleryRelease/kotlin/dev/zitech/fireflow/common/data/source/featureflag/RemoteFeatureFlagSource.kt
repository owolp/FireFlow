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

import dev.zitech.fireflow.common.data.remote.configurator.RemoteConfigurator
import dev.zitech.fireflow.common.domain.model.featureflag.Feature
import dev.zitech.fireflow.core.work.WorkError
import dev.zitech.fireflow.core.work.WorkSuccess
import javax.inject.Inject

internal class RemoteFeatureFlagSource @Inject constructor(
    private val remoteConfigurator: RemoteConfigurator
) : FeatureFlagSource {

    override val priority: Int = PRIORITY_MAXIMUM

    override suspend fun hasFeature(feature: Feature): Boolean =
        when (feature) {
            else -> false
        }

    override suspend fun isFeatureEnabled(feature: Feature): Boolean =
        when (val result = remoteConfigurator.getBoolean(feature.key)) {
            is WorkSuccess -> result.data
            is WorkError -> feature.defaultValue
        }
}
