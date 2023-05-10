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

package dev.zitech.fireflow.common.domain.usecase.featureflag

import dev.zitech.fireflow.common.domain.model.featureflag.ProdFeature
import dev.zitech.fireflow.common.domain.repository.featureflag.FeatureFlagRepository
import javax.inject.Inject

/**
 * Use case for retrieving the list of production features and their enabled status.
 *
 * @property featureFlagRepository The repository for accessing feature flag data.
 */
class GetProdFeaturesUseCase @Inject constructor(
    private val featureFlagRepository: FeatureFlagRepository
) {
    /**
     * Invokes the use case to retrieve the list of production features and their enabled status.
     *
     * @return A list of pairs, where each pair contains a production feature and its enabled status.
     * The list is sorted by the feature titles in ascending order.
     */
    suspend operator fun invoke(): List<Pair<ProdFeature, Boolean>> {
        val features = mutableListOf<Pair<ProdFeature, Boolean>>()

        featureFlagRepository.getProdFeatures().forEach { feature ->
            features.add(
                Pair(
                    feature,
                    featureFlagRepository.isFeatureEnabled(feature)
                )
            )
        }

        return features.sortedBy { it.first.title }
    }
}
