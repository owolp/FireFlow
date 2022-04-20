/*
 * Copyright (C) 2022 Zitech
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

package dev.zitech.core.storage.framework.database.user

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.storage.data.database.mapper.UserAccountMapper
import dev.zitech.core.storage.framework.database.dao.FakeUserAccountDao
import dev.zitech.core.storage.framework.database.entity.UserAccountEntityFactory
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class UserAccountDatabaseSourceImplTest {

    private val userAccountDao = FakeUserAccountDao()
    private val userAccountMapper = UserAccountMapper()

    private lateinit var sut: UserAccountDatabaseSource

    @BeforeEach
    fun setup() {
        sut = UserAccountDatabaseSourceImpl(
            userAccountDao,
            userAccountMapper
        )
    }

    @Nested
    inner class GetUserAccounts {

        @Test
        @DisplayName("WHEN no user accounts THEN return empty list")
        fun emptyList() = runBlocking {
            // Act
            val result = sut.getUserAccounts()

            // Assert
            assertThat(result).isEmpty()
        }

        @Test
        @DisplayName("WHEN user accounts THEN return correct list")
        fun notEmptyList() = runBlocking {
            // Arrange
            val entity1 = UserAccountEntityFactory.createUserAccountEntity(
                1L, true
            )
            val entity2 = UserAccountEntityFactory.createUserAccountEntity(
                2L, false
            )
            val entity3 = UserAccountEntityFactory.createUserAccountEntity(
                3L, false
            )
            userAccountDao.saveUserAccount(entity1)
            userAccountDao.saveUserAccount(entity2)
            userAccountDao.saveUserAccount(entity3)

            // Act
            val result = sut.getUserAccounts()

            // Assert
            assertThat(result).hasSize(3)
            assertThat(result[0].id).isEqualTo(1)
            assertThat(result[0].isCurrentUserAccount).isTrue()
            assertThat(result[1].id).isEqualTo(2)
            assertThat(result[1].isCurrentUserAccount).isFalse()
            assertThat(result[2].id).isEqualTo(3)
            assertThat(result[2].isCurrentUserAccount).isFalse()
        }
    }

    @Nested
    inner class GetCurrentUserAccount {

        @Test
        @DisplayName("WHEN no current user account THEN return null")
        fun noUserAccount() = runBlocking {
            // Act
            val result = sut.getCurrentUserAccount()

            // Assert
            assertThat(result).isNull()
        }

        @Test
        @DisplayName("WHEN there is a current user accounts THEN return it")
        fun userAccount() = runBlocking {
            // Arrange
            val entity1 = UserAccountEntityFactory.createUserAccountEntity(
                1L, true
            )
            userAccountDao.saveUserAccount(entity1)

            // Act
            val result = sut.getCurrentUserAccount()

            // Assert
            assertThat(result?.id).isEqualTo(1)
            assertThat(result?.isCurrentUserAccount).isTrue()
        }
    }

    @Nested
    inner class SaveUserAccount {

        @Test
        @DisplayName("GIVEN true WHEN no current account THEN return success")
        fun noCurrentUserAccount() = runBlocking {
            // Assert
            assertThat(userAccountDao.containsCurrentUserAccountEntity()).isFalse()

            // Act
            val result = sut.saveUserAccount(true)

            // Assert
            assertThat(result).isEqualTo(1)
            assertThat(userAccountDao.containsCurrentUserAccountEntity()).isTrue()
        }

        @Test
        @DisplayName("GIVEN true WHEN current account THEN return success")
        fun availableCurrentUserAccount() = runBlocking {
            // Arrange
            userAccountDao.saveUserAccount(
                UserAccountEntityFactory.createUserAccountEntity(isCurrentUserAccount = true)
            )

            // Assert
            assertThat(userAccountDao.containsCurrentUserAccountEntity()).isTrue()

            // Act
            val result = sut.saveUserAccount(true)

            // Assert
            assertThat(result).isEqualTo(1)
            assertThat(userAccountDao.containsCurrentUserAccountEntity()).isTrue()
            assertThat(userAccountDao.getUserAccounts()).hasSize(1)
        }
    }
}
