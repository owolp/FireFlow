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

package dev.zitech.core.persistence.data.repository.database

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.persistence.data.repository.preferences.ContainsPreferencesRepositoryImpl
import dev.zitech.core.persistence.data.repository.preferences.GetPreferencesRepositoryImpl
import dev.zitech.core.persistence.data.repository.preferences.SavePreferencesRepositoryImpl
import dev.zitech.core.persistence.domain.repository.preferences.ContainsPreferencesRepository
import dev.zitech.core.persistence.domain.repository.database.DatabaseKeyRepository
import dev.zitech.core.persistence.domain.repository.preferences.GetPreferencesRepository
import dev.zitech.core.persistence.domain.repository.preferences.SavePreferencesRepository
import dev.zitech.core.persistence.framework.preferences.FakePreferencesDataSource
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class DatabaseKeyRepositoryImplTest {

    companion object {
        private const val ENCRYPTION_LENGTH = 20
        private const val KEY_SECURE_STORAGE_SECURED_DATABASE = "secured_database"
    }

    private val fakePreferencesDataSource = FakePreferencesDataSource()

    private val containsPreferencesRepository: ContainsPreferencesRepository =
        ContainsPreferencesRepositoryImpl(
            fakePreferencesDataSource,
            fakePreferencesDataSource,
            fakePreferencesDataSource
        )
    private val getPreferencesRepository: GetPreferencesRepository =
        GetPreferencesRepositoryImpl(
            fakePreferencesDataSource,
            fakePreferencesDataSource,
            fakePreferencesDataSource
        )
    private val savePreferencesRepository: SavePreferencesRepository =
        SavePreferencesRepositoryImpl(
            fakePreferencesDataSource,
            fakePreferencesDataSource,
            fakePreferencesDataSource
        )

    private lateinit var sut: DatabaseKeyRepository

    @BeforeEach
    fun setup() {
        sut = DatabaseKeyRepositoryImpl(
            containsPreferencesRepository,
            getPreferencesRepository,
            savePreferencesRepository
        )
    }

    @Test
    @DisplayName("GIVEN key is not saved WHEN getDatabaseKey THEN save key and return it")
    fun keyNotSaved() = runBlocking {
        // Act
        val result = sut.getDatabaseKey()

        // Assert
        assertThat(result.value.size).isEqualTo(ENCRYPTION_LENGTH)
    }

    @Test
    @DisplayName("GIVEN key is saved WHEN getDatabaseKey THEN return it")
    fun keySaved() = runBlocking {
        // Arrange
        val value = DataFactory.createRandomString()
        val expectedValue = value.toCharArray()
        fakePreferencesDataSource.saveString(
            KEY_SECURE_STORAGE_SECURED_DATABASE,
            value
        )

        // Act
        val result = sut.getDatabaseKey()

        // Assert
        assertThat(result.value).isEqualTo(expectedValue)
    }
}