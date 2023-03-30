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

import dev.zitech.core.common.DataFactory
import dev.zitech.core.persistence.data.repository.preferences.RemovePreferencesRepositoryImpl
import dev.zitech.core.persistence.domain.model.preferences.PreferenceType
import dev.zitech.core.persistence.domain.repository.preferences.RemovePreferencesRepository
import dev.zitech.core.persistence.domain.source.preferences.PreferencesDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RemovePreferencesRepositoryImplTest {

    private val key = DataFactory.createRandomString()

    private val developmentPreferencesDataSource = mockk<PreferencesDataSource>()
    private val securedPreferencesDataSource = mockk<PreferencesDataSource>()
    private val standardPreferencesDataSource = mockk<PreferencesDataSource>()

    private lateinit var sut: RemovePreferencesRepository

    @BeforeEach
    fun setUp() {
        sut = RemovePreferencesRepositoryImpl(
            developmentPreferencesDataSource = developmentPreferencesDataSource,
            securedPreferencesDataSource = securedPreferencesDataSource,
            standardPreferencesDataSource = standardPreferencesDataSource,
        )
    }

    @Nested
    inner class Boolean {

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.DEVELOPMENT THEN return result from " +
                "developmentPreferencesDataSource",
        )
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT

            coEvery { developmentPreferencesDataSource.removeBoolean(key) } just Runs

            sut.removeBoolean(preferenceType, key)

            coVerify { developmentPreferencesDataSource.removeBoolean(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource",
        )
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED

            coEvery { securedPreferencesDataSource.removeBoolean(key) } just Runs

            sut.removeBoolean(preferenceType, key)

            coVerify { securedPreferencesDataSource.removeBoolean(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource",
        )
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD

            coEvery { standardPreferencesDataSource.removeBoolean(key) } just Runs

            sut.removeBoolean(preferenceType, key)

            coVerify { standardPreferencesDataSource.removeBoolean(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }
    }

    @Nested
    inner class Float {

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.DEVELOPMENT THEN return result from " +
                "developmentPreferencesDataSource",
        )
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT

            coEvery { developmentPreferencesDataSource.removeFloat(key) } just Runs

            sut.removeFloat(preferenceType, key)

            coVerify { developmentPreferencesDataSource.removeFloat(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource",
        )
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED

            coEvery { securedPreferencesDataSource.removeFloat(key) } just Runs

            sut.removeFloat(preferenceType, key)

            coVerify { securedPreferencesDataSource.removeFloat(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource",
        )
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD

            coEvery { standardPreferencesDataSource.removeFloat(key) } just Runs

            sut.removeFloat(preferenceType, key)

            coVerify { standardPreferencesDataSource.removeFloat(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }
    }

    @Nested
    inner class Int {

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.DEVELOPMENT THEN return result from " +
                "developmentPreferencesDataSource",
        )
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT

            coEvery { developmentPreferencesDataSource.removeInt(key) } just Runs

            sut.removeInt(preferenceType, key)

            coVerify { developmentPreferencesDataSource.removeInt(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource",
        )
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED

            coEvery { securedPreferencesDataSource.removeInt(key) } just Runs

            sut.removeInt(preferenceType, key)

            coVerify { securedPreferencesDataSource.removeInt(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource",
        )
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD

            coEvery { standardPreferencesDataSource.removeInt(key) } just Runs

            sut.removeInt(preferenceType, key)

            coVerify { standardPreferencesDataSource.removeInt(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }
    }

    @Nested
    inner class Long {

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.DEVELOPMENT THEN return result from " +
                "developmentPreferencesDataSource",
        )
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT

            coEvery { developmentPreferencesDataSource.removeLong(key) } just Runs

            sut.removeLong(preferenceType, key)

            coVerify { developmentPreferencesDataSource.removeLong(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource",
        )
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED

            coEvery { securedPreferencesDataSource.removeLong(key) } just Runs

            sut.removeLong(preferenceType, key)

            coVerify { securedPreferencesDataSource.removeLong(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource",
        )
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD

            coEvery { standardPreferencesDataSource.removeLong(key) } just Runs

            sut.removeLong(preferenceType, key)

            coVerify { standardPreferencesDataSource.removeLong(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }
    }

    @Nested
    inner class String {

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.DEVELOPMENT THEN return result from " +
                "developmentPreferencesDataSource",
        )
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT

            coEvery { developmentPreferencesDataSource.removeString(key) } just Runs

            sut.removeString(preferenceType, key)

            coVerify { developmentPreferencesDataSource.removeString(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource",
        )
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED

            coEvery { securedPreferencesDataSource.removeString(key) } just Runs

            sut.removeString(preferenceType, key)

            coVerify { securedPreferencesDataSource.removeString(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource",
        )
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD

            coEvery { standardPreferencesDataSource.removeString(key) } just Runs

            sut.removeString(preferenceType, key)

            coVerify { standardPreferencesDataSource.removeString(key) }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }
    }

    @Nested
    inner class RemoveAll {

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.DEVELOPMENT THEN return result from " +
                "developmentPreferencesDataSource",
        )
        fun developmentPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.DEVELOPMENT

            coEvery { developmentPreferencesDataSource.removeAll() } just Runs

            sut.removeAll(preferenceType)

            coVerify { developmentPreferencesDataSource.removeAll() }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource",
        )
        fun securePreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED

            coEvery { securedPreferencesDataSource.removeAll() } just Runs

            sut.removeAll(preferenceType)

            coVerify { securedPreferencesDataSource.removeAll() }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }

        @Test
        @DisplayName(
            "WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource",
        )
        fun sharedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD

            coEvery { standardPreferencesDataSource.removeAll() } just Runs

            sut.removeAll(preferenceType)

            coVerify { standardPreferencesDataSource.removeAll() }
            confirmVerified(
                developmentPreferencesDataSource,
                securedPreferencesDataSource,
                standardPreferencesDataSource,
            )
        }
    }
}
