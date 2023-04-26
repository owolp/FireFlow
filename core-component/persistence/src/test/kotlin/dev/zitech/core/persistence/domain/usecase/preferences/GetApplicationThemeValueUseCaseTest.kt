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

package dev.zitech.core.persistence.domain.usecase.preferences

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.persistence.data.preferences.repository.FakePreferencesRepository
import dev.zitech.core.persistence.domain.model.preferences.IntPreference
import dev.zitech.core.persistence.domain.model.preferences.PreferenceType
import dev.zitech.settings.domain.mapper.IntToApplicationThemeMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class GetApplicationThemeValueUseCaseTest {

    private val intToApplicationThemeMapper =
        dev.zitech.settings.domain.mapper.IntToApplicationThemeMapper()
    private val getPreferencesRepository = FakePreferencesRepository()

    private lateinit var sut: dev.zitech.settings.domain.usecase.GetApplicationThemeValueUseCase

    @BeforeEach
    fun setup() {
        sut = dev.zitech.settings.domain.usecase.GetApplicationThemeValueUseCase(
            getPreferencesRepository,
            intToApplicationThemeMapper
        )
    }

    @Test
    @DisplayName("WHEN getPreferencesRepository includes key THEN return correct value")
    fun invoke() = runTest {
        // Arrange
        getPreferencesRepository.saveInt(
            PreferenceType.STANDARD,
            IntPreference.APPLICATION_THEME.key,
            ApplicationTheme.DARK.id
        )

        sut().test {
            // Act & Assert
            assertThat(awaitItem()).isEqualTo(ApplicationTheme.DARK)
            awaitComplete()
        }
    }

    @Test
    @DisplayName("WHEN getPreferencesRepository does not include key THEN return System")
    fun defaultValue() = runTest {
        sut().test {
            // Act & Assert
            assertThat(awaitItem()).isEqualTo(ApplicationTheme.SYSTEM)
            awaitComplete()
        }
    }
}
