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

package dev.zitech.core.persistence.data.preferences.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.persistence.data.repository.preferences.ContainsPreferencesRepositoryImpl
import dev.zitech.core.persistence.domain.model.preferences.PreferenceType
import dev.zitech.core.persistence.domain.repository.preferences.ContainsPreferencesRepository
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

internal class ContainsPreferencesRepositoryImplTest {

    private val key = DataFactory.createRandomString()

    private val developmentPreferencesDataSource = mockk<PreferencesDataSource>()
    private val securedPreferencesDataSource = mockk<PreferencesDataSource>()
    private val standardPreferencesDataSource = mockk<PreferencesDataSource>()

    private lateinit var sut: ContainsPreferencesRepository

    @BeforeEach
    fun setUp() {
        sut = ContainsPreferencesRepositoryImpl(
            developmentPreferencesDataSource = developmentPreferencesDataSource,
            securedPreferencesDataSource = securedPreferencesDataSource,
            standardPreferencesDataSource = standardPreferencesDataSource
        )
    }

    @Nested
    inner class Boolean {

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.DEVELOPMENT THEN return result " +
                "from developmentPreferencesDataSource"
        )
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT
            val flowResult = DataFactory.createRandomBoolean()

            every { developmentPreferencesDataSource.containsBoolean(key) } returns flowOf(
                flowResult
            )

            sut.containsBoolean(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { developmentPreferencesDataSource.containsBoolean(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource"
        )
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val flowResult = DataFactory.createRandomBoolean()

            every { securedPreferencesDataSource.containsBoolean(key) } returns flowOf(
                flowResult
            )

            sut.containsBoolean(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { securedPreferencesDataSource.containsBoolean(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource"
        )
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val flowResult = DataFactory.createRandomBoolean()

            every { standardPreferencesDataSource.containsBoolean(key) } returns flowOf(
                flowResult
            )

            sut.containsBoolean(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { standardPreferencesDataSource.containsBoolean(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }
    }

    @Nested
    inner class Float {

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.DEVELOPMENT THEN return result " +
                "from developmentPreferencesDataSource"
        )
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT
            val flowResult = DataFactory.createRandomBoolean()

            every { developmentPreferencesDataSource.containsFloat(key) } returns flowOf(
                flowResult
            )

            sut.containsFloat(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { developmentPreferencesDataSource.containsFloat(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource"
        )
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val flowResult = DataFactory.createRandomBoolean()

            every { securedPreferencesDataSource.containsFloat(key) } returns flowOf(
                flowResult
            )

            sut.containsFloat(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { securedPreferencesDataSource.containsFloat(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource"
        )
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val flowResult = DataFactory.createRandomBoolean()

            every { standardPreferencesDataSource.containsFloat(key) } returns flowOf(
                flowResult
            )

            sut.containsFloat(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { standardPreferencesDataSource.containsFloat(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }
    }

    @Nested
    inner class Int {

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.DEVELOPMENT THEN return result " +
                "from developmentPreferencesDataSource"
        )
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT
            val flowResult = DataFactory.createRandomBoolean()

            every { developmentPreferencesDataSource.containsInt(key) } returns flowOf(
                flowResult
            )

            sut.containsInt(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { developmentPreferencesDataSource.containsInt(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource"
        )
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val flowResult = DataFactory.createRandomBoolean()

            every { securedPreferencesDataSource.containsInt(key) } returns flowOf(
                flowResult
            )

            sut.containsInt(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { securedPreferencesDataSource.containsInt(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource"
        )
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val flowResult = DataFactory.createRandomBoolean()

            every { standardPreferencesDataSource.containsInt(key) } returns flowOf(
                flowResult
            )

            sut.containsInt(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { standardPreferencesDataSource.containsInt(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }
    }

    @Nested
    inner class Long {

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.DEVELOPMENT THEN return result " +
                "from developmentPreferencesDataSource"
        )
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT
            val flowResult = DataFactory.createRandomBoolean()

            every { developmentPreferencesDataSource.containsLong(key) } returns flowOf(
                flowResult
            )

            sut.containsLong(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { developmentPreferencesDataSource.containsLong(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource"
        )
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val flowResult = DataFactory.createRandomBoolean()

            every { securedPreferencesDataSource.containsLong(key) } returns flowOf(
                flowResult
            )

            sut.containsLong(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { securedPreferencesDataSource.containsLong(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource"
        )
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val flowResult = DataFactory.createRandomBoolean()

            every { standardPreferencesDataSource.containsLong(key) } returns flowOf(
                flowResult
            )

            sut.containsLong(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { standardPreferencesDataSource.containsLong(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }
    }

    @Nested
    inner class String {

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.DEVELOPMENT THEN return result " +
                "from developmentPreferencesDataSource"
        )
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT
            val flowResult = DataFactory.createRandomBoolean()

            every { developmentPreferencesDataSource.containsString(key) } returns flowOf(
                flowResult
            )

            sut.containsString(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { developmentPreferencesDataSource.containsString(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource"
        )
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val flowResult = DataFactory.createRandomBoolean()

            every { securedPreferencesDataSource.containsString(key) } returns flowOf(
                flowResult
            )

            sut.containsString(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { securedPreferencesDataSource.containsString(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource"
        )
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val flowResult = DataFactory.createRandomBoolean()

            every { standardPreferencesDataSource.containsString(key) } returns flowOf(
                flowResult
            )

            sut.containsString(preferenceType, key).test {
                assertThat(awaitItem()).isEqualTo(flowResult)
                awaitComplete()
            }
            coVerify { standardPreferencesDataSource.containsString(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource
            )
        }
    }
}
