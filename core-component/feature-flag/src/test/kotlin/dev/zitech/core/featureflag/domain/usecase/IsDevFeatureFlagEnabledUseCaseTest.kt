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

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.featureflag.domain.model.Feature
import dev.zitech.core.persistence.domain.model.PreferenceType
import dev.zitech.core.persistence.domain.repository.GetPreferencesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class IsDevFeatureFlagEnabledUseCaseTest {

    private val getPreferencesRepository = mockk<GetPreferencesRepository>()

    private lateinit var sut: IsDevFeatureFlagEnabledUseCase

    @BeforeEach
    fun setUp() {
        sut = IsDevFeatureFlagEnabledUseCase(
            getPreferencesRepository = getPreferencesRepository
        )
    }

    @Test
    fun invoke() = runBlocking {
        val preferenceType = PreferenceType.DEVELOPMENT
        val key = DataFactory.createRandomString()
        val defaultValue = DataFactory.createRandomBoolean()

        val feature = mockkClass(Feature::class)
        every { feature.key } returns key
        every { feature.defaultValue } returns defaultValue

        val result = DataFactory.createRandomBoolean()

        every {
            getPreferencesRepository.getBoolean(
                preferenceType = preferenceType,
                key = key,
                defaultValue = defaultValue
            )
        } returns flowOf(result)

        sut(feature).test {
            assertThat(awaitItem()).isEqualTo(result)
            awaitComplete()
        }
        verify {
            getPreferencesRepository.getBoolean(
                preferenceType = preferenceType,
                key = key,
                defaultValue = defaultValue
            )
        }
    }
}