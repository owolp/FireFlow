/*
 * Copyright (C) 2022 Zitech
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

package dev.zitech.core.storage.data.preferences.repository

import dev.zitech.core.common.DataFactory
import dev.zitech.core.storage.domain.model.PreferenceType
import dev.zitech.core.storage.domain.repository.SavePreferencesRepository
import dev.zitech.core.storage.framework.preference.PreferencesDataSource
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

internal class SavePreferencesRepositoryImplTest {

    private val key = DataFactory.createRandomString()

    private val securedPreferencesDataSource = mockk<PreferencesDataSource>()
    private val standardPreferencesDataSource = mockk<PreferencesDataSource>()

    private lateinit var sut: SavePreferencesRepository

    @BeforeEach
    fun setUp() {
        sut = SavePreferencesRepositoryImpl(
            securedPreferencesDataSource = securedPreferencesDataSource,
            standardPreferencesDataSource = standardPreferencesDataSource
        )
    }

    @Nested
    inner class Boolean {


        @Test
        @DisplayName("WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource")
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val value = DataFactory.createRandomBoolean()

            coEvery { securedPreferencesDataSource.saveBoolean(key, value) } just Runs

            sut.saveBoolean(preferenceType, key, value)

            coVerify { securedPreferencesDataSource.saveBoolean(key, value) }
            confirmVerified(securedPreferencesDataSource, standardPreferencesDataSource)
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource")
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val value = DataFactory.createRandomBoolean()

            coEvery { standardPreferencesDataSource.saveBoolean(key, value) } just Runs

            sut.saveBoolean(preferenceType, key, value)

            coVerify { standardPreferencesDataSource.saveBoolean(key, value) }
            confirmVerified(securedPreferencesDataSource, standardPreferencesDataSource)
        }
    }

    @Nested
    inner class Float {



        @Test
        @DisplayName("WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource")
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val value = DataFactory.createRandomDouble().toFloat()

            coEvery { securedPreferencesDataSource.saveFloat(key, value) } just Runs

            sut.saveFloat(preferenceType, key, value)

            coVerify { securedPreferencesDataSource.saveFloat(key, value) }
            confirmVerified(securedPreferencesDataSource, standardPreferencesDataSource)
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource")
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val value = DataFactory.createRandomDouble().toFloat()

            coEvery { standardPreferencesDataSource.saveFloat(key, value) } just Runs

            sut.saveFloat(preferenceType, key, value)

            coVerify { standardPreferencesDataSource.saveFloat(key, value) }
            confirmVerified(securedPreferencesDataSource, standardPreferencesDataSource)
        }
    }

    @Nested
    inner class Int {



        @Test
        @DisplayName("WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource")
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val value = DataFactory.createRandomInt()

            coEvery { securedPreferencesDataSource.saveInt(key, value) } just Runs

            sut.saveInt(preferenceType, key, value)

            coVerify { securedPreferencesDataSource.saveInt(key, value) }
            confirmVerified(securedPreferencesDataSource, standardPreferencesDataSource)
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource")
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val value = DataFactory.createRandomInt()

            coEvery { standardPreferencesDataSource.saveInt(key, value) } just Runs

            sut.saveInt(preferenceType, key, value)

            coVerify { standardPreferencesDataSource.saveInt(key, value) }
            confirmVerified(securedPreferencesDataSource, standardPreferencesDataSource)
        }
    }

    @Nested
    inner class Long {



        @Test
        @DisplayName("WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource")
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val value = DataFactory.createRandomLong()

            coEvery { securedPreferencesDataSource.saveLong(key, value) } just Runs

            sut.saveLong(preferenceType, key, value)

            coVerify { securedPreferencesDataSource.saveLong(key, value) }
            confirmVerified(securedPreferencesDataSource, standardPreferencesDataSource)
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource")
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val value = DataFactory.createRandomLong()

            coEvery { standardPreferencesDataSource.saveLong(key, value) } just Runs

            sut.saveLong(preferenceType, key, value)

            coVerify { standardPreferencesDataSource.saveLong(key, value) }
            confirmVerified(securedPreferencesDataSource, standardPreferencesDataSource)
        }
    }

    @Nested
    inner class String {



        @Test
        @DisplayName("WHEN called with PreferenceType.SECURED THEN return result from securedPreferencesDataSource")
        fun securedPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.SECURED
            val value = DataFactory.createRandomString()

            coEvery { securedPreferencesDataSource.saveString(key, value) } just Runs

            sut.saveString(preferenceType, key, value)

            coVerify { securedPreferencesDataSource.saveString(key, value) }
            confirmVerified(securedPreferencesDataSource, standardPreferencesDataSource)
        }

        @Test
        @DisplayName("WHEN called with PreferenceType.STANDARD THEN return result from standardPreferencesDataSource")
        fun standardPreferencesDataSource() = runBlocking {
            val preferenceType = PreferenceType.STANDARD
            val value = DataFactory.createRandomString()

            coEvery { standardPreferencesDataSource.saveString(key, value) } just Runs

            sut.saveString(preferenceType, key, value)

            coVerify { standardPreferencesDataSource.saveString(key, value) }
            confirmVerified(securedPreferencesDataSource, standardPreferencesDataSource)
        }
    }
}