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

package dev.zitech.core.persistence.framework.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.zitech.core.persistence.framework.database.FireFlowDatabase
import dev.zitech.core.persistence.framework.database.entity.UserAccountEntityFactory
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class UserAccountDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: FireFlowDatabase
    private lateinit var sut: UserAccountDao

    @BeforeEach
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FireFlowDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        sut = database.userAccountDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }

    @Test
    fun getUserAccounts() = runBlocking {
        // Arrange
        val userAccountEntity1 = UserAccountEntityFactory.createUserAccountEntity()
        val userAccountEntity2 = UserAccountEntityFactory.createUserAccountEntity()
        val userAccountEntity3 = UserAccountEntityFactory.createUserAccountEntity()
        sut.saveUserAccount(userAccountEntity1)
        sut.saveUserAccount(userAccountEntity2)
        sut.saveUserAccount(userAccountEntity3)

        // Act
        val userAccounts = sut.getUserAccounts()

        // Assert
        assertThat(userAccounts).hasSize(3)
        assertThat(userAccounts).contains(userAccountEntity1)
        assertThat(userAccounts).contains(userAccountEntity2)
        assertThat(userAccounts).contains(userAccountEntity3)
    }

    @Nested
    inner class GetCurrentUserAccountFlow {

        @Test
        @DisplayName("GIVEN there is 1 current user account THEN return it")
        fun oneCurrentUserAccount() = runBlocking {
            // Arrange
            val userAccountEntity = UserAccountEntityFactory.createUserAccountEntity(
                isCurrentUserAccount = true
            )
            sut.saveUserAccount(userAccountEntity)

            // Act
            val result = sut.getCurrentUserAccount()

            // Act & Assert
            sut.getCurrentUserAccount().test {
                assertThat(awaitItem()).isEqualTo(userAccountEntity)
            }
        }

        @Test
        @DisplayName("GIVEN there are 2 current user account THEN return bigger id")
        fun multipleCurrentUserAccounts() = runBlocking {
            // Arrange
            val userAccountEntity1 = UserAccountEntityFactory.createUserAccountEntity(
                id = 1,
                isCurrentUserAccount = true
            )
            val userAccountEntity2 = UserAccountEntityFactory.createUserAccountEntity(
                id = 2,
                isCurrentUserAccount = true
            )
            val userAccountEntity3 = UserAccountEntityFactory.createUserAccountEntity(
                id = 3,
                isCurrentUserAccount = true
            )
            sut.saveUserAccount(userAccountEntity1)
            sut.saveUserAccount(userAccountEntity3)
            sut.saveUserAccount(userAccountEntity2)

            // Act & Assert
            sut.getCurrentUserAccount().test {
                assertThat(awaitItem()).isEqualTo(userAccountEntity3)
            }
        }
    }

    @Nested
    inner class GetCurrentUserAccount {

        @Test
        @DisplayName("GIVEN there is no current user account THEN return null")
        fun noCurrentUserAccount() = runBlocking {
            // Act
            val result = sut.getCurrentUserAccount()

            // Assert
            assertThat(result).isNull()
        }

        @Test
        @DisplayName("GIVEN there is 1 current user account THEN return it")
        fun oneCurrentUserAccount() = runBlocking {
            // Arrange
            val userAccountEntity = UserAccountEntityFactory.createUserAccountEntity(
                isCurrentUserAccount = true
            )
            sut.saveUserAccount(userAccountEntity)

            // Act
            val result = sut.getCurrentUserAccount()

            // Assert
            assertThat(result).isEqualTo(userAccountEntity)
        }

        @Test
        @DisplayName("GIVEN there are 2 current user account THEN return bigger id")
        fun multipleCurrentUserAccounts() = runBlocking {
            // Arrange
            val userAccountEntity1 = UserAccountEntityFactory.createUserAccountEntity(
                id = 1,
                isCurrentUserAccount = true
            )
            val userAccountEntity2 = UserAccountEntityFactory.createUserAccountEntity(
                id = 2,
                isCurrentUserAccount = true
            )
            val userAccountEntity3 = UserAccountEntityFactory.createUserAccountEntity(
                id = 3,
                isCurrentUserAccount = true
            )
            sut.saveUserAccount(userAccountEntity1)
            sut.saveUserAccount(userAccountEntity3)
            sut.saveUserAccount(userAccountEntity2)

            // Act
            val result = sut.getCurrentUserAccount()

            // Assert
            assertThat(result).isEqualTo(userAccountEntity3)
        }
    }

    @Nested
    inner class SaveUserAccount {

        @Test
        @DisplayName("GIVEN no same user THEN success")
        fun noSameUserAccount() = runBlocking {
            // Arrange
            val userAccountEntity = UserAccountEntityFactory.createUserAccountEntity()
            sut.saveUserAccount(userAccountEntity)

            // Act
            val userAccounts = sut.getUserAccounts()

            // Assert
            assertThat(userAccounts).hasSize(1)
            assertThat(userAccounts).contains(userAccountEntity)
        }

        @Test
        @DisplayName("GIVEN same user THEN replace and success")
        fun sameUserAccount() = runBlocking {
            // Arrange
            val userAccountEntity1 = UserAccountEntityFactory.createUserAccountEntity(id = 1)
            val userAccountEntity2 = UserAccountEntityFactory.createUserAccountEntity(id = 1)
            sut.saveUserAccount(userAccountEntity1)
            sut.saveUserAccount(userAccountEntity2)

            // Act
            val userAccounts = sut.getUserAccounts()

            // Assert
            assertThat(userAccounts).hasSize(1)
            assertThat(userAccounts).contains(userAccountEntity1)
        }
    }

    @Test
    fun removeCurrentUserAccount() = runBlocking {
        // Arrange
        val userAccountEntity1 = UserAccountEntityFactory.createUserAccountEntity(
            isCurrentUserAccount = true
        )
        sut.saveUserAccount(userAccountEntity1)

        // Act
        sut.removeCurrentUserAccount()
        val currentUserAccount = sut.getCurrentUserAccount()

        // Assert
        assertThat(currentUserAccount).isNull()
    }
}
