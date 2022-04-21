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

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.featureflag.domain.model.Feature
import dev.zitech.core.featureflag.domain.repository.FeatureFlagRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class IsFeatureEnabledUseCaseTest {

    private val featureFlagRepository = mockk<FeatureFlagRepository>()

    private lateinit var sut: IsFeatureEnabledUseCase

    @BeforeEach
    fun setup() {
        sut = IsFeatureEnabledUseCase(
            featureFlagRepository = featureFlagRepository
        )
    }

    @Test
    fun invoke() = runBlocking {
        val feature = mockkClass(Feature::class)
        val featureEnabled = DataFactory.createRandomBoolean()

        coEvery { featureFlagRepository.isFeatureEnabled(feature) } returns featureEnabled

        val result = sut(feature)

        assertThat(result).isEqualTo(featureEnabled)
        coVerify {
            featureFlagRepository.isFeatureEnabled(feature)
        }
    }
}