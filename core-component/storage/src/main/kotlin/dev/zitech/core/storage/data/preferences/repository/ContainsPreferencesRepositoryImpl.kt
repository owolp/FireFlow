/*
 * Copyright (C) 2022 Zitech
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

package dev.zitech.core.storage.data.preferences.repository

import dev.zitech.core.storage.domain.model.PreferenceType
import dev.zitech.core.storage.domain.repository.ContainsPreferencesRepository
import dev.zitech.core.storage.framework.preference.PreferencesDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class ContainsPreferencesRepositoryImpl @Inject constructor(
    private val standardPreferencesDataSource: PreferencesDataSource
) : ContainsPreferencesRepository {

    override fun containsBoolean(
        preferenceType: PreferenceType,
        key: String
    ): Flow<Boolean> = when (preferenceType) {
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.containsBoolean(key)
        }
    }

    override fun containsFloat(
        preferenceType: PreferenceType,
        key: String
    ): Flow<Boolean> = when (preferenceType) {
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.containsFloat(key)
        }
    }

    override fun containsInt(
        preferenceType: PreferenceType,
        key: String
    ): Flow<Boolean> = when (preferenceType) {
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.containsInt(key)
        }
    }

    override fun containsLong(
        preferenceType: PreferenceType,
        key: String
    ): Flow<Boolean> = when (preferenceType) {
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.containsLong(key)
        }
    }

    override fun containsString(
        preferenceType: PreferenceType,
        key: String
    ): Flow<Boolean> = when (preferenceType) {
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.containsString(key)
        }
    }
}
