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

package dev.zitech.core.remoteconfig.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.remoteconfig.framework.configurator.FakeRemoteConfigurator
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class InitializeRemoteConfiguratorUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        // Arrange
        val fakeRemoteConfigurator = FakeRemoteConfigurator().apply {
            initResult = DataResult.Success(Unit)
        }
        val sut = InitializeRemoteConfiguratorUseCase(fakeRemoteConfigurator)

        // Act & Assert
        sut().test {
            assertThat(awaitItem()).isEqualTo(DataResult.Success(Unit))
            awaitComplete()
        }
    }
}