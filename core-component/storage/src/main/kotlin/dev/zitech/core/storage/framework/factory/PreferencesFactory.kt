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

package dev.zitech.core.storage.framework.factory

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences as DataStorePreferences
import androidx.datastore.preferences.preferencesDataStore
import dev.zitech.core.storage.domain.model.PreferenceType
import dev.zitech.core.storage.framework.preference.PreferencesDataSource
import dev.zitech.core.storage.framework.preference.StandardPreferencesDataSource

internal object PreferencesFactory {

    private const val STANDARD_PREFERENCES_NAME = "standard_preferences"

    private val Context.dataStore: DataStore<DataStorePreferences> by preferencesDataStore(
        name = STANDARD_PREFERENCES_NAME
    )

    fun createsPreferences(context: Context, type: PreferenceType): PreferencesDataSource =
        when (type) {
            PreferenceType.STANDARD -> {
                StandardPreferencesDataSource(context.dataStore)
            }
        }
}
