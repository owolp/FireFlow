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

package dev.zitech.core.persistence.domain.usecase.preferences

import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.persistence.domain.model.preferences.IntPreference
import dev.zitech.core.persistence.domain.model.preferences.PreferenceType
import dev.zitech.core.persistence.domain.repository.preferences.SavePreferencesRepository
import dev.zitech.core.persistence.framework.preference.mapper.ApplicationThemeToIntMapper
import javax.inject.Inject

class SetApplicationThemeValueUseCase @Inject constructor(
    private val savePreferencesRepository: SavePreferencesRepository,
    private val applicationThemeToIntMapper: ApplicationThemeToIntMapper
) {

    suspend operator fun invoke(applicationTheme: ApplicationTheme) =
        savePreferencesRepository.saveInt(
            PreferenceType.STANDARD,
            IntPreference.APPLICATION_THEME.key,
            applicationThemeToIntMapper(applicationTheme)
        )
}
