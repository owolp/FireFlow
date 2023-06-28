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

package dev.zitech.fireflow.authentication.presentation.accounts.viewmodel

import dev.zitech.fireflow.common.presentation.architecture.MviIntent

internal sealed interface AccountsIntent : MviIntent
internal data class BackClicked(val isBackNavigationSupported: Boolean) : AccountsIntent
internal data class MoreItemClicked(
    val identification: String,
    val menuItemId: Int,
    val userId: Long
) : AccountsIntent
internal data class MoreDismissed(val userId: Long) : AccountsIntent
internal data class MoreClicked(val userId: Long) : AccountsIntent
internal object SwitchToAccountClicked : AccountsIntent
internal object QuitHandled : AccountsIntent
internal object CloseHandled : AccountsIntent
internal object HomeHandled : AccountsIntent
internal object NonFatalErrorHandled : AccountsIntent
internal object FatalErrorHandled : AccountsIntent
internal data class ConfirmRemoveAccountClicked(val userId: Long) : AccountsIntent
internal object ConfirmRemoveAccountDismissed : AccountsIntent
internal object OnNewAccountClicked : AccountsIntent
internal object NewAccountHandled : AccountsIntent
