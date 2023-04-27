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
import javax.inject.Inject

class ApplicationThemeToIntMapper @Inject constructor() : Mapper<ApplicationTheme, Int> {

    override fun invoke(input: ApplicationTheme): Int =
        when (input) {
            ApplicationTheme.SYSTEM -> ApplicationTheme.SYSTEM.id
            ApplicationTheme.DARK -> ApplicationTheme.DARK.id
            ApplicationTheme.LIGHT -> ApplicationTheme.LIGHT.id
        }
}
