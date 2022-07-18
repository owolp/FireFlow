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
import dev.zitech.core.persistence.domain.repository.preferences.SavePreferencesRepository
import dev.zitech.core.persistence.domain.source.preferences.PreferencesDataSource
import javax.inject.Inject

class SavePreferencesRepositoryImpl @Inject constructor(
    private val developmentPreferencesDataSource: PreferencesDataSource,
    private val securedPreferencesDataSource: PreferencesDataSource,
    private val standardPreferencesDataSource: PreferencesDataSource
) : SavePreferencesRepository {

    override suspend fun saveBoolean(
        preferenceType: PreferenceType,
        key: String,
        value: Boolean
    ) {
        when (preferenceType) {
            PreferenceType.DEVELOPMENT -> {
                developmentPreferencesDataSource.saveBoolean(key, value)
            }
            PreferenceType.SECURED -> {
                securedPreferencesDataSource.saveBoolean(key, value)
            }
            PreferenceType.STANDARD -> {
                standardPreferencesDataSource.saveBoolean(key, value)
            }
        }
    }

    override suspend fun saveFloat(
        preferenceType: PreferenceType,
        key: String,
        value: Float
    ) {
        when (preferenceType) {
            PreferenceType.DEVELOPMENT -> {
                developmentPreferencesDataSource.saveFloat(key, value)
            }
            PreferenceType.SECURED -> {
                securedPreferencesDataSource.saveFloat(key, value)
            }
            PreferenceType.STANDARD -> {
                standardPreferencesDataSource.saveFloat(key, value)
            }
        }
    }

    override suspend fun saveInt(
        preferenceType: PreferenceType,
        key: String,
        value: Int
    ) {
        when (preferenceType) {
            PreferenceType.DEVELOPMENT -> {
                developmentPreferencesDataSource.saveInt(key, value)
            }
            PreferenceType.SECURED -> {
                securedPreferencesDataSource.saveInt(key, value)
            }
            PreferenceType.STANDARD -> {
                standardPreferencesDataSource.saveInt(key, value)
            }
        }
    }

    override suspend fun saveLong(
        preferenceType: PreferenceType,
        key: String,
        value: Long
    ) {
        when (preferenceType) {
            PreferenceType.DEVELOPMENT -> {
                developmentPreferencesDataSource.saveLong(key, value)
            }
            PreferenceType.SECURED -> {
                securedPreferencesDataSource.saveLong(key, value)
            }
            PreferenceType.STANDARD -> {
                standardPreferencesDataSource.saveLong(key, value)
            }
        }
    }

    override suspend fun saveString(
        preferenceType: PreferenceType,
        key: String,
        value: String
    ) {
        when (preferenceType) {
            PreferenceType.DEVELOPMENT -> {
                developmentPreferencesDataSource.saveString(key, value)
            }
            PreferenceType.SECURED -> {
                securedPreferencesDataSource.saveString(key, value)
            }
            PreferenceType.STANDARD -> {
                standardPreferencesDataSource.saveString(key, value)
            }
        }
    }
}
