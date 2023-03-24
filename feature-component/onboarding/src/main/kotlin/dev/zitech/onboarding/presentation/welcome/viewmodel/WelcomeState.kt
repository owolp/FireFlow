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

package dev.zitech.onboarding.presentation.welcome.viewmodel

import dev.zitech.core.common.domain.error.FireFlowError
import dev.zitech.core.common.presentation.architecture.MviState

internal data class WelcomeState(
    val demo: Boolean = false,
    val demoWarning: Boolean = false,
    val fatalError: FireFlowError? = null,
    val firefly: Boolean = false,
    val nonFatalError: FireFlowError? = null,
    val oauth: Boolean = false,
    val pat: Boolean = false,
    val quitApp: Boolean = false
) : MviState
