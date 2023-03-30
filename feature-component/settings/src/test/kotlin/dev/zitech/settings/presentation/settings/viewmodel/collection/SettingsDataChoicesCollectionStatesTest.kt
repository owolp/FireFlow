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

package dev.zitech.settings.presentation.settings.viewmodel.collection

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.persistence.domain.usecase.preferences.GetAllowPersonalizedAdsValueUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetAnalyticsCollectionValueUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetCrashReporterCollectionValueUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetPerformanceCollectionValueUseCase
import dev.zitech.core.reporter.analytics.domain.usecase.AllowPersonalizedAdsUseCase
import dev.zitech.core.reporter.analytics.domain.usecase.SetAnalyticsCollectionUseCase
import dev.zitech.core.reporter.crash.domain.usecase.SetCrashReporterCollectionUseCase
import dev.zitech.core.reporter.performance.domain.usecase.SetPerformanceCollectionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class SettingsDataChoicesCollectionStatesTest {

    private val getAnalyticsCollectionValueUseCase = mockk<GetAnalyticsCollectionValueUseCase>()
    private val setAnalyticsCollectionUseCase = mockk<SetAnalyticsCollectionUseCase>(relaxed = true)
    private val getAllowPersonalizedAdsValueUseCase = mockk<GetAllowPersonalizedAdsValueUseCase>()
    private val allowPersonalizedAdsUseCase = mockk<AllowPersonalizedAdsUseCase>(
        relaxUnitFun = true
    )
    private val getCrashReporterCollectionValueUseCase = mockk<GetCrashReporterCollectionValueUseCase>()
    private val setCrashReporterCollectionUseCase = mockk<SetCrashReporterCollectionUseCase>(
        relaxed = true
    )
    private val getPerformanceCollectionValueUseCase = mockk<GetPerformanceCollectionValueUseCase>()
    private val setPerformanceCollectionUseCase = mockk<SetPerformanceCollectionUseCase>(
        relaxed = true
    )

    private lateinit var sut: DataChoicesCollectionStates

    @BeforeEach
    fun setup() {
        sut = DataChoicesCollectionStates(
            getAnalyticsCollectionValueUseCase,
            setAnalyticsCollectionUseCase,
            getAllowPersonalizedAdsValueUseCase,
            allowPersonalizedAdsUseCase,
            getCrashReporterCollectionValueUseCase,
            setCrashReporterCollectionUseCase,
            getPerformanceCollectionValueUseCase,
            setPerformanceCollectionUseCase
        )
    }

    @Test
    fun setAnalyticsCollection() = runTest {
        // Arrange
        val checked = DataFactory.createRandomBoolean()

        // Act
        sut.setAnalyticsCollection(checked)

        // Assert
        coVerify { setAnalyticsCollectionUseCase(checked) }
    }

    @Test
    fun getAnalyticsCollectionValue() = runTest {
        // Arrange
        val checked = DataFactory.createRandomBoolean()
        coEvery { getAnalyticsCollectionValueUseCase() } returns checked

        // Act
        val result = sut.getAnalyticsCollectionValue()

        // Assert
        assertThat(result).isEqualTo(checked)
        coVerify { getAnalyticsCollectionValueUseCase() }
    }

    @Test
    fun setAllowPersonalizedAdsValue() = runTest {
        // Arrange
        val checked = DataFactory.createRandomBoolean()

        // Act
        sut.setAllowPersonalizedAdsValue(checked)

        // Assert
        coVerify { allowPersonalizedAdsUseCase(checked) }
    }

    @Test
    fun getAllowPersonalizedAdsValue() = runTest {
        // Arrange
        val checked = DataFactory.createRandomBoolean()
        coEvery { getAllowPersonalizedAdsValueUseCase() } returns checked

        // Act
        val result = sut.getAllowPersonalizedAdsValue()

        // Assert
        assertThat(result).isEqualTo(checked)
        coVerify { getAllowPersonalizedAdsValueUseCase() }
    }

    @Test
    fun setCrashReporterCollection() = runTest {
        // Arrange
        val checked = DataFactory.createRandomBoolean()

        // Act
        sut.setCrashReporterCollection(checked)

        // Assert
        coVerify { setCrashReporterCollectionUseCase(checked) }
    }

    @Test
    fun getCrashReporterCollectionValue() = runTest {
        // Arrange
        val checked = DataFactory.createRandomBoolean()
        coEvery { getCrashReporterCollectionValueUseCase() } returns checked

        // Act
        val result = sut.getCrashReporterCollectionValue()

        // Assert
        assertThat(result).isEqualTo(checked)
        coVerify { getCrashReporterCollectionValueUseCase() }
    }

    @Test
    fun setPerformanceCollection() = runTest {
        // Arrange
        val checked = DataFactory.createRandomBoolean()

        // Act
        sut.setPerformanceCollection(checked)

        // Assert
        coVerify { setPerformanceCollectionUseCase(checked) }
    }

    @Test
    fun getPerformanceCollectionValue() = runTest {
        // Arrange
        val checked = DataFactory.createRandomBoolean()
        coEvery { getPerformanceCollectionValueUseCase() } returns checked

        // Act
        val result = sut.getPerformanceCollectionValue()

        // Assert
        assertThat(result).isEqualTo(checked)
        coVerify { getPerformanceCollectionValueUseCase() }
    }
}
