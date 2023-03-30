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

package dev.zitech.core.common.data.repository

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.domain.cache.Cache
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CacheRepositoryImplTest {

    private lateinit var sut: CacheRepositoryImpl

    @BeforeEach
    fun setup() {
        sut = CacheRepositoryImpl()
    }

    @Nested
    @DisplayName("WHEN addCache")
    inner class AddCache {

        @Test
        @DisplayName("and caches contains cache THEN do not add to cache")
        fun doNotAdd() {
            // Arrange
            val cache = getStubCache()
            sut.addCache(cache)

            // Assert
            assertThat(sut.caches).hasSize(1)
            assertThat(sut.caches).contains(cache)

            // Act
            sut.addCache(cache)

            // Assert
            assertThat(sut.caches).hasSize(1)
        }

        @Test
        @DisplayName("and caches does not contain cache THEN add to cache")
        fun add() {
            // Arrange
            val cache = getStubCache()

            // Assert
            assertThat(sut.caches).isEmpty()
            assertThat(sut.caches).doesNotContain(cache)

            // Act
            sut.addCache(cache)

            // Assert
            assertThat(sut.caches).hasSize(1)
            assertThat(sut.caches).contains(cache)
        }
    }

    @Nested
    @DisplayName("WHEN removeCache")
    inner class RemoveCache {

        @Test
        @DisplayName("and caches does not contain cache THEN do not remove from cache")
        fun doNotRemove() {
            // Arrange
            val cache = getStubCache()

            // Assert
            assertThat(sut.caches).isEmpty()
            assertThat(sut.caches).doesNotContain(cache)

            // Act
            sut.removeCache(cache)

            // Assert
            assertThat(sut.caches).isEmpty()
            assertThat(sut.caches).doesNotContain(cache)
        }

        @Test
        @DisplayName("and caches contains cache THEN remove from cache")
        fun remove() {
            // Arrange
            val cache = getStubCache()
            sut.caches.add(cache)

            // Assert
            assertThat(sut.caches).hasSize(1)
            assertThat(sut.caches).contains(cache)

            // Act
            sut.removeCache(cache)

            // Assert
            assertThat(sut.caches).isEmpty()
            assertThat(sut.caches).doesNotContain(cache)
        }
    }

    @Test
    fun invalidateCaches() {
        // Arrange
        val cache = mockkClass(Cache::class, relaxUnitFun = true)
        sut.caches.add(cache)

        // Act
        sut.invalidateCaches()

        // Assert
        verify { cache.invalidate() }
    }

    private fun getStubCache() = object : Cache {
        override fun invalidate() {
            // NO_OP
        }
    }
}
