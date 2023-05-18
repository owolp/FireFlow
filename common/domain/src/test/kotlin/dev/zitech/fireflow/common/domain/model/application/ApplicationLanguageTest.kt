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
import java.util.Locale
import org.junit.Test

internal class ApplicationLanguageTest {

    @Test
    fun `getApplicationLanguage() with valid ID should return corresponding enum value`() {
        val english = ApplicationLanguage.getApplicationLanguage(1)
        val bulgarian = ApplicationLanguage.getApplicationLanguage(2)

        assertThat(english).isEqualTo(ApplicationLanguage.ENGLISH)
        assertThat(bulgarian).isEqualTo(ApplicationLanguage.BULGARIAN)
    }

    @Test
    fun `getApplicationLanguage() with invalid ID should return SYSTEM enum value`() {
        val invalidId = 999
        val result = ApplicationLanguage.getApplicationLanguage(invalidId)

        assertThat(result).isEqualTo(ApplicationLanguage.SYSTEM)
    }

    @Test
    fun `enum values should have correct properties`() {
        assertThat(ApplicationLanguage.SYSTEM.id).isEqualTo(0)
        assertThat(ApplicationLanguage.SYSTEM.text).isEqualTo(R.string.application_language_system)
        assertThat(ApplicationLanguage.SYSTEM.locale).isNull()

        assertThat(ApplicationLanguage.ENGLISH.id).isEqualTo(1)
        assertThat(ApplicationLanguage.ENGLISH.text).isEqualTo(R.string.application_language_english)
        assertThat(ApplicationLanguage.ENGLISH.locale).isEqualTo(Locale.ENGLISH)

        assertThat(ApplicationLanguage.BULGARIAN.id).isEqualTo(2)
        assertThat(ApplicationLanguage.BULGARIAN.text).isEqualTo(R.string.application_language_bulgarian)
        assertThat(ApplicationLanguage.BULGARIAN.locale).isEqualTo(Locale("bg", "BG"))
    }
}
