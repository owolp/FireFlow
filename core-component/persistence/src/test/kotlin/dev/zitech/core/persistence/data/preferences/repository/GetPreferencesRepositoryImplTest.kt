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

package dev.zitech.core.persistence.data.preferences.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.persistence.data.repository.preferences.GetPreferencesRepositoryImpl
import dev.zitech.core.persistence.domain.model.preferences.PreferenceType
import dev.zitech.core.persistence.domain.repository.preferences.GetPreferencesRepository
import dev.zitech.core.persistence.domain.source.preferences.PreferencesDataSource
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class GetPreferencesRepositoryImplTest {

    private val key = DataFactory.createRandomString()

    private val developmentPreferencesDataSource = mockk<PreferencesDataSource>()
    private val securedPreferencesDataSource = mockk<PreferencesDataSource>()
    private val standardPreferencesDataSource = mockk<PreferencesDataSource>()

    private lateinit var sut: GetPreferencesRepository

    @BeforeEach
    fun setUp() {
        sut = GetPreferencesRepositoryImpl(
            developmentPreferencesDataSource = developmentPreferencesDataSource,
            securedPreferencesDataSource = securedPreferencesDataSource,
            standardPreferencesDataSource = standardPreferencesDataSource
        )
    }

    @Nested
    inner class Boolean {

        private val defaultValue = DataFactory.createRandomBoolean()

        @Test
        @DisplayName("WHEN called with PreferenceType.DEVELOPMENT THEN return result from " +
            "developmentPreferencesDataSource")
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT
            val flowResult = DataFactory.createRandomBoolean()

            every { developmentPreferencesDataSource.getBoolean(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getBoolean(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { developmentPreferencesDataSource.getBoolean(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource")
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val flowResult = DataFactory.createRandomBoolean()

            every { securedPreferencesDataSource.getBoolean(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getBoolean(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { securedPreferencesDataSource.getBoolean(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource")
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val flowResult = DataFactory.createRandomBoolean()

            every { standardPreferencesDataSource.getBoolean(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getBoolean(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { standardPreferencesDataSource.getBoolean(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }
    }

    @Nested
    inner class Float {

        private val defaultValue = DataFactory.createRandomDouble().toFloat()

        @Test
        @DisplayName("WHEN called with PreferenceType.DEVELOPMENT THEN return result from " +
            "developmentPreferencesDataSource")
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT
            val flowResult = DataFactory.createRandomDouble().toFloat()

            every { developmentPreferencesDataSource.getFloat(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getFloat(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { developmentPreferencesDataSource.getFloat(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource")
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val flowResult = DataFactory.createRandomDouble().toFloat()

            every { securedPreferencesDataSource.getFloat(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getFloat(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { securedPreferencesDataSource.getFloat(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource")
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val flowResult = DataFactory.createRandomDouble().toFloat()

            every { standardPreferencesDataSource.getFloat(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getFloat(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { standardPreferencesDataSource.getFloat(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }
    }

    @Nested
    inner class Int {

        private val defaultValue = DataFactory.createRandomInt()

        @Test
        @DisplayName("WHEN called with PreferenceType.DEVELOPMENT THEN return result from " +
            "developmentPreferencesDataSource")
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT
            val flowResult = DataFactory.createRandomInt()

            every { developmentPreferencesDataSource.getInt(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getInt(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { developmentPreferencesDataSource.getInt(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource")
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val flowResult = DataFactory.createRandomInt()

            every { securedPreferencesDataSource.getInt(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getInt(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { securedPreferencesDataSource.getInt(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource")
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val flowResult = DataFactory.createRandomInt()

            every { standardPreferencesDataSource.getInt(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getInt(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { standardPreferencesDataSource.getInt(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }
    }

    @Nested
    inner class Long {

        private val defaultValue = DataFactory.createRandomLong()

        @Test
        @DisplayName("WHEN called with PreferenceType.DEVELOPMENT THEN return result from " +
            "developmentPreferencesDataSource")
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT
            val flowResult = DataFactory.createRandomLong()

            every { developmentPreferencesDataSource.getLong(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getLong(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { developmentPreferencesDataSource.getLong(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource")
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val flowResult = DataFactory.createRandomLong()

            every { securedPreferencesDataSource.getLong(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getLong(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { securedPreferencesDataSource.getLong(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource")
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val flowResult = DataFactory.createRandomLong()

            every { standardPreferencesDataSource.getLong(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getLong(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { standardPreferencesDataSource.getLong(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }
    }

    @Nested
    inner class String {

        private val defaultValue = DataFactory.createRandomString()

        @Test
        @DisplayName("WHEN called with PreferenceType.DEVELOPMENT THEN return result from " +
            "developmentPreferencesDataSource")
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT
            val flowResult = DataFactory.createRandomString()

            every { developmentPreferencesDataSource.getString(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getString(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { developmentPreferencesDataSource.getString(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource")
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val flowResult = DataFactory.createRandomString()

            every { securedPreferencesDataSource.getString(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getString(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { securedPreferencesDataSource.getString(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource")
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val flowResult = DataFactory.createRandomString()

            every { standardPreferencesDataSource.getString(key, defaultValue) } returns flowOf(
                flowResult
            )

            sut.getString(preferenceType, key, defaultValue).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { standardPreferencesDataSource.getString(key, defaultValue) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }
    }
}