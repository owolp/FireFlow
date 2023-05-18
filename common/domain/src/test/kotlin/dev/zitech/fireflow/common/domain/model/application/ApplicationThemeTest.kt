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

package dev.zitech.fireflow.common.domain.model.application

import com.google.common.truth.Truth.assertThat
import dev.zitech.fireflow.common.domain.R
import org.junit.Test

internal class ApplicationThemeTest {

    @Test
    fun `getApplicationTheme() with valid ID should return corresponding enum value`() {
        val dark = ApplicationTheme.getApplicationTheme(1)
        val light = ApplicationTheme.getApplicationTheme(2)

        assertThat(dark).isEqualTo(ApplicationTheme.DARK)
        assertThat(light).isEqualTo(ApplicationTheme.LIGHT)
    }

    @Test
    fun `getApplicationTheme() with invalid ID should return SYSTEM enum value`() {
        val invalidId = 999
        val result = ApplicationTheme.getApplicationTheme(invalidId)

        assertThat(result).isEqualTo(ApplicationTheme.SYSTEM)
    }

    @Test
    fun `enum values should have correct properties`() {
        assertThat(ApplicationTheme.SYSTEM.id).isEqualTo(0)
        assertThat(ApplicationTheme.SYSTEM.text).isEqualTo(R.string.application_theme_system)

        assertThat(ApplicationTheme.DARK.id).isEqualTo(1)
        assertThat(ApplicationTheme.DARK.text).isEqualTo(R.string.application_theme_dark)

        assertThat(ApplicationTheme.LIGHT.id).isEqualTo(2)
        assertThat(ApplicationTheme.LIGHT.text).isEqualTo(R.string.application_theme_light)
    }
}
