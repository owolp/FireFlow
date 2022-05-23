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

package dev.zitech.core.persistence.data.repository.preferences

import dev.zitech.core.persistence.data.source.preferences.PreferencesDataSource
import dev.zitech.core.persistence.domain.model.preferences.PreferenceType
import dev.zitech.core.persistence.domain.repository.preferences.ContainsPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class ContainsPreferencesRepositoryImpl @Inject constructor(
    private val developmentPreferencesDataSource: PreferencesDataSource,
    private val securedPreferencesDataSource: PreferencesDataSource,
    private val standardPreferencesDataSource: PreferencesDataSource
) : ContainsPreferencesRepository {

    override fun containsBoolean(
        preferenceType: PreferenceType,
        key: String
    ): Flow<Boolean> = when (preferenceType) {
        PreferenceType.DEVELOPMENT -> {
            developmentPreferencesDataSource.containsBoolean(key)
        }
        PreferenceType.SECURED -> {
            securedPreferencesDataSource.containsBoolean(key)
        }
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.containsBoolean(key)
        }
    }

    override fun containsFloat(
        preferenceType: PreferenceType,
        key: String
    ): Flow<Boolean> = when (preferenceType) {
        PreferenceType.DEVELOPMENT -> {
            developmentPreferencesDataSource.containsFloat(key)
        }
        PreferenceType.SECURED -> {
            securedPreferencesDataSource.containsFloat(key)
        }
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.containsFloat(key)
        }
    }

    override fun containsInt(
        preferenceType: PreferenceType,
        key: String
    ): Flow<Boolean> = when (preferenceType) {
        PreferenceType.DEVELOPMENT -> {
            developmentPreferencesDataSource.containsInt(key)
        }
        PreferenceType.SECURED -> {
            securedPreferencesDataSource.containsInt(key)
        }
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.containsInt(key)
        }
    }

    override fun containsLong(
        preferenceType: PreferenceType,
        key: String
    ): Flow<Boolean> = when (preferenceType) {
        PreferenceType.DEVELOPMENT -> {
            developmentPreferencesDataSource.containsLong(key)
        }
        PreferenceType.SECURED -> {
            securedPreferencesDataSource.containsLong(key)
        }
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.containsLong(key)
        }
    }

    override fun containsString(
        preferenceType: PreferenceType,
        key: String
    ): Flow<Boolean> = when (preferenceType) {
        PreferenceType.DEVELOPMENT -> {
            developmentPreferencesDataSource.containsString(key)
        }
        PreferenceType.SECURED -> {
            securedPreferencesDataSource.containsString(key)
        }
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.containsString(key)
        }
    }
}
