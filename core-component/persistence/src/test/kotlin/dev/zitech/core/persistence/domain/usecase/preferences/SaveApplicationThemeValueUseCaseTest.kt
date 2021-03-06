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

package dev.zitech.core.persistence.domain.usecase.preferences

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.persistence.data.preferences.repository.FakePreferencesRepository
import dev.zitech.core.persistence.domain.model.preferences.IntPreference
import dev.zitech.core.persistence.domain.model.preferences.PreferenceType
import dev.zitech.core.persistence.framework.preference.mapper.ApplicationThemeToIntMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class SaveApplicationThemeValueUseCaseTest {

    private val savePreferencesRepository = FakePreferencesRepository()
    private val applicationThemeToIntMapper = ApplicationThemeToIntMapper()

    private lateinit var sut: SaveApplicationThemeValueUseCase

    @BeforeEach
    fun setup() {
        sut = SaveApplicationThemeValueUseCase(
            savePreferencesRepository,
            applicationThemeToIntMapper
        )
    }

    @Test
    fun invoke() = runTest {
        // Act
        sut(ApplicationTheme.DARK)

        // Assert
        assertThat(
            savePreferencesRepository.getInt(
                PreferenceType.STANDARD,
                IntPreference.APPLICATION_THEME.key,
                ApplicationTheme.SYSTEM.id
            ).first()
        ).isEqualTo(ApplicationTheme.DARK.id)
    }
}