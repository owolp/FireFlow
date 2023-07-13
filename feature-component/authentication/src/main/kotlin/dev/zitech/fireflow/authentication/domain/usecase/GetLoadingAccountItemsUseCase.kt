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

package dev.zitech.fireflow.authentication.domain.usecase

import dev.zitech.fireflow.authentication.presentation.accounts.model.AccountItem
import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.core.datafactory.DataFactory
import javax.inject.Inject

/**
 * Use case class for getting loading account items.
 *
 * This class provides a list of fake [AccountItem] objects that represent loading state items.
 * It can be used to populate the UI while the actual account items are being fetched or loaded.
 */
internal class GetLoadingAccountItemsUseCase @Inject constructor() {

    /**
     * Retrieves the loading account items.
     *
     * @return A list of fake [AccountItem] objects.
     */
    operator fun invoke(): List<AccountItem> = listOf(
        createFakeAccountItem(),
        createFakeAccountItem(),
        createFakeAccountItem()
    )

    private fun createFakeAccountItem(): AccountItem {
        val fakeInfo = DataFactory.createRandomString(length = FAKE_INFO_LENGTH)
        return AccountItem(
            menuItems = emptyList(),
            more = false,
            user = User.Remote(
                id = 0L,
                isCurrentUser = false,
                connectivityNotification = false,
                serverAddress = fakeInfo,
                email = fakeInfo
            )
        )
    }

    private companion object {
        const val FAKE_INFO_LENGTH = 10
    }
}
