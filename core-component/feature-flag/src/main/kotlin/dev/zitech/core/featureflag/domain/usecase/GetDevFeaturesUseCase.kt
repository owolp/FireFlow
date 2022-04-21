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

package dev.zitech.core.featureflag.domain.usecase

import dev.zitech.core.featureflag.domain.model.DevFeature
import dev.zitech.core.featureflag.domain.repository.FeatureFlagRepository
import javax.inject.Inject

class GetDevFeaturesUseCase @Inject constructor(
    private val featureFlagRepository: FeatureFlagRepository
) {

    suspend operator fun invoke(): List<Pair<DevFeature, Boolean>> {
        val features = mutableListOf<Pair<DevFeature, Boolean>>()

        featureFlagRepository.getDevFeatures().forEach { feature ->
            features.add(
                Pair(
                    feature,
                    featureFlagRepository.isFeatureEnabled(feature),
                )
            )
        }

        return features.sortedBy { it.first.title }
    }
}
