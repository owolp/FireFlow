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
import dev.zitech.core.storage.domain.model.PreferenceType
import dev.zitech.core.storage.framework.preference.PreferencesDataSource
import dev.zitech.core.storage.framework.preference.SecuredPreferencesDataSource
import dev.zitech.core.storage.framework.preference.StandardPreferencesDataSource

internal object PreferencesFactory {

    private const val SECURED_PREFERENCES_NAME = "secured_preferences"
    private const val STANDARD_PREFERENCES_NAME = "standard_preferences"

    fun createsPreferences(context: Context, type: PreferenceType): PreferencesDataSource =
        when (type) {
            PreferenceType.SECURED -> {
                SecuredPreferencesDataSource(
                    context = context,
                    fileName = SECURED_PREFERENCES_NAME
                )
            }
            PreferenceType.STANDARD -> {
                StandardPreferencesDataSource(
                    context = context,
                    fileName = STANDARD_PREFERENCES_NAME
                )
            }
        }
}
