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

package dev.zitech.core.reporter.analytics.domain.usecase

import dev.zitech.core.common.DataFactory
import dev.zitech.core.persistence.domain.model.database.UserLoggedState
import dev.zitech.core.persistence.domain.usecase.database.GetUserLoggedStateUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetAllowPersonalizedAdsValueUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.SaveAllowPersonalizedAdsValueUseCase
import dev.zitech.core.reporter.analytics.domain.repository.AnalyticsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class AllowPersonalizedAdsUseCaseTest {

    private val analyticsRepository = mockk<AnalyticsRepository>(relaxUnitFun = true)
    private val getUserLoggedStateUseCase = mockk<GetUserLoggedStateUseCase>()
    private val getAllowPersonalizedAdsValueUseCase = mockk<GetAllowPersonalizedAdsValueUseCase>()
    private val saveAllowPersonalizedAdsValueUseCase = mockk<SaveAllowPersonalizedAdsValueUseCase>(
        relaxUnitFun = true,
    )

    private lateinit var sut: AllowPersonalizedAdsUseCase

    @BeforeEach
    fun setup() {
        sut = AllowPersonalizedAdsUseCase(
            analyticsRepository,
            getUserLoggedStateUseCase,
            getAllowPersonalizedAdsValueUseCase,
            saveAllowPersonalizedAdsValueUseCase,
        )
    }

    @Test
    fun `GIVEN enabled not null value THEN invoke allowPersonalizedAds and save with correct value`() = runTest {
        // Arrange
        val expectedResult = DataFactory.createRandomBoolean()

        // Act
        sut(expectedResult)

        // Assert
        coVerify {
            analyticsRepository.allowPersonalizedAds(expectedResult)
            saveAllowPersonalizedAdsValueUseCase(expectedResult)
        }
        coVerify(exactly = 0) {
            getUserLoggedStateUseCase()
            getAllowPersonalizedAdsValueUseCase()
        }
    }

    @Test
    fun `GIVEN enabled null value and user is logged in THEN invoke allowPersonalizedAds and save with correct value`() = runTest {
        // Arrange
        val expectedResult = DataFactory.createRandomBoolean()
        coEvery { getUserLoggedStateUseCase() } returns UserLoggedState.LOGGED_IN
        coEvery { getAllowPersonalizedAdsValueUseCase() } returns expectedResult

        // Act
        sut(null)

        // Assert
        coVerify {
            getUserLoggedStateUseCase()
            getAllowPersonalizedAdsValueUseCase()
        }
        coVerify {
            analyticsRepository.allowPersonalizedAds(expectedResult)
            saveAllowPersonalizedAdsValueUseCase(expectedResult)
        }
    }

    @Test
    fun `GIVEN enabled null value and user is not logged in THEN invoke allowPersonalizedAds and save with correct value`() = runTest {
        // Arrange
        val expectedResult = false
        coEvery { getUserLoggedStateUseCase() } returns UserLoggedState.LOGGED_OUT
        coEvery { getAllowPersonalizedAdsValueUseCase() } returns expectedResult

        // Act
        sut(null)

        // Assert
        coVerify { getUserLoggedStateUseCase() }
        coVerify(exactly = 0) { getAllowPersonalizedAdsValueUseCase() }
        coVerify {
            analyticsRepository.allowPersonalizedAds(expectedResult)
            saveAllowPersonalizedAdsValueUseCase(expectedResult)
        }
    }
}
