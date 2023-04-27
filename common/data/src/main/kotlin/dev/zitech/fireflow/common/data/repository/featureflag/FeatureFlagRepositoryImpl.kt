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

package dev.zitech.fireflow.common.data.repository.featureflag

import androidx.annotation.VisibleForTesting
import dev.zitech.fireflow.common.data.source.featureflag.DevFeatureFlagSource
import dev.zitech.fireflow.common.data.source.featureflag.FeatureFlagSource
import dev.zitech.fireflow.common.domain.model.featureflag.DevFeature
import dev.zitech.fireflow.common.domain.model.featureflag.Feature
import dev.zitech.fireflow.common.domain.model.featureflag.ProdFeature
import dev.zitech.fireflow.common.domain.repository.featureflag.FeatureFlagRepository
import dev.zitech.fireflow.core.applicationconfig.AppConfigProvider
import dev.zitech.fireflow.core.applicationconfig.BuildMode
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

internal class FeatureFlagRepositoryImpl @Inject constructor(
    private val appConfigProvider: AppConfigProvider,
    private val devFeatureFlagSource: FeatureFlagSource,
    private val prodFeatureFlagSource: FeatureFlagSource,
    private val remoteFeatureFlagSource: FeatureFlagSource
) : FeatureFlagRepository {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val sources = CopyOnWriteArrayList<FeatureFlagSource>()

    override suspend fun getDevFeatures(): List<DevFeature> =
        DevFeature.values().toList()

    override suspend fun getProdFeatures(): List<ProdFeature> =
        ProdFeature.values().toList()

    override fun init() {
        when (appConfigProvider.buildMode) {
            BuildMode.RELEASE -> {
                sources.add(prodFeatureFlagSource)
                sources.add(remoteFeatureFlagSource)
            }
            BuildMode.DEBUG -> sources.add(devFeatureFlagSource)
        }
    }

    override suspend fun isFeatureEnabled(feature: Feature): Boolean =
        sources.filter { it.hasFeature(feature) }
            .sortedByDescending(FeatureFlagSource::priority)
            .firstOrNull()
            ?.isFeatureEnabled(feature)
            ?: feature.defaultValue

    override suspend fun setDevFeatureEnabled(feature: Feature, enabled: Boolean) {
        (devFeatureFlagSource as? DevFeatureFlagSource)?.setFeatureEnabled(
            feature = feature,
            enabled = enabled
        )
    }
}
