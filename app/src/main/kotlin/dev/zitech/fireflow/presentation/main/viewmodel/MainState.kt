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

package dev.zitech.fireflow.presentation.main.viewmodel

import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.common.presentation.architecture.MviState

data class MainState(
    val databaseCleanCompleted: Boolean = false,
    val mandatoryStepsCompleted: Boolean = false,
    val theme: ApplicationTheme? = null
) : MviState {

    val splash: Boolean = !(databaseCleanCompleted && mandatoryStepsCompleted)
}
