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

package dev.zitech.settings.domain.usecase

import dev.zitech.core.common.domain.model.ApplicationLanguage
import dev.zitech.settings.domain.usecase.application.SetApplicationLanguageValueUseCase
import dev.zitech.settings.frawework.locale.ApplicationLocale
import io.mockk.Runs
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class SetApplicationLanguageValueUseCaseTest {

    @Test
    fun invoke() {
        // Arrange
        val language = ApplicationLanguage.BULGARIAN
        val applicationLocale = mockk<ApplicationLocale>()
        every { applicationLocale.set(language) } just Runs

        val sut = SetApplicationLanguageValueUseCase(applicationLocale)

        // Act
        sut(language)

        // Assert
        coVerify { applicationLocale.set(language) }
    }
}
