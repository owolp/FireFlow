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

internal class ApplicationThemeToIntMapperTest {

    private lateinit var sut: Mapper<ApplicationTheme, Int>

    @Before
    fun setUp() {
        sut = ApplicationThemeToIntMapper()
    }

    @Test
    fun `invoke should map ApplicationTheme SYSTEM to correct integer value`() {
        val result = sut(ApplicationTheme.SYSTEM)

        assertThat(result).isEqualTo(ApplicationTheme.SYSTEM.id)
    }

    @Test
    fun `invoke should map ApplicationTheme DARK to correct integer value`() {
        val result = sut(ApplicationTheme.DARK)

        assertThat(result).isEqualTo(ApplicationTheme.DARK.id)
    }

    @Test
    fun `invoke should map ApplicationTheme LIGHT to correct integer value`() {
        val result = sut(ApplicationTheme.LIGHT)

        assertThat(result).isEqualTo(ApplicationTheme.LIGHT.id)
    }
}
