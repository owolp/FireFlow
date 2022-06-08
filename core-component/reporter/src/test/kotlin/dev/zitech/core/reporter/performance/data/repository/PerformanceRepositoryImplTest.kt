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

package dev.zitech.core.reporter.performance.data.repository

import com.google.common.truth.Truth
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.framework.applicationconfig.FakeAppConfigProvider
import dev.zitech.core.reporter.performance.domain.repository.PerformanceRepository
import dev.zitech.core.reporter.performance.framework.FakePerformanceReporter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PerformanceRepositoryImplTest {

    private val appConfigProvider = FakeAppConfigProvider()
    private val performanceReporter = FakePerformanceReporter()

    private lateinit var sut: PerformanceRepository

    @BeforeEach
    fun setup() {
        sut = PerformanceRepositoryImpl(
            appConfigProvider,
            performanceReporter
        )
    }

    @Test
    fun setCollectionEnabled() {
        // Arrange
        val expectedResult = DataFactory.createRandomBoolean()

        // Act
        sut.setCollectionEnabled(expectedResult)

        // Assert
        Truth.assertThat(performanceReporter.setCollectionEnabledValue).isEqualTo(expectedResult)
    }
}