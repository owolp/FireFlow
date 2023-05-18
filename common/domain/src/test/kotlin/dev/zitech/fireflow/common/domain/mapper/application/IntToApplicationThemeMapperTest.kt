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

package dev.zitech.fireflow.common.domain.mapper.application

import com.google.common.truth.Truth.assertThat
import dev.zitech.fireflow.common.domain.mapper.Mapper
import dev.zitech.fireflow.common.domain.model.application.ApplicationTheme
import org.junit.Before
import org.junit.Test

internal class IntToApplicationThemeMapperTest {

    private lateinit var sut: Mapper<Int, ApplicationTheme>

    @Before
    fun setUp() {
        sut = IntToApplicationThemeMapper()
    }

    @Test
    fun `invoke should map integer value of ApplicationTheme SYSTEM to correct enum value`() {
        val result = sut(ApplicationTheme.SYSTEM.id)

        assertThat(result).isEqualTo(ApplicationTheme.SYSTEM)
    }

    @Test
    fun `invoke should map integer value of ApplicationTheme DARK to correct enum value`() {
        val result = sut(ApplicationTheme.DARK.id)

        assertThat(result).isEqualTo(ApplicationTheme.DARK)
    }

    @Test
    fun `invoke should map integer value of ApplicationTheme LIGHT to correct enum value`() {
        val result = sut(ApplicationTheme.LIGHT.id)

        assertThat(result).isEqualTo(ApplicationTheme.LIGHT)
    }

    @Test
    fun `invoke should return ApplicationTheme SYSTEM for unsupported integer value`() {
        val unsupportedValue = 999

        val result = sut(unsupportedValue)

        assertThat(result).isEqualTo(ApplicationTheme.SYSTEM)
    }
}
