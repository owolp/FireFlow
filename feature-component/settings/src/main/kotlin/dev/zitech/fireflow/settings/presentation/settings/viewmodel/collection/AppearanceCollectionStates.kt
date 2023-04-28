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

package dev.zitech.fireflow.settings.presentation.settings.viewmodel.collection

import dev.zitech.fireflow.common.domain.model.application.ApplicationLanguage
import dev.zitech.fireflow.common.domain.model.application.ApplicationTheme
import dev.zitech.fireflow.common.domain.usecase.application.GetApplicationThemeValueUseCase
import dev.zitech.fireflow.common.domain.usecase.application.SetApplicationThemeValueUseCase
import dev.zitech.fireflow.settings.domain.usecase.application.GetApplicationLanguageValueUseCase
import dev.zitech.fireflow.settings.domain.usecase.application.SetApplicationLanguageValueUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.first

internal class AppearanceCollectionStates @Inject constructor(
    private val getApplicationLanguageValueUseCase: GetApplicationLanguageValueUseCase,
    private val getApplicationThemeValueUseCase: GetApplicationThemeValueUseCase,
    private val setApplicationLanguageValueUseCase: SetApplicationLanguageValueUseCase,
    private val setApplicationThemeValueUseCase: SetApplicationThemeValueUseCase
) {

    fun getApplicationLanguageValue(): ApplicationLanguage =
        getApplicationLanguageValueUseCase()

    suspend fun getApplicationThemeValue(): ApplicationTheme =
        getApplicationThemeValueUseCase().first()

    fun setApplicationLanguageValue(applicationLanguage: ApplicationLanguage) {
        setApplicationLanguageValueUseCase(applicationLanguage)
    }

    suspend fun setApplicationThemeValue(applicationTheme: ApplicationTheme) {
        setApplicationThemeValueUseCase(applicationTheme)
    }
}
