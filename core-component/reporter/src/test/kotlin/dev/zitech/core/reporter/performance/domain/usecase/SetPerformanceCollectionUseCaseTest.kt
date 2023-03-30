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

package dev.zitech.core.reporter.performance.domain.usecase

import dev.zitech.core.common.DataFactory
import dev.zitech.core.persistence.domain.model.database.UserLoggedState
import dev.zitech.core.persistence.domain.usecase.database.GetUserLoggedStateUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetPerformanceCollectionValueUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.SavePerformanceCollectionValueUseCase
import dev.zitech.core.remoteconfig.domain.model.BooleanConfig
import dev.zitech.core.remoteconfig.domain.usecase.GetBooleanConfigValueUseCase
import dev.zitech.core.reporter.performance.domain.repository.PerformanceRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class SetPerformanceCollectionUseCaseTest {

    private val performanceRepository = mockk<PerformanceRepository>(relaxUnitFun = true)
    private val getUserLoggedStateUseCase = mockk<GetUserLoggedStateUseCase>()
    private val getPerformanceCollectionValueUseCase = mockk<GetPerformanceCollectionValueUseCase>()
    private val getBooleanConfigValueUseCase = mockk<GetBooleanConfigValueUseCase>()
    private val savePerformanceCollectionValueUseCase = mockk<SavePerformanceCollectionValueUseCase>(
        relaxUnitFun = true,
    )

    private lateinit var sut: SetPerformanceCollectionUseCase

    @BeforeEach
    fun setup() {
        sut = SetPerformanceCollectionUseCase(
            performanceRepository,
            getUserLoggedStateUseCase,
            getPerformanceCollectionValueUseCase,
            getBooleanConfigValueUseCase,
            savePerformanceCollectionValueUseCase,
        )
    }

    @Test
    fun `GIVEN enabled not null value THEN invoke setCollectionEnabled and save with correct value`() = runTest {
        // Arrange
        val expectedResult = DataFactory.createRandomBoolean()

        // Act
        sut(expectedResult)

        // Assert
        coVerify {
            performanceRepository.setCollectionEnabled(expectedResult)
            savePerformanceCollectionValueUseCase(expectedResult)
        }
        coVerify(exactly = 0) {
            getUserLoggedStateUseCase()
            getPerformanceCollectionValueUseCase()
        }
    }

    @Test
    fun `GIVEN enabled null value and user is logged in THEN invoke setCollectionEnabled and save with correct value`() = runTest {
        // Arrange
        val performanceCollectionEnabled = DataFactory.createRandomBoolean()
        coEvery { getPerformanceCollectionValueUseCase() } returns performanceCollectionEnabled

        val booleanConfigValue = DataFactory.createRandomBoolean()
        coEvery {
            getBooleanConfigValueUseCase(BooleanConfig.PERFORMANCE_COLLECTION_ENABLED)
        } returns booleanConfigValue

        val expectedResult = performanceCollectionEnabled && booleanConfigValue

        coEvery { getUserLoggedStateUseCase() } returns UserLoggedState.LOGGED_IN

        // Act
        sut(null)

        // Assert
        coVerify {
            getUserLoggedStateUseCase()
            getPerformanceCollectionValueUseCase()
        }
        coVerify {
            performanceRepository.setCollectionEnabled(expectedResult)
            savePerformanceCollectionValueUseCase(expectedResult)
        }
    }

    @Test
    fun `GIVEN enabled null value and user is not logged in THEN invoke setCollectionEnabled and save with correct value`() = runTest {
        // Arrange
        val expectedResult = false
        coEvery { getUserLoggedStateUseCase() } returns UserLoggedState.LOGGED_OUT
        coEvery { getPerformanceCollectionValueUseCase() } returns expectedResult

        // Act
        sut(null)

        // Assert
        coVerify { getUserLoggedStateUseCase() }
        coVerify(exactly = 0) { getPerformanceCollectionValueUseCase() }
        coVerify {
            performanceRepository.setCollectionEnabled(expectedResult)
            savePerformanceCollectionValueUseCase(expectedResult)
        }
    }
}
