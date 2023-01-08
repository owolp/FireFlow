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
import dev.zitech.core.persistence.domain.repository.preferences.GetPreferencesRepository
import dev.zitech.core.persistence.framework.preference.mapper.IntToApplicationThemeMapper
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetApplicationThemeValueUseCase @Inject constructor(
    private val getPreferencesRepository: GetPreferencesRepository,
    private val intToApplicationThemeMapper: IntToApplicationThemeMapper
) {

    operator fun invoke(): Flow<ApplicationTheme> =
        getPreferencesRepository.getInt(
            PreferenceType.STANDARD,
            IntPreference.APPLICATION_THEME.key,
            IntPreference.APPLICATION_THEME.defaultValue
        ).map {
            intToApplicationThemeMapper(it)
        }
}
