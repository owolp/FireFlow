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

package dev.zitech.core.featureflag.data.provider

import dev.zitech.core.featureflag.domain.model.Feature
import dev.zitech.core.featureflag.domain.provider.FeatureFlagProvider
import dev.zitech.core.featureflag.domain.usecase.IsDevFeatureFlagEnabledUseCase
import dev.zitech.core.featureflag.domain.usecase.SetDevFeatureFlagEnabledUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.first

internal class DevFeatureFlagProvider @Inject constructor(
    private val isDevFeatureFlagEnabledUseCase: IsDevFeatureFlagEnabledUseCase,
    private val setDevFeatureFlagEnabledUseCase: SetDevFeatureFlagEnabledUseCase
) : FeatureFlagProvider {

    override val priority: Int = PRIORITY_MEDIUM

    override suspend fun isFeatureEnabled(feature: Feature): Boolean =
        isDevFeatureFlagEnabledUseCase(feature).first()

    override suspend fun hasFeature(feature: Feature): Boolean = true

    suspend fun setFeatureEnabled(feature: Feature, enabled: Boolean): Unit =
        setDevFeatureFlagEnabledUseCase(
            key = feature.key,
            value = enabled
        )
}
