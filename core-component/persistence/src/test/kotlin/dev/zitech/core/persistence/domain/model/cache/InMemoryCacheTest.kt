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

package dev.zitech.core.persistence.domain.model.cache

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.framework.time.TimeHelper
import dev.zitech.core.persistence.data.repository.cache.FakeCacheRepository
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class InMemoryCacheTest {

    private val cacheRepository = FakeCacheRepository()

    @Nested
    @DisplayName("WHEN set")
    inner class SetData {

        @Test
        @DisplayName("not null data with positive lifetimeMillis THEN set cache")
        fun notNullDataWithPositive() {
            // Arrange
            val expectedResult = DataFactory.createRandomBoolean()
            val lifetimeMillis = DataFactory.createRandomInt(min = 0)
            val sut = InMemoryCache<Boolean>(cacheRepository, lifetimeMillis)

            mockkObject(TimeHelper)
            val currentTimeMillis = DataFactory.createRandomLong(min = 1000)
            every { TimeHelper.getCurrentTime() } returns currentTimeMillis

            // Assert
            assertThat(cacheRepository.caches).isEmpty()
            assertThat(sut.cacheExpirationTimeMillis).isNull()

            // Act
            sut.data = expectedResult

            // Assert
            assertThat(sut.data).isEqualTo(expectedResult)
            assertThat(sut.cacheExpirationTimeMillis).isEqualTo((currentTimeMillis + lifetimeMillis).toBigInteger())
            assertThat(cacheRepository.caches).contains(sut)
            assertThat(cacheRepository.caches).hasSize(1)
        }

        @Test
        @DisplayName("not null data with negative lifetimeMillis THEN throw IllegalStateException")
        fun notNullDataWithNegative() {
            // Arrange
            val expectedResult = DataFactory.createRandomBoolean()
            val lifetimeMillis = DataFactory.createRandomInt(min = Int.MIN_VALUE, max = -1)
            val sut = InMemoryCache<Boolean>(cacheRepository, lifetimeMillis)

            // Assert
            assertThat(cacheRepository.caches).isEmpty()
            assertThat(sut.cacheExpirationTimeMillis).isNull()

            // Act & Assert
            assertThrows(IllegalStateException::class.java) {
                sut.data = expectedResult
            }
        }

        @Test
        @DisplayName("null data THEN clear cache")
        fun nullData() {
            // Arrange
            val sut = InMemoryCache<Boolean>(cacheRepository)

            // Assert
            assertThat(cacheRepository.caches).isEmpty()
            assertThat(sut.cacheExpirationTimeMillis).isNull()

            // Act
            sut.data = null

            // Assert
            assertThat(sut.data).isNull()
            assertThat(cacheRepository.caches).isEmpty()
            assertThat(sut.cacheExpirationTimeMillis).isNull()
        }
    }

    @Nested
    @DisplayName("WHEN get")
    inner class GetData {
        @Test
        @DisplayName("not null expiration time and expiration time equal THEN do nothing")
        fun notNullExpirationTimeEqual() {
            // Assert
            assertThat(cacheRepository.caches).isEmpty()

            // Arrange
            val expirationTime = 123
            val sut = InMemoryCache<Boolean>(cacheRepository)

            mockkObject(TimeHelper)
            every { TimeHelper.getCurrentTime() } returns expirationTime.toLong()

            sut.cacheExpirationTimeMillis = expirationTime.toBigInteger()

            val data = DataFactory.createRandomBoolean()
            sut.data = data

            // Assert
            assertThat(cacheRepository.caches).hasSize(1)

            // Act
            val expectedResult = sut.data

            // Assert
            assertThat(expectedResult).isEqualTo(data)
            assertThat(cacheRepository.caches).contains(sut)
            assertThat(cacheRepository.caches).hasSize(1)
        }

        @Test
        @DisplayName("not null expiration time and expiration time more THEN do nothing")
        fun notNullExpirationTimeMore() {
            // Assert
            assertThat(cacheRepository.caches).isEmpty()

            // Arrange
            val expirationTime = 123
            val sut = InMemoryCache<Boolean>(cacheRepository)

            mockkObject(TimeHelper)
            every { TimeHelper.getCurrentTime() } returns expirationTime.toLong()

            sut.cacheExpirationTimeMillis = 456.toBigInteger()

            val data = DataFactory.createRandomBoolean()
            sut.data = data

            // Assert
            assertThat(cacheRepository.caches).hasSize(1)

            // Act
            val expectedResult = sut.data

            // Assert
            assertThat(expectedResult).isEqualTo(data)
            assertThat(cacheRepository.caches).contains(sut)
            assertThat(cacheRepository.caches).hasSize(1)
        }

        @Test
        @DisplayName("not null expiration time and expiration time less THEN invalidate")
        fun notNullExpirationTimeLess() {
            // Assert
            assertThat(cacheRepository.caches).isEmpty()

            // Arrange
            val sut = InMemoryCache<Boolean>(cacheRepository)
            val data = DataFactory.createRandomBoolean()
            sut.data = data

            val expirationTime = 456
            mockkObject(TimeHelper)
            every { TimeHelper.getCurrentTime() } returns expirationTime.toLong()

            sut.cacheExpirationTimeMillis = 123.toBigInteger()

            // Assert
            assertThat(cacheRepository.caches).hasSize(1)

            // Act
            val expectedResult = sut.data

            // Assert
            assertThat(expectedResult).isNull()
            assertThat(cacheRepository.caches).doesNotContain(sut)
            assertThat(cacheRepository.caches).isEmpty()
        }

        @Test
        @DisplayName("null expiration time THEN do nothing")
        fun nullExpirationTime() {
            // Assert
            assertThat(cacheRepository.caches).isEmpty()

            // Arrange
            val expirationTime = 123
            val sut = InMemoryCache<Boolean>(cacheRepository)

            mockkObject(TimeHelper)
            every { TimeHelper.getCurrentTime() } returns expirationTime.toLong()

            sut.cacheExpirationTimeMillis = null

            val data = DataFactory.createRandomBoolean()
            sut.data = data

            // Assert
            assertThat(cacheRepository.caches).hasSize(1)

            // Act
            val expectedResult = sut.data

            // Assert
            assertThat(expectedResult).isEqualTo(data)
            assertThat(cacheRepository.caches).contains(sut)
            assertThat(cacheRepository.caches).hasSize(1)
        }
    }

    @Test
    fun invalidate() {
        // Arrange
        val sut = InMemoryCache<Boolean>(cacheRepository)

        sut.data = DataFactory.createRandomBoolean()
        val expirationTime = 123
        sut.cacheExpirationTimeMillis = expirationTime.toBigInteger()

        // Assert
        assertThat(cacheRepository.caches).hasSize(1)

        // Act
        sut.invalidate()

        // Assert
        assertThat(sut.data).isNull()
        assertThat(sut.cacheExpirationTimeMillis).isNull()
        assertThat(cacheRepository.caches).isEmpty()
    }
}