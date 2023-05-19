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

import dev.zitech.fireflow.common.data.local.database.FireFlowDatabase
import dev.zitech.fireflow.common.data.source.preferences.PreferencesDataSource
import dev.zitech.fireflow.common.domain.mapper.application.ApplicationThemeToIntMapper
import dev.zitech.fireflow.common.domain.mapper.application.IntToApplicationThemeMapper
import dev.zitech.fireflow.common.domain.model.application.ApplicationTheme
import dev.zitech.fireflow.common.domain.model.preferences.IntPreference
import dev.zitech.fireflow.common.domain.repository.application.ApplicationRepository
import dev.zitech.fireflow.core.dispatcher.AppDispatchers
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class ApplicationRepositoryImpl @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val applicationThemeToIntMapper: ApplicationThemeToIntMapper,
    private val developmentPreferencesDataSource: PreferencesDataSource,
    private val fireFlowDatabase: FireFlowDatabase,
    private val intToApplicationThemeMapper: IntToApplicationThemeMapper,
    private val securedPreferencesDataSource: PreferencesDataSource,
    private val standardPreferencesDataSource: PreferencesDataSource
) : ApplicationRepository {

    private companion object {
        const val CLEAR_DELAY_TIME = 5000L
    }

    override suspend fun clearApplicationStorage(): OperationResult<Unit> = withContext(appDispatchers.io) {
        standardPreferencesDataSource.removeAll()
        securedPreferencesDataSource.removeAll()
        developmentPreferencesDataSource.removeAll()
        fireFlowDatabase.clearAllTables()

        // https://stackoverflow.com/questions/44244508/room-persistance-library-delete-all#comment89515848_49545016
        // Adding a delay since clearAllTables() is asynchronous and there is no way to tell when it completes
        delay(CLEAR_DELAY_TIME)

        return@withContext OperationResult.Success(Unit)
    }

    override fun getApplicationTheme(): Flow<ApplicationTheme> =
        standardPreferencesDataSource.getInt(
            IntPreference.APPLICATION_THEME.key,
            IntPreference.APPLICATION_THEME.defaultValue
        ).map(intToApplicationThemeMapper::invoke)

    override suspend fun setApplicationTheme(applicationTheme: ApplicationTheme) {
        standardPreferencesDataSource.saveInt(
            IntPreference.APPLICATION_THEME.key,
            applicationThemeToIntMapper(applicationTheme)
        )
    }
}
