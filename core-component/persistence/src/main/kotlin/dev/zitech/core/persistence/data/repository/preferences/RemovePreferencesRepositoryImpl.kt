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

import dev.zitech.core.persistence.domain.model.preferences.PreferenceType
import dev.zitech.core.persistence.domain.repository.preferences.RemovePreferencesRepository
import dev.zitech.core.persistence.domain.source.preferences.PreferencesDataSource
import javax.inject.Inject

internal class RemovePreferencesRepositoryImpl @Inject constructor(
    private val developmentPreferencesDataSource: PreferencesDataSource,
    private val securedPreferencesDataSource: PreferencesDataSource,
    private val standardPreferencesDataSource: PreferencesDataSource
) : RemovePreferencesRepository {

    override suspend fun removeBoolean(
        preferenceType: PreferenceType,
        key: String
    ) {
        when (preferenceType) {
            PreferenceType.DEVELOPMENT -> {
                developmentPreferencesDataSource.removeBoolean(key)
            }
            PreferenceType.SECURED -> {
                securedPreferencesDataSource.removeBoolean(key)
            }
            PreferenceType.STANDARD -> {
                standardPreferencesDataSource.removeBoolean(key)
            }
        }
    }

    override suspend fun removeFloat(
        preferenceType: PreferenceType,
        key: String
    ) {
        when (preferenceType) {
            PreferenceType.DEVELOPMENT -> {
                developmentPreferencesDataSource.removeFloat(key)
            }
            PreferenceType.SECURED -> {
                securedPreferencesDataSource.removeFloat(key)
            }
            PreferenceType.STANDARD -> {
                standardPreferencesDataSource.removeFloat(key)
            }
        }
    }

    override suspend fun removeInt(
        preferenceType: PreferenceType,
        key: String
    ) {
        when (preferenceType) {
            PreferenceType.DEVELOPMENT -> {
                developmentPreferencesDataSource.removeInt(key)
            }
            PreferenceType.SECURED -> {
                securedPreferencesDataSource.removeInt(key)
            }
            PreferenceType.STANDARD -> {
                standardPreferencesDataSource.removeInt(key)
            }
        }
    }

    override suspend fun removeLong(
        preferenceType: PreferenceType,
        key: String
    ) {
        when (preferenceType) {
            PreferenceType.DEVELOPMENT -> {
                developmentPreferencesDataSource.removeLong(key)
            }
            PreferenceType.SECURED -> {
                securedPreferencesDataSource.removeLong(key)
            }
            PreferenceType.STANDARD -> {
                standardPreferencesDataSource.removeLong(key)
            }
        }
    }

    override suspend fun removeString(
        preferenceType: PreferenceType,
        key: String
    ) {
        when (preferenceType) {
            PreferenceType.DEVELOPMENT -> {
                developmentPreferencesDataSource.removeString(key)
            }
            PreferenceType.SECURED -> {
                securedPreferencesDataSource.removeString(key)
            }
            PreferenceType.STANDARD -> {
                standardPreferencesDataSource.removeString(key)
            }
        }
    }

    override suspend fun removeAll(preferenceType: PreferenceType) {
        when (preferenceType) {
            PreferenceType.DEVELOPMENT -> {
                developmentPreferencesDataSource.removeAll()
            }
            PreferenceType.SECURED -> {
                securedPreferencesDataSource.removeAll()
            }
            PreferenceType.STANDARD -> {
                standardPreferencesDataSource.removeAll()
            }
        }
    }
}
