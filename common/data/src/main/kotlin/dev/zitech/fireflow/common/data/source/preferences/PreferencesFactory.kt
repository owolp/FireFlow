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

package dev.zitech.fireflow.common.data.source.preferences

import android.content.Context
import dev.zitech.fireflow.common.domain.model.preferences.PreferenceType
import dev.zitech.fireflow.core.dispatcher.AppDispatchers

const val DEVELOPMENT_PREFERENCES_NAME = "development_preferences"
const val SECURED_PREFERENCES_NAME = "secured_preferences"
const val STANDARD_PREFERENCES_NAME = "standard_preferences"

internal object PreferencesFactory {

    fun createsPreferences(
        appDispatchers: AppDispatchers,
        context: Context,
        type: PreferenceType
    ): PreferencesDataSource =
        when (type) {
            PreferenceType.DEVELOPMENT -> {
                StandardPreferencesDataSource(
                    appDispatchers = appDispatchers,
                    fileName = DEVELOPMENT_PREFERENCES_NAME,
                    context = context
                )
            }

            PreferenceType.SECURED -> {
                SecuredPreferencesDataSource(
                    appDispatchers = appDispatchers,
                    fileName = SECURED_PREFERENCES_NAME,
                    context = context
                )
            }

            PreferenceType.STANDARD -> {
                StandardPreferencesDataSource(
                    appDispatchers = appDispatchers,
                    fileName = STANDARD_PREFERENCES_NAME,
                    context = context
                )
            }
        }
}
