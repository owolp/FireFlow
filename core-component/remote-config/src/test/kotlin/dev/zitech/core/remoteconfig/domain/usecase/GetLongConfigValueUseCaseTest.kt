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

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.remoteconfig.data.repository.ConfigRepositoryImpl
import dev.zitech.core.remoteconfig.domain.model.LongConfig
import dev.zitech.core.remoteconfig.framework.source.FakeConfigProviderSource
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class GetLongConfigValueUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        // Arrange
        val expectedResult = DataFactory.createRandomLong()
        val fakeConfigProviderSource = FakeConfigProviderSource()
        fakeConfigProviderSource.longResult = DataResult.Success(expectedResult)
        val configRepository = ConfigRepositoryImpl(fakeConfigProviderSource)
        val sut = GetLongConfigValueUseCase(configRepository)

        // Act
        val result = sut(mockkClass(LongConfig::class))

        // Assert
        assertThat(result).isEqualTo(expectedResult)
    }
}