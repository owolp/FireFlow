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
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.persistence.domain.model.UserAccountBuilder
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class UpdateCurrentUserAccountUseCaseTest {

    private val userAccountRepository = mockk<UserAccountRepository>(relaxed = true)
    private val getCurrentUserAccountUseCase = mockk<GetCurrentUserAccountUseCase>()

    private lateinit var sut: UpdateCurrentUserAccountUseCase

    @BeforeEach
    fun setup() {
        sut = UpdateCurrentUserAccountUseCase(
            userAccountRepository,
            getCurrentUserAccountUseCase
        )
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(userAccountRepository)
    }

    @Test
    fun theme() = runTest {
        // Arrange
        val currentUserAccount = UserAccountBuilder()
            .setTheme(UserAccount.Theme.DARK)
            .build()

        coEvery {
            getCurrentUserAccountUseCase()
        } returns flowOf(DataResult.Success(currentUserAccount))

        val input: UserAccount.Theme = UserAccount.Theme.SYSTEM

        // Act
        sut(theme = input)

        // Assert
        coVerify { userAccountRepository.updateCurrentUserAccountTheme(input) }
    }

    @Test
    fun error() = runTest {
        // Arrange
        val error = DataResult.Error()

        coEvery {
            getCurrentUserAccountUseCase()
        } returns flowOf(error)

        val input: UserAccount.Theme = UserAccount.Theme.SYSTEM

        // Act
        val result = sut(theme = input)

        // Assert
        assertThat(result).isEqualTo(error)
    }
}
