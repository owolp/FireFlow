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

package dev.zitech.settings.presentation.settings.viewmodel.theme

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.settings.R
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class SettingsStringsProviderTest {

    private val stringsProvider = mockk<StringsProvider>()

    private lateinit var sut: SettingsStringsProvider

    @BeforeEach
    fun setup() {
        sut = SettingsStringsProvider(
            stringsProvider
        )
    }

    @Test
    fun getDialogThemeTitle() {
        // Arrange
        val text = DataFactory.createRandomString()
        every { stringsProvider(R.string.appearance_dialog_theme_title) } returns text

        // Act
        val result = sut.getDialogThemeTitle()

        // Assert
        assertThat(result).isEqualTo(text)
        verify { stringsProvider(R.string.appearance_dialog_theme_title) }
    }

    @Test
    fun getDialogThemes() = runTest {
        // Arrange
        every { stringsProvider(ApplicationTheme.SYSTEM.text) } returns "System"
        every { stringsProvider(ApplicationTheme.DARK.text) } returns "Dark"
        every { stringsProvider(ApplicationTheme.LIGHT.text) } returns "Light"

        val applicationTheme = ApplicationTheme.DARK

        // Act
        val result = sut.getDialogThemes(applicationTheme)

        // Assert
        assertThat(result.size).isEqualTo(ApplicationTheme.values().size)
        with(result[ApplicationTheme.SYSTEM.id]) {
            assertThat(id).isEqualTo(ApplicationTheme.SYSTEM.id)
            assertThat(text).isEqualTo("System")
            assertThat(selected).isFalse()
            assertThat(enabled).isTrue()
        }
        with(result[ApplicationTheme.DARK.id]) {
            assertThat(id).isEqualTo(ApplicationTheme.DARK.id)
            assertThat(text).isEqualTo("Dark")
            assertThat(selected).isTrue()
            assertThat(enabled).isTrue()
        }
        with(result[ApplicationTheme.LIGHT.id]) {
            assertThat(id).isEqualTo(ApplicationTheme.LIGHT.id)
            assertThat(text).isEqualTo("Light")
            assertThat(selected).isFalse()
            assertThat(enabled).isTrue()
        }
    }
}