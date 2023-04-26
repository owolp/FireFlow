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

package dev.zitech.core.reporter.crash.domain.usecase

import dev.zitech.core.common.DataFactory
import dev.zitech.core.persistence.domain.model.database.UserLoggedState
import dev.zitech.core.persistence.domain.usecase.database.GetUserLoggedStateUseCase
import dev.zitech.core.reporter.crash.domain.repository.CrashRepository
import dev.zitech.settings.domain.usecase.reporter.GetCrashReporterCollectionValueUseCase
import dev.zitech.settings.domain.usecase.reporter.SaveCrashReporterCollectionValueUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class SetCrashReporterCollectionUseCaseTest {

    private val crashRepository = mockk<CrashRepository>(relaxUnitFun = true)
    private val getUserLoggedStateUseCase = mockk<GetUserLoggedStateUseCase>()
    private val getCrashReporterCollectionValueUseCase = mockk<dev.zitech.settings.domain.usecase.reporter.GetCrashReporterCollectionValueUseCase>()
    private val saveCrashReporterCollectionValueUseCase = mockk<dev.zitech.settings.domain.usecase.reporter.SaveCrashReporterCollectionValueUseCase>(
        relaxUnitFun = true
    )

    private lateinit var sut: dev.zitech.settings.domain.usecase.reporter.SetCrashReporterCollectionUseCase

    @BeforeEach
    fun setup() {
        sut = dev.zitech.settings.domain.usecase.reporter.SetCrashReporterCollectionUseCase(
            crashRepository,
            getUserLoggedStateUseCase,
            getCrashReporterCollectionValueUseCase,
            saveCrashReporterCollectionValueUseCase
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
            crashRepository.setCollectionEnabled(expectedResult)
            saveCrashReporterCollectionValueUseCase(expectedResult)
        }
        coVerify(exactly = 0) {
            getUserLoggedStateUseCase()
            getCrashReporterCollectionValueUseCase()
        }
    }

    @Test
    fun `GIVEN enabled null value and user is logged in THEN invoke setCollectionEnabled and save with correct value`() = runTest {
        // Arrange
        val expectedResult = DataFactory.createRandomBoolean()
        coEvery { getUserLoggedStateUseCase() } returns UserLoggedState.LOGGED_IN
        coEvery { getCrashReporterCollectionValueUseCase() } returns expectedResult

        // Act
        sut(null)

        // Assert
        coVerify {
            getUserLoggedStateUseCase()
            getCrashReporterCollectionValueUseCase()
        }
        coVerify {
            crashRepository.setCollectionEnabled(expectedResult)
            saveCrashReporterCollectionValueUseCase(expectedResult)
        }
    }

    @Test
    fun `GIVEN enabled null value and user is not logged in THEN invoke setCollectionEnabled and save with correct value`() = runTest {
        // Arrange
        val expectedResult = false
        coEvery { getUserLoggedStateUseCase() } returns UserLoggedState.LOGGED_OUT
        coEvery { getCrashReporterCollectionValueUseCase() } returns expectedResult

        // Act
        sut(null)

        // Assert
        coVerify { getUserLoggedStateUseCase() }
        coVerify(exactly = 0) { getCrashReporterCollectionValueUseCase() }
        coVerify {
            crashRepository.setCollectionEnabled(expectedResult)
            saveCrashReporterCollectionValueUseCase(expectedResult)
        }
    }
}
