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
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    override suspend fun clearApplicationStorage(): OperationResult<Unit> =
        withContext(appDispatchers.io) {
            val standardResult = async { standardPreferencesDataSource.removeAll() }
            val securedResult = async { securedPreferencesDataSource.removeAll() }
            val developmentResult = async { developmentPreferencesDataSource.removeAll() }

            val allResults = listOf(standardResult, securedResult, developmentResult).awaitAll()

            if (allResults.all { it is OperationResult.Success }) {
                fireFlowDatabase.clearAllTables()
                OperationResult.Success(Unit)
            } else {
                allResults.first { it is OperationResult.Failure }
            }
        }

    override fun getApplicationTheme(): Flow<OperationResult<ApplicationTheme>> =
        standardPreferencesDataSource.getInt(
            IntPreference.APPLICATION_THEME.key
        ).map { operationResult ->
            when (operationResult) {
                is OperationResult.Success -> OperationResult.Success(
                    intToApplicationThemeMapper.invoke(
                        operationResult.data
                    )
                )
                is OperationResult.Failure -> {
                    when (operationResult.error) {
                        Error.PreferenceNotFound -> OperationResult.Success(
                            intToApplicationThemeMapper.invoke(
                                IntPreference.APPLICATION_THEME.defaultValue
                            )
                        )
                        else -> OperationResult.Failure(operationResult.error)
                    }
                }
            }
        }

    override suspend fun setApplicationTheme(applicationTheme: ApplicationTheme): OperationResult<Unit> =
        standardPreferencesDataSource.saveInt(
            IntPreference.APPLICATION_THEME.key,
            applicationThemeToIntMapper(applicationTheme)
        )
}
