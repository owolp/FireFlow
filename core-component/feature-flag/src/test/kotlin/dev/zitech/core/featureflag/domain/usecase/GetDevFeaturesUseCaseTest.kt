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

package dev.zitech.core.featureflag.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.featureflag.domain.model.DevFeature
import dev.zitech.core.featureflag.domain.repository.FeatureFlagRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetDevFeaturesUseCaseTest {

    private val featureFlagRepository = mockk<FeatureFlagRepository>()

    private lateinit var sut: GetDevFeaturesUseCase

    @BeforeEach
    fun setup() {
        sut = GetDevFeaturesUseCase(
            featureFlagRepository = featureFlagRepository
        )
    }

    @Test
    fun invoke() = runBlocking {
        val devFeature1 = mockkClass(DevFeature::class, relaxed = true)
        val devFeature2 = mockkClass(DevFeature::class, relaxed = true)
        val devFeature3 = mockkClass(DevFeature::class, relaxed = true)

        coEvery { featureFlagRepository.getDevFeatures() } returns listOf(
            devFeature1,
            devFeature2,
            devFeature3
        )

        coEvery { featureFlagRepository.isFeatureEnabled(devFeature1) } returns true
        coEvery { featureFlagRepository.isFeatureEnabled(devFeature2) } returns true
        coEvery { featureFlagRepository.isFeatureEnabled(devFeature3) } returns false

        val result = sut()

        assertThat(result[0].first).isEqualTo(devFeature1)
        assertThat(result[0].second).isEqualTo(true)
        assertThat(result[1].first).isEqualTo(devFeature2)
        assertThat(result[1].second).isEqualTo(true)
        assertThat(result[2].first).isEqualTo(devFeature3)
        assertThat(result[2].second).isEqualTo(false)
    }
}
