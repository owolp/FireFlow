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
import dev.zitech.core.persistence.domain.repository.preferences.GetPreferencesRepository
import dev.zitech.core.persistence.domain.source.preferences.PreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPreferencesRepositoryImpl @Inject constructor(
    private val developmentPreferencesDataSource: PreferencesDataSource,
    private val securedPreferencesDataSource: PreferencesDataSource,
    private val standardPreferencesDataSource: PreferencesDataSource
) : GetPreferencesRepository {

    override fun getBoolean(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: Boolean
    ): Flow<Boolean> = when (preferenceType) {
        PreferenceType.DEVELOPMENT -> {
            developmentPreferencesDataSource.getBoolean(key, defaultValue)
        }
        PreferenceType.SECURED -> {
            securedPreferencesDataSource.getBoolean(key, defaultValue)
        }
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.getBoolean(key, defaultValue)
        }
    }

    override fun getFloat(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: Float
    ): Flow<Float> = when (preferenceType) {
        PreferenceType.DEVELOPMENT -> {
            developmentPreferencesDataSource.getFloat(key, defaultValue)
        }
        PreferenceType.SECURED -> {
            securedPreferencesDataSource.getFloat(key, defaultValue)
        }
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.getFloat(key, defaultValue)
        }
    }

    override fun getInt(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: Int
    ): Flow<Int> = when (preferenceType) {
        PreferenceType.DEVELOPMENT -> {
            developmentPreferencesDataSource.getInt(key, defaultValue)
        }
        PreferenceType.SECURED -> {
            securedPreferencesDataSource.getInt(key, defaultValue)
        }
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.getInt(key, defaultValue)
        }
    }

    override fun getLong(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: Long
    ): Flow<Long> = when (preferenceType) {
        PreferenceType.DEVELOPMENT -> {
            developmentPreferencesDataSource.getLong(key, defaultValue)
        }
        PreferenceType.SECURED -> {
            securedPreferencesDataSource.getLong(key, defaultValue)
        }
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.getLong(key, defaultValue)
        }
    }

    override fun getString(
        preferenceType: PreferenceType,
        key: String,
        defaultValue: String?
    ): Flow<String?> = when (preferenceType) {
        PreferenceType.DEVELOPMENT -> {
            developmentPreferencesDataSource.getString(key, defaultValue)
        }
        PreferenceType.SECURED -> {
            securedPreferencesDataSource.getString(key, defaultValue)
        }
        PreferenceType.STANDARD -> {
            standardPreferencesDataSource.getString(key, defaultValue)
        }
    }
}
