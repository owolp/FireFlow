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

package dev.zitech.core.persistence.framework.preference.mapper

import dev.zitech.core.common.data.mapper.Mapper
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.domain.model.ApplicationTheme
import javax.inject.Inject

class IntToApplicationThemeMapper @Inject constructor() : Mapper<Int, ApplicationTheme> {

    companion object {
        const val TAG = "IntToApplicationThemeMapper"
    }

    override fun invoke(input: Int): ApplicationTheme =
        when (input) {
            ApplicationTheme.SYSTEM.id -> ApplicationTheme.SYSTEM
            ApplicationTheme.DARK.id -> ApplicationTheme.DARK
            ApplicationTheme.LIGHT.id -> ApplicationTheme.LIGHT
            else -> {
                Logger.e(TAG, "Theme $input not supported!")
                ApplicationTheme.SYSTEM
            }
        }
}
