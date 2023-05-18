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

package dev.zitech.fireflow.common.data.repository.application

import dev.zitech.fireflow.common.data.source.preferences.PreferencesDataSource
import dev.zitech.fireflow.common.domain.mapper.application.ApplicationThemeToIntMapper
import dev.zitech.fireflow.common.domain.mapper.application.IntToApplicationThemeMapper
import dev.zitech.fireflow.common.domain.model.application.ApplicationTheme
import dev.zitech.fireflow.common.domain.model.preferences.IntPreference
import dev.zitech.fireflow.common.domain.repository.application.ApplicationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ApplicationRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
    private val intToApplicationThemeMapper: IntToApplicationThemeMapper,
    private val applicationThemeToIntMapper: ApplicationThemeToIntMapper
) : ApplicationRepository {

    override fun getApplicationTheme(): Flow<ApplicationTheme> =
        preferencesDataSource.getInt(
            IntPreference.APPLICATION_THEME.key,
            IntPreference.APPLICATION_THEME.defaultValue
        ).map(intToApplicationThemeMapper::invoke)

    override suspend fun setApplicationTheme(applicationTheme: ApplicationTheme) {
        preferencesDataSource.saveInt(
            IntPreference.APPLICATION_THEME.key,
            applicationThemeToIntMapper(applicationTheme)
        )
    }
}
