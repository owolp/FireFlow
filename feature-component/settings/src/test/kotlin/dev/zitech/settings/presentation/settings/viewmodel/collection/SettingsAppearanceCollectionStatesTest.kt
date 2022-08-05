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
import dev.zitech.core.common.domain.model.ApplicationLanguage
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.persistence.domain.usecase.preferences.GetApplicationThemeValueUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.SetApplicationThemeValueUseCase
import dev.zitech.settings.domain.usecase.GetApplicationLanguageValueUseCase
import dev.zitech.settings.domain.usecase.SetApplicationLanguageValueUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class SettingsAppearanceCollectionStatesTest {

    private val getApplicationThemeValueUseCase = mockk<GetApplicationThemeValueUseCase>()
    private val setApplicationThemeValueUseCase = mockk<SetApplicationThemeValueUseCase>(relaxed = true)
    private val getApplicationLanguageValueUseCase = mockk<GetApplicationLanguageValueUseCase>()
    private val setApplicationLanguageValueUseCase = mockk<SetApplicationLanguageValueUseCase>(relaxed = true)

    private lateinit var sut: SettingsAppearanceCollectionStates

    @BeforeEach
    fun setup() {
        sut = SettingsAppearanceCollectionStates(
            getApplicationThemeValueUseCase,
            setApplicationThemeValueUseCase,
            getApplicationLanguageValueUseCase,
            setApplicationLanguageValueUseCase
        )
    }

    @Test
    fun setApplicationThemeValue() = runTest {
        // Arrange
        val applicationTheme = ApplicationTheme.DARK

        // Act
        sut.setApplicationThemeValue(applicationTheme)

        // Assert
        coVerify { setApplicationThemeValueUseCase(applicationTheme) }
    }

    @Test
    fun getApplicationThemeValue() = runTest {
        // Arrange
        val applicationTheme = ApplicationTheme.DARK
        coEvery { getApplicationThemeValueUseCase() } returns flowOf(applicationTheme)

        // Act
        val result = sut.getApplicationThemeValue()

        // Assert
        assertThat(result).isEqualTo(applicationTheme)
        coVerify { getApplicationThemeValueUseCase() }
    }

    @Test
    fun setApplicationLanguageValue() {
        // Arrange
        val applicationLanguage = ApplicationLanguage.BULGARIAN

        // Act
        sut.setApplicationLanguageValue(applicationLanguage)

        // Assert
        coVerify { setApplicationLanguageValueUseCase(applicationLanguage) }
    }

    @Test
    fun getApplicationLanguageValue() = runTest {
        // Arrange
        val applicationLanguage = ApplicationLanguage.BULGARIAN
        coEvery { getApplicationLanguageValueUseCase() } returns applicationLanguage

        // Act
        val result = sut.getApplicationLanguageValue()

        // Assert
        assertThat(result).isEqualTo(applicationLanguage)
        coVerify { getApplicationLanguageValueUseCase() }
    }
}
