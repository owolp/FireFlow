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
import dev.zitech.core.common.domain.model.ApplicationLanguage
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.settings.R
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
    fun getDialogThemes() {
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

    @Test
    fun getDialogLanguageTitle() {
        // Arrange
        val text = DataFactory.createRandomString()
        every { stringsProvider(R.string.appearance_dialog_language_title) } returns text

        // Act
        val result = sut.getDialogLanguageTitle()

        // Assert
        assertThat(result).isEqualTo(text)
        verify { stringsProvider(R.string.appearance_dialog_language_title) }
    }

    @Test
    fun getDialogLanguages() {
        // Arrange
        every { stringsProvider(ApplicationLanguage.SYSTEM.text) } returns "System"
        every { stringsProvider(ApplicationLanguage.ENGLISH.text) } returns "English"
        every { stringsProvider(ApplicationLanguage.BULGARIAN.text) } returns "Bulgarian"

        val applicationLanguage = ApplicationLanguage.ENGLISH

        // Act
        val result = sut.getDialogLanguages(applicationLanguage)

        // Assert
        assertThat(result.size).isEqualTo(ApplicationLanguage.values().size)
        with(result[ApplicationLanguage.SYSTEM.id]) {
            assertThat(id).isEqualTo(ApplicationLanguage.SYSTEM.id)
            assertThat(text).isEqualTo("System")
            assertThat(selected).isFalse()
            assertThat(enabled).isTrue()
        }
        with(result[ApplicationLanguage.ENGLISH.id]) {
            assertThat(id).isEqualTo(ApplicationLanguage.ENGLISH.id)
            assertThat(text).isEqualTo("English")
            assertThat(selected).isTrue()
            assertThat(enabled).isTrue()
        }
        with(result[ApplicationLanguage.BULGARIAN.id]) {
            assertThat(id).isEqualTo(ApplicationLanguage.BULGARIAN.id)
            assertThat(text).isEqualTo("Bulgarian")
            assertThat(selected).isFalse()
            assertThat(enabled).isTrue()
        }
    }
}