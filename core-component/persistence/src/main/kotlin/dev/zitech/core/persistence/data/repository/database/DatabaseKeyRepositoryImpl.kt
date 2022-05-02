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

package dev.zitech.core.persistence.data.repository.database

import dev.zitech.core.common.DataFactory
import dev.zitech.core.persistence.domain.model.database.DatabaseKey
import dev.zitech.core.persistence.domain.model.preferences.PreferenceType
import dev.zitech.core.persistence.domain.repository.database.DatabaseKeyRepository
import dev.zitech.core.persistence.domain.repository.preferences.ContainsPreferencesRepository
import dev.zitech.core.persistence.domain.repository.preferences.GetPreferencesRepository
import dev.zitech.core.persistence.domain.repository.preferences.SavePreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class DatabaseKeyRepositoryImpl @Inject constructor(
    private val containsPreferencesRepository: ContainsPreferencesRepository,
    private val getPreferencesRepository: GetPreferencesRepository,
    private val savePreferencesRepository: SavePreferencesRepository
) : DatabaseKeyRepository {

    companion object {
        private const val ENCRYPTION_LENGTH = 20
        private const val KEY_SECURED_STORAGE_SECURED_DATABASE = "secured_database"
    }

    override suspend fun getDatabaseKey(): DatabaseKey {
        if (!containsPreferencesRepository.containsString(
                PreferenceType.SECURED, KEY_SECURED_STORAGE_SECURED_DATABASE
            ).first()
        ) {
            savePreferencesRepository.saveString(
                PreferenceType.SECURED,
                KEY_SECURED_STORAGE_SECURED_DATABASE,
                DataFactory.createRandomString(ENCRYPTION_LENGTH)
            )
        }

        val databaseKey: CharArray = getPreferencesRepository.getString(
            PreferenceType.SECURED,
            KEY_SECURED_STORAGE_SECURED_DATABASE,
            ""
        ).first()?.toCharArray()!!

        return DatabaseKey(databaseKey)
    }
}
