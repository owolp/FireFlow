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
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class GetCurrentUserAccountUseCaseTest {

    private val userAccountRepository = mockk<UserAccountRepository>()

    private lateinit var sut: GetCurrentUserAccountUseCase

    @BeforeEach
    fun setup() {
        sut = GetCurrentUserAccountUseCase(userAccountRepository)
    }

    @Test
    @DisplayName("WHEN there is current account THEN return Success")
    fun success() = runBlocking {
        // Arrange
        val id = DataFactory.createRandomLong()
        val isCurrentUserAccount = DataFactory.createRandomBoolean()
        val userAccount = mockkClass(UserAccount::class)
        every { userAccount.id } returns id
        every { userAccount.isCurrentUserAccount } returns isCurrentUserAccount
        coEvery { userAccountRepository.getCurrentUserAccount() } returns DataResult.Success(userAccount)

        // Act
        val result = sut()

        // Assert
        assertThat((result as DataResult.Success).value.id).isEqualTo(id)
        assertThat(result.value.isCurrentUserAccount).isEqualTo(isCurrentUserAccount)
        coVerify { userAccountRepository.getCurrentUserAccount() }
        confirmVerified(userAccountRepository)
    }

    @Test
    @DisplayName("WHEN there is no current account THEN return Error")
    fun error() = runBlocking {
        // Arrange
        val message = DataFactory.createRandomString()
        coEvery { userAccountRepository.getCurrentUserAccount() } returns DataResult.Error(message)

        // Act
        val result = sut()

        // Assert
        assertThat((result as DataResult.Error).message).isEqualTo(message)
        coVerify { userAccountRepository.getCurrentUserAccount() }
        confirmVerified(userAccountRepository)
    }
}