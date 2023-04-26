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

package dev.zitech.fireflow.common.domain.mapper.application

import dev.zitech.fireflow.common.domain.mapper.Mapper
import dev.zitech.fireflow.common.domain.model.application.ApplicationTheme
import dev.zitech.fireflow.core.logger.Logger
import javax.inject.Inject

class IntToApplicationThemeMapper @Inject constructor() : Mapper<Int, ApplicationTheme> {

    private val tag = Logger.tag(this::class.java)

    override fun invoke(input: Int): ApplicationTheme =
        when (input) {
            ApplicationTheme.SYSTEM.id -> ApplicationTheme.SYSTEM
            ApplicationTheme.DARK.id -> ApplicationTheme.DARK
            ApplicationTheme.LIGHT.id -> ApplicationTheme.LIGHT
            else -> {
                Logger.e(tag, "Theme $input not supported!")
                ApplicationTheme.SYSTEM
            }
        }
}
