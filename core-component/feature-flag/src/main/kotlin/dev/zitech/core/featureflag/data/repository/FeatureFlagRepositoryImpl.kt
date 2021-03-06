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

package dev.zitech.core.featureflag.data.repository

import androidx.annotation.VisibleForTesting
import dev.zitech.core.common.domain.applicationconfig.AppConfigProvider
import dev.zitech.core.common.domain.model.BuildMode
import dev.zitech.core.featureflag.data.provider.DevFeatureFlagProvider
import dev.zitech.core.featureflag.domain.model.DevFeature
import dev.zitech.core.featureflag.domain.model.Feature
import dev.zitech.core.featureflag.domain.model.ProdFeature
import dev.zitech.core.featureflag.domain.provider.FeatureFlagProvider
import dev.zitech.core.featureflag.domain.repository.FeatureFlagRepository
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

internal class FeatureFlagRepositoryImpl @Inject constructor(
    private val appConfigProvider: AppConfigProvider,
    private val devFeatureFlagProvider: FeatureFlagProvider,
    private val prodFeatureFlagProvider: FeatureFlagProvider,
    private val remoteFeatureFlagProvider: FeatureFlagProvider
) : FeatureFlagRepository {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val providers = CopyOnWriteArrayList<FeatureFlagProvider>()

    override fun init() {
        when (appConfigProvider.buildMode) {
            BuildMode.RELEASE -> {
                providers.add(prodFeatureFlagProvider)
                providers.add(remoteFeatureFlagProvider)
            }
            BuildMode.DEBUG -> providers.add(devFeatureFlagProvider)
        }
    }

    override suspend fun isFeatureEnabled(feature: Feature): Boolean =
        providers.filter { it.hasFeature(feature) }
            .sortedByDescending(FeatureFlagProvider::priority)
            .firstOrNull()
            ?.isFeatureEnabled(feature)
            ?: feature.defaultValue

    override suspend fun getDevFeatures(): List<DevFeature> =
        DevFeature.values().toList()

    override suspend fun setDevFeatureEnabled(feature: Feature, enabled: Boolean) {
        (devFeatureFlagProvider as? DevFeatureFlagProvider)?.setFeatureEnabled(
            feature = feature,
            enabled = enabled
        )
    }

    override suspend fun getProdFeatures(): List<ProdFeature> =
        ProdFeature.values().toList()
}
