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

package dev.zitech.core.persistence.domain.usecase.database

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.domain.model.WorkError
import dev.zitech.core.common.domain.model.WorkSuccess
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class GetUserAccountsUseCaseTest {

    private val userAccountRepository = mockk<UserAccountRepository>()

    private lateinit var sut: GetUserAccountsUseCase

    @BeforeEach
    fun setup() {
        sut = GetUserAccountsUseCase(userAccountRepository)
    }

    @Test
    @DisplayName("WHEN there is no exception THEN return Success")
    fun success() = runBlocking {
        // Arrange
        coEvery { userAccountRepository.getUserAccounts() } returns flowOf(
            WorkSuccess(
                listOf(mockk(), mockk(), mockk()),
            ),
        )

        // Act & Assert
        sut().test {
            assertThat((awaitItem() as WorkSuccess).data).hasSize(3)
            awaitComplete()
        }
        coVerify { userAccountRepository.getUserAccounts() }
        confirmVerified(userAccountRepository)
    }

    @Test
    @DisplayName("WHEN there is exception THEN return Error")
    fun error() = runBlocking {
        // Arrange
        val error = Error.NullUserAccount
        coEvery { userAccountRepository.getUserAccounts() } returns flowOf(
            WorkError(
                error = error,
            ),
        )

        // Act & Assert
        sut().test {
            assertThat((awaitItem() as WorkError).error).isEqualTo(error)
            awaitComplete()
        }
        coVerify { userAccountRepository.getUserAccounts() }
        confirmVerified(userAccountRepository)
    }
}
