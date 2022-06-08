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

package dev.zitech.core.reporter.analytics.domain.usecase

import dev.zitech.core.common.DataFactory
import dev.zitech.core.persistence.domain.model.database.UserLoggedState
import dev.zitech.core.persistence.domain.usecase.database.GetUserLoggedStateUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetAnalyticsCollectionValueUseCase
import dev.zitech.core.reporter.analytics.domain.repository.AnalyticsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class SetAnalyticsCollectionUseCaseTest {

    private val analyticsRepository = mockk<AnalyticsRepository>(relaxUnitFun = true)
    private val getUserLoggedStateUseCase = mockk<GetUserLoggedStateUseCase>()
    private val getAnalyticsCollectionValueUseCase = mockk<GetAnalyticsCollectionValueUseCase>()

    private lateinit var sut: SetAnalyticsCollectionUseCase

    @BeforeEach
    fun setup() {
        sut = SetAnalyticsCollectionUseCase(
            analyticsRepository,
            getUserLoggedStateUseCase,
            getAnalyticsCollectionValueUseCase
        )
    }

    @Test
    fun `GIVEN enabled not null value THEN invoke setCollectionEnabled with correct value`() = runTest {
        // Arrange
        val expectedResult = DataFactory.createRandomBoolean()

        // Act
        sut(expectedResult)

        // Assert
        verify { analyticsRepository.setCollectionEnabled(expectedResult) }
        coVerify(exactly = 0) {
            getUserLoggedStateUseCase()
            getAnalyticsCollectionValueUseCase()
        }
    }

    @Test
    fun `GIVEN enabled null value and user is logged in THEN invoke setCollectionEnabled with correct value`() = runTest {
        // Arrange
        val expectedResult = DataFactory.createRandomBoolean()
        coEvery { getUserLoggedStateUseCase() } returns UserLoggedState.LOGGED_IN
        coEvery { getAnalyticsCollectionValueUseCase() } returns expectedResult

        // Act
        sut(null)

        // Assert
        coVerify {
            getUserLoggedStateUseCase()
            getAnalyticsCollectionValueUseCase()
        }
        verify { analyticsRepository.setCollectionEnabled(expectedResult) }
    }

    @Test
    fun `GIVEN enabled null value and user is not logged in THEN invoke setCollectionEnabled with correct value`() = runTest {
        // Arrange
        val expectedResult = false
        coEvery { getUserLoggedStateUseCase() } returns UserLoggedState.LOGGED_OUT
        coEvery { getAnalyticsCollectionValueUseCase() } returns expectedResult

        // Act
        sut(null)

        // Assert
        coVerify { getUserLoggedStateUseCase() }
        coVerify(exactly = 0) { getAnalyticsCollectionValueUseCase() }
        verify { analyticsRepository.setCollectionEnabled(expectedResult) }
    }
}