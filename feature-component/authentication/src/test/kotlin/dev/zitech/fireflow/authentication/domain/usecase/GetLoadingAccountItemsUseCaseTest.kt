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

import com.google.common.truth.Truth.assertThat
import dev.zitech.fireflow.common.domain.model.user.User
import org.junit.Test

class GetLoadingAccountItemsUseCaseTest {

    private val sut = GetLoadingAccountItemsUseCase()

    @Test
    fun `invoke should return list of fake account items`() {
        // Act
        val accountItems = sut.invoke()

        // Assert
        assertThat(accountItems).hasSize(3)

        accountItems.forEach { accountItem ->

            assertThat(accountItem.menuItems).isEmpty()
            assertThat(accountItem.more).isFalse()
            assertThat(accountItem.user).isNotNull()
            assertThat(accountItem.user.id).isEqualTo(0L)
            assertThat(accountItem.user.isCurrentUser).isFalse()
            with(accountItem.user as User.Remote) {
                assertThat(connectivityNotification).isFalse()
                assertThat(serverAddress).isNotNull()
                assertThat(email).isNotNull()
            }
        }
    }
}
