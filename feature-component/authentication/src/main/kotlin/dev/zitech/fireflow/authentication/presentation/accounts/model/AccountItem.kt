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

package dev.zitech.fireflow.authentication.presentation.accounts.model

import androidx.annotation.StringRes
import dev.zitech.fireflow.authentication.R
import dev.zitech.fireflow.common.domain.model.user.User

/**
 * Represents an account item displayed in a UI component.
 *
 * @property menuItems The menu items available in the account item dropdown menu.
 * @property more Indicates whether to show the "more" dropdown menu.
 * @property user The user associated with the account item.
 */
data class AccountItem(
    val menuItems: List<MenuItem>,
    val more: Boolean,
    val user: User
) {
    /**
     * Sealed class representing a menu item within the account item dropdown menu.
     *
     * @property id The ID of the menu item.
     * @property resId The resource ID associated with the menu item.
     */
    sealed class MenuItem(val id: Int, @StringRes val resId: Int) {
        object SwitchToAccount : MenuItem(1, R.string.accounts_menu_item_switch_to_account)
        object RemoveAccount : MenuItem(2, R.string.accounts_menu_item_remove_account)
    }
}
