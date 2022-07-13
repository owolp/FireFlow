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

package dev.zitech.settings.presentation.settings.viewmodel.collection

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.persistence.domain.usecase.preferences.GetAnalyticsCollectionValueUseCase
import dev.zitech.core.reporter.analytics.domain.usecase.SetAnalyticsCollectionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class SettingsAnalyticsCollectionStatesTest {

    private val getAnalyticsCollectionValueUseCase = mockk<GetAnalyticsCollectionValueUseCase>()
    private val setAnalyticsCollectionUseCase = mockk<SetAnalyticsCollectionUseCase>(relaxed = true)

    private lateinit var sut: SettingsAnalyticsCollectionStates

    @BeforeEach
    fun setup() {
        sut = SettingsAnalyticsCollectionStates(
            getAnalyticsCollectionValueUseCase,
            setAnalyticsCollectionUseCase
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
}