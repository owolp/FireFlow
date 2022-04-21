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

package dev.zitech.core.storage.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.framework.strings.FakeStringsProvider
import dev.zitech.core.storage.data.database.mapper.UserAccountMapper
import dev.zitech.core.storage.data.database.repository.UserAccountRepositoryImpl
import dev.zitech.core.storage.framework.database.dao.FakeUserAccountDao
import dev.zitech.core.storage.framework.database.user.UserAccountDatabaseSource
import dev.zitech.core.storage.framework.database.user.UserAccountDatabaseSourceImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class SaveUserAccountUseCaseTest {

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
        val isCurrentUserAccount = DataFactory.createRandomBoolean()
        val sut = SaveUserAccountUseCase(
            UserAccountRepositoryImpl(
                userAccountDatabaseSource,
                stringsProvider
            )
        )

        // Act
        val result = sut(isCurrentUserAccount)

        // Assert
        assertThat((result as DataResult.Success).value).isEqualTo(1)
    }

    @Test
    @DisplayName("WHEN there is exception THEN return Error")
    fun error() = runBlocking {
        // Arrange
        val isCurrentUserAccount = DataFactory.createRandomBoolean()
        val exception = DataFactory.createException()
        coEvery { mockedUserAccountDatabaseSource.saveUserAccount(isCurrentUserAccount) } throws exception

        val sut = SaveUserAccountUseCase(
            UserAccountRepositoryImpl(
                mockedUserAccountDatabaseSource,
                stringsProvider
            )
        )

        // Act
        val result = sut(isCurrentUserAccount)

        // Assert
        assertThat((result as DataResult.Error).cause).isEqualTo(exception)
    }
}