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
import dev.zitech.core.persistence.domain.model.UserAccountBuilder
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class GetCurrentUserAccountUseCaseTest {

    @Test
    @DisplayName("WHEN there is current account THEN return Success")
    fun success() = runTest {
        // Arrange
        val userAccountRepository = mockk<UserAccountRepository>()
        val sut = GetCurrentUserAccountUseCase(userAccountRepository)

        val account = UserAccountBuilder().build()
        every {
            userAccountRepository.getCurrentUserAccount()
        } returns flowOf(WorkSuccess(account))

        // Act & Assert
        sut().test {
            assertThat((awaitItem() as WorkSuccess).data).isEqualTo(account)
            awaitComplete()
        }
        coVerify { userAccountRepository.getCurrentUserAccount() }
        confirmVerified(userAccountRepository)
    }

    @Test
    @DisplayName("WHEN there is no current account THEN return Error")
    fun error() = runTest {
        // Arrange
        val userAccountRepository = mockk<UserAccountRepository>()
        val sut = GetCurrentUserAccountUseCase(userAccountRepository)

        val error = Error.NullUserAccount
        every {
            userAccountRepository.getCurrentUserAccount()
        } returns flowOf(WorkError(error))

        // Act & Assert
        sut().test {
            assertThat(awaitItem()).isInstanceOf(WorkError::class.java)
            awaitComplete()
        }
        coVerify { userAccountRepository.getCurrentUserAccount() }
        confirmVerified(userAccountRepository)
    }
}