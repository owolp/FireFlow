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

package dev.zitech.core.persistence.framework.preference.mapper

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.domain.model.ApplicationTheme
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ApplicationThemeToIntMapperTest {

    private lateinit var sut: dev.zitech.settings.domain.mapper.ApplicationThemeToIntMapper

    @BeforeEach
    fun setup() {
        sut = dev.zitech.settings.domain.mapper.ApplicationThemeToIntMapper()
    }

    @Test
    @DisplayName("WHEN input theme is System THEN return SYSTEM id")
    fun system() {
        // Arrange
        val theme = ApplicationTheme.SYSTEM

        // Act
        val result = sut(theme)

        // Assert
        assertThat(result).isEqualTo(ApplicationTheme.SYSTEM.id)
    }

    @Test
    @DisplayName("WHEN input theme is Dark THEN return DARK id")
    fun dark() {
        // Arrange
        val theme = ApplicationTheme.DARK

        // Act
        val result = sut(theme)

        // Assert
        assertThat(result).isEqualTo(ApplicationTheme.DARK.id)
    }

    @Test
    @DisplayName("WHEN input theme is Light THEN return LIGHT id")
    fun light() {
        // Arrange
        val theme = ApplicationTheme.LIGHT

        // Act
        val result = sut(theme)

        // Assert
        assertThat(result).isEqualTo(ApplicationTheme.LIGHT.id)
    }
}
