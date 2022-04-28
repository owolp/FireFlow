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

package dev.zitech.core.remoteconfig.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.remoteconfig.domain.model.BooleanConfig
import dev.zitech.core.remoteconfig.domain.model.DoubleConfig
import dev.zitech.core.remoteconfig.domain.model.LongConfig
import dev.zitech.core.remoteconfig.domain.model.StringConfig
import dev.zitech.core.remoteconfig.domain.repository.ConfigRepository
import dev.zitech.core.remoteconfig.framework.source.FakeConfigProviderSource
import io.mockk.mockkClass
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class ConfigRepositoryImplTest {

    private val fakeConfigProviderSource = FakeConfigProviderSource()

    private lateinit var sut: ConfigRepository

    @Before
    fun setup() {
        sut = ConfigRepositoryImpl(
            fakeConfigProviderSource
        )
    }

    @Test
    fun init() = runBlocking {
        // Arrange
        fakeConfigProviderSource.initResult = DataResult.Success(Unit)

        // Act & Assert
        sut.init().test {
            assertThat(awaitItem()).isEqualTo(DataResult.Success(Unit))
            awaitComplete()
        }
    }

    @Test
    fun getBooleanConfigs() {
        // Act
        val result = sut.getBooleanConfigs()

        // Assert
        assertThat(result).hasSize(BooleanConfig.values().size)
    }

    @Test
    fun getDoubleConfigs() {
        // Act
        val result = sut.getDoubleConfigs()

        // Assert
        assertThat(result).hasSize(DoubleConfig.values().size)
    }

    @Test
    fun getLongConfigs() {
        // Act
        val result = sut.getLongConfigs()

        // Assert
        assertThat(result).hasSize(LongConfig.values().size)
    }

    @Test
    fun getStringConfigs() {
        // Act
        val result = sut.getStringConfigs()

        // Assert
        assertThat(result).hasSize(StringConfig.values().size)
    }

    @Test
    fun getBooleanValue() = runBlocking {
        // Arrange
        val expectedResult = DataFactory.createRandomBoolean()
        fakeConfigProviderSource.booleanResult = DataResult.Success(expectedResult)

        // Act
        val result = sut.getBooleanValue(mockkClass(BooleanConfig::class))

        // Assert
        assertThat((result as DataResult.Success).value).isEqualTo(expectedResult)
    }

    @Test
    fun getDoubleValue() = runBlocking {
        // Arrange
        val expectedResult = DataFactory.createRandomDouble()
        fakeConfigProviderSource.doubleResult = DataResult.Success(expectedResult)

        // Act
        val result = sut.getDoubleValue(mockkClass(DoubleConfig::class))

        // Assert
        assertThat((result as DataResult.Success).value).isEqualTo(expectedResult)
    }

    @Test
    fun getLongValue() = runBlocking {
        // Arrange
        val expectedResult = DataFactory.createRandomLong()
        fakeConfigProviderSource.longResult = DataResult.Success(expectedResult)

        // Act
        val result = sut.getLongValue(mockkClass(LongConfig::class))

        // Assert
        assertThat((result as DataResult.Success).value).isEqualTo(expectedResult)
    }

    @Test
    fun getStringValue() = runBlocking {
        // Arrange
        val expectedResult = DataFactory.createRandomString()
        fakeConfigProviderSource.stringResult = DataResult.Success(expectedResult)

        // Act
        val result = sut.getStringValue(mockkClass(StringConfig::class))

        // Assert
        assertThat((result as DataResult.Success).value).isEqualTo(expectedResult)
    }
}