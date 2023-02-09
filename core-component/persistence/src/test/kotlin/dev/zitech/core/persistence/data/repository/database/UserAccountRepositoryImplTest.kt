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

package dev.zitech.core.persistence.data.repository.database

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import dev.zitech.core.persistence.domain.source.database.UserAccountDatabaseSource
import dev.zitech.core.persistence.framework.database.dao.FakeUserAccountDao
import dev.zitech.core.persistence.framework.database.mapper.UserAccountMapper
import dev.zitech.core.persistence.framework.database.source.UserAccountDatabaseSourceImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class UserAccountRepositoryImplTest {

    private val userAccountDatabaseSource: UserAccountDatabaseSource = UserAccountDatabaseSourceImpl(
        FakeUserAccountDao(),
        UserAccountMapper()
    )
    private val mockedUserAccountDatabaseSource = mockk<UserAccountDatabaseSource>()

    @Nested
    inner class GetUserAccounts {

        @Test
        @DisplayName("WHEN no exception THEN return Success")
        fun success() = runBlocking {
            // Arrange
            val sut: UserAccountRepository = UserAccountRepositoryImpl(
                userAccountDatabaseSource
            )

            userAccountDatabaseSource.saveUserAccount(false)
            userAccountDatabaseSource.saveUserAccount(false)
            userAccountDatabaseSource.saveUserAccount(true)

            // Act & Assert
            sut.getUserAccounts().test {
                assertThat((awaitItem() as DataResult.Success).value).hasSize(3)
                awaitComplete()
            }
        }
    }

    @Nested
    inner class GetCurrentUserAccount {

        @Test
        @DisplayName("WHEN account is not null THEN return Success")
        fun success() = runBlocking {
            // Arrange
            val sut: UserAccountRepository = UserAccountRepositoryImpl(
                userAccountDatabaseSource
            )

            userAccountDatabaseSource.saveUserAccount(true)

            // Act & Assert
            sut.getCurrentUserAccount().test {
                with((awaitItem() as DataResult.Success).value) {
                    assertThat(userId).isEqualTo(-1)
                    assertThat(isCurrentUserAccount).isTrue()
                }
                awaitComplete()
            }
        }
    }

    @Nested
    inner class SaveUserAccount {

        @Test
        @DisplayName("WHEN no exception THEN return Success")
        fun success() = runBlocking {
            // Arrange
            val sut: UserAccountRepository = UserAccountRepositoryImpl(
                userAccountDatabaseSource
            )

            // Act
            val result = sut.saveUserAccount(true)

            // Assert
            assertThat((result as DataResult.Success).value).isEqualTo(1)
        }

        @Test
        @DisplayName("WHEN exception THEN return Error")
        fun error() = runBlocking {
            // Arrange
            val exception = DataFactory.createException()
            coEvery { mockedUserAccountDatabaseSource.saveUserAccount(true) } throws exception
            val sut: UserAccountRepository = UserAccountRepositoryImpl(
                mockedUserAccountDatabaseSource
            )

            // Act
            val result = sut.saveUserAccount(true)

            // Assert
            assertThat((result as DataResult.Error).cause).isEqualTo(exception)
        }
    }
}