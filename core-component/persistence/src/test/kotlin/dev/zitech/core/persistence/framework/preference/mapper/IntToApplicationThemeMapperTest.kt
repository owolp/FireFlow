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
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.model.ApplicationTheme
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class IntToApplicationThemeMapperTest {

    private lateinit var sut: IntToApplicationThemeMapper

    @BeforeEach
    fun setup() {
        sut = IntToApplicationThemeMapper()
    }

    @Test
    @DisplayName("WHEN input theme is System Id THEN return SYSTEM")
    fun system() {
        // Arrange
        val themeId = ApplicationTheme.SYSTEM.id

        // Act
        val result = sut(themeId)

        // Assert
        assertThat(result).isEqualTo(ApplicationTheme.SYSTEM)
    }

    @Test
    @DisplayName("WHEN input theme is Dark Id THEN return DARK")
    fun dark() {
        // Arrange
        val themeId = ApplicationTheme.DARK.id

        // Act
        val result = sut(themeId)

        // Assert
        assertThat(result).isEqualTo(ApplicationTheme.DARK)
    }

    @Test
    @DisplayName("WHEN input theme is Light THEN return LIGHT id")
    fun light() {
        // Arrange
        val themeId = ApplicationTheme.LIGHT.id

        // Act
        val result = sut(themeId)

        // Assert
        assertThat(result).isEqualTo(ApplicationTheme.LIGHT)
    }

    @Test
    @DisplayName("WHEN input theme is other id THEN return SYSTEM")
    fun other() {
        // Arrange
        val themeId = DataFactory.createRandomInt(min = 10)

        // Act
        val result = sut(themeId)

        // Assert
        assertThat(result).isEqualTo(ApplicationTheme.SYSTEM)
    }
}
