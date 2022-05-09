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
import dev.zitech.core.persistence.data.preferences.repository.FakePreferencesRepository
import dev.zitech.core.persistence.domain.model.preferences.PreferenceType
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class GetCrashReporterCollectionValueUseCaseTest {

    private val getPreferencesRepository = FakePreferencesRepository()

    private lateinit var sut: GetCrashReporterCollectionValueUseCase

    @BeforeEach
    fun setup() {
        sut = GetCrashReporterCollectionValueUseCase(getPreferencesRepository)
    }

    @Test
    @DisplayName("WHEN getPreferencesRepository includes key THEN return correct value")
    fun invoke() = runBlocking {
        // Arrange
        getPreferencesRepository.saveBoolean(
            PreferenceType.STANDARD,
            "crash_reporter_collection",
            true
        )

        // Act
        val result = sut()

        // Assert
        assertThat(result).isTrue()
    }

    @Test
    @DisplayName("WHEN getPreferencesRepository does not include key THEN return defaultValue")
    fun defaultValue() = runBlocking {
        // Act & Assert
        val result = sut()

        // Assert
        assertThat(result).isFalse()
    }
}