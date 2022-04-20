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

package dev.zitech.core.storage.data.database.repository

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.framework.strings.StringsProvider
import dev.zitech.core.storage.R
import dev.zitech.core.storage.data.database.mapper.UserAccountMapper
import dev.zitech.core.storage.domain.repository.UserAccountRepository
import dev.zitech.core.storage.framework.database.dao.FakeUserAccountDao
import dev.zitech.core.storage.framework.database.user.UserAccountDatabaseSource
import dev.zitech.core.storage.framework.database.user.UserAccountDatabaseSourceImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class UserAccountRepositoryImplTest {

    private val userAccountDatabaseSource: UserAccountDatabaseSource = UserAccountDatabaseSourceImpl(
        FakeUserAccountDao(),
        UserAccountMapper()
    )
    private val mockedUserAccountDatabaseSource = mockk<UserAccountDatabaseSource>()
    private val mockedStringsProvider = mockk<StringsProvider>()

    @Nested
    inner class GetUserAccounts {

        @Test
        @DisplayName("WHEN no exception THEN return Success")
        fun success() = runBlocking {
            // Arrange
            val sut: UserAccountRepository = UserAccountRepositoryImpl(
                userAccountDatabaseSource,
                mockedStringsProvider
            )

            userAccountDatabaseSource.saveUserAccount(false)
            userAccountDatabaseSource.saveUserAccount(false)
            userAccountDatabaseSource.saveUserAccount(true)

            // Act
            val result = sut.getUserAccounts()

            // Assert
            assertThat((result as DataResult.Success).value).hasSize(3)
        }

        @Test
        @DisplayName("WHEN exception THEN return Error")
        fun failure() = runBlocking {
            // Arrange
            val exception = DataFactory.createException()
            coEvery { mockedUserAccountDatabaseSource.getUserAccounts() } throws exception
            val sut: UserAccountRepository = UserAccountRepositoryImpl(
                mockedUserAccountDatabaseSource,
                mockedStringsProvider
            )

            // Act
            val result = sut.getUserAccounts()

            // Assert
            assertThat((result as DataResult.Error).cause).isEqualTo(exception)
        }
    }

    @Nested
    inner class GetCurrentUserAccount {

        @Nested
        inner class NoException {
            @Test
            @DisplayName("WHEN account is not null THEN return Success")
            fun accountNotNull() = runBlocking {
                // Arrange
                val sut: UserAccountRepository = UserAccountRepositoryImpl(
                    userAccountDatabaseSource,
                    mockedStringsProvider
                )

                userAccountDatabaseSource.saveUserAccount(true)

                // Act
                val result = sut.getCurrentUserAccount()

                // Assert
                assertThat((result as DataResult.Success).value.id).isEqualTo(-1)
                assertThat(result.value.isCurrentUserAccount).isTrue()
            }

            @Test
            @DisplayName("WHEN account is null THEN return Failure")
            fun accountNull() = runBlocking {
                // Arrange
                val message = DataFactory.createRandomString()
                every { mockedStringsProvider(R.string.error_message_current_user_null) } returns message

                val sut: UserAccountRepository = UserAccountRepositoryImpl(
                    userAccountDatabaseSource,
                    mockedStringsProvider
                )

                // Act
                val result = sut.getCurrentUserAccount()

                // Assert
                assertThat((result as DataResult.Error).message).isEqualTo(message)
            }
        }

        @Test
        @DisplayName("WHEN exception THEN return Failure")
        fun exception() = runBlocking {
            // Arrange
            val exception = DataFactory.createException()
            every { mockedStringsProvider(R.string.error_message_current_user_null) } throws exception

            val sut: UserAccountRepository = UserAccountRepositoryImpl(
                userAccountDatabaseSource,
                mockedStringsProvider
            )

            // Act
            val result = sut.getCurrentUserAccount()

            // Assert
            assertThat((result as DataResult.Error).cause).isEqualTo(exception)
        }
    }

    @Nested
    inner class SaveUserAccount {

        @Test
        @DisplayName("WHEN no exception THEN return Success")
        fun success() = runBlocking {
            // Arrange
            val sut: UserAccountRepository = UserAccountRepositoryImpl(
                userAccountDatabaseSource,
                mockedStringsProvider
            )

            // Act
            val result = sut.saveUserAccount(true)

            // Assert
            assertThat((result as DataResult.Success).value).isEqualTo(1)
        }

        @Test
        @DisplayName("WHEN exception THEN return Error")
        fun failure() = runBlocking {
            // Arrange
            val exception = DataFactory.createException()
            coEvery { mockedUserAccountDatabaseSource.saveUserAccount(true) } throws exception
            val sut: UserAccountRepository = UserAccountRepositoryImpl(
                mockedUserAccountDatabaseSource,
                mockedStringsProvider
            )

            // Act
            val result = sut.saveUserAccount(true)

            // Assert
            assertThat((result as DataResult.Error).cause).isEqualTo(exception)
        }
    }
}