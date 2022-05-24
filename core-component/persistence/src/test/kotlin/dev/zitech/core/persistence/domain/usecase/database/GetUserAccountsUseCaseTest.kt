/*
 * Copyright (C) 2022 Zitech Ltd.
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

package dev.zitech.core.persistence.domain.usecase.database

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.framework.strings.FakeStringsProvider
import dev.zitech.core.persistence.framework.database.mapper.UserAccountMapper
import dev.zitech.core.persistence.data.repository.database.UserAccountRepositoryImpl
import dev.zitech.core.persistence.framework.database.dao.FakeUserAccountDao
import dev.zitech.core.persistence.domain.source.database.UserAccountDatabaseSource
import dev.zitech.core.persistence.framework.database.source.UserAccountDatabaseSourceImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class GetUserAccountsUseCaseTest {

    private val userAccountDatabaseSource: UserAccountDatabaseSource = UserAccountDatabaseSourceImpl(
        FakeUserAccountDao(),
        UserAccountMapper()
    )
    private val stringsProvider = FakeStringsProvider()
    private val mockedUserAccountDatabaseSource = mockk<UserAccountDatabaseSource>()

    @Test
    @DisplayName("WHEN there is no exception THEN return Success")
    fun success() = runBlocking {
        // Arrange
        userAccountDatabaseSource.saveUserAccount(false)
        userAccountDatabaseSource.saveUserAccount(false)
        userAccountDatabaseSource.saveUserAccount(true)
        val sut = GetUserAccountsUseCase(
            UserAccountRepositoryImpl(
                userAccountDatabaseSource,
                stringsProvider
            )
        )

        // Act
        val result = sut()

        // Assert
        assertThat((result as DataResult.Success).value).hasSize(3)
    }

    @Test
    @DisplayName("WHEN there is exception THEN return Error")
    fun error() = runBlocking {
        // Arrange
        val exception = DataFactory.createException()
        coEvery { mockedUserAccountDatabaseSource.getUserAccounts() } throws exception

        val sut = GetUserAccountsUseCase(
            UserAccountRepositoryImpl(
                mockedUserAccountDatabaseSource,
                stringsProvider
            )
        )

        // Act
        val result = sut()

        // Assert
        assertThat((result as DataResult.Error).cause).isEqualTo(exception)
    }
}