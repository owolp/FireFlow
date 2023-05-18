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

package dev.zitech.fireflow.common.domain.model.user

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class UserAccountTest {

    @Test
    fun `getRandomState should return a random string of specified length`() {
        val randomState = UserAccount.getRandomState()

        assertThat(randomState.length).isEqualTo(10)
    }

    @Test
    fun `UserAccount properties should have correct values`() {
        val authenticationType = UserAuthenticationType.Pat("")

        val userAccount = UserAccount(
            authenticationType = authenticationType,
            email = "example@example.com",
            fireflyId = "fireflyId",
            isCurrentUserAccount = true,
            role = "admin",
            serverAddress = "https://example.com",
            state = "state",
            type = "type",
            userId = 12345
        )

        assertThat(userAccount.authenticationType).isEqualTo(authenticationType)
        assertThat(userAccount.email).isEqualTo("example@example.com")
        assertThat(userAccount.fireflyId).isEqualTo("fireflyId")
        assertThat(userAccount.isCurrentUserAccount).isTrue()
        assertThat(userAccount.role).isEqualTo("admin")
        assertThat(userAccount.serverAddress).isEqualTo("https://example.com")
        assertThat(userAccount.state).isEqualTo("state")
        assertThat(userAccount.type).isEqualTo("type")
        assertThat(userAccount.userId).isEqualTo(12345L)
    }
}
