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

package dev.zitech.onboarding.presentation.pat.viewmodel

import dev.zitech.core.common.presentation.architecture.MviIntent

internal data class PersonalAccessTokenChanged(val pat: String) : PatIntent
internal data class ServerAddressChanged(val serverAddress: String) : PatIntent
internal object BackClicked : PatIntent
internal object FatalErrorHandled : PatIntent
internal object LoginClicked : PatIntent
internal object NonFatalErrorHandled : PatIntent
internal object StepClosedHandled : PatIntent
internal object StepCompletedHandled : PatIntent

internal sealed interface PatIntent : MviIntent
