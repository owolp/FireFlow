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

package dev.zitech.core.storage.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zitech.core.storage.data.preferences.repository.ContainsPreferencesRepositoryImpl
import dev.zitech.core.storage.data.preferences.repository.GetPreferencesRepositoryImpl
import dev.zitech.core.storage.data.preferences.repository.RemovePreferencesRepositoryImpl
import dev.zitech.core.storage.data.preferences.repository.SavePreferencesRepositoryImpl
import dev.zitech.core.storage.di.annotation.StandardPreferencesDataSource
import dev.zitech.core.storage.domain.model.PreferenceType
import dev.zitech.core.storage.domain.repository.ContainsPreferencesRepository
import dev.zitech.core.storage.domain.repository.GetPreferencesRepository
import dev.zitech.core.storage.domain.repository.RemovePreferencesRepository
import dev.zitech.core.storage.domain.repository.SavePreferencesRepository
import dev.zitech.core.storage.framework.factory.PreferencesFactory
import dev.zitech.core.storage.framework.preference.PreferencesDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object StorageSingletonModule {

    @StandardPreferencesDataSource
    @Singleton
    @Provides
    fun standardPreferencesDataSource(
        @ApplicationContext applicationContext: Context
    ): PreferencesDataSource {
        return PreferencesFactory.createsPreferences(
            applicationContext,
            PreferenceType.STANDARD
        )
    }

    @Singleton
    @Provides
    fun containsPreferencesRepository(
        @StandardPreferencesDataSource standardPreferencesDataSource: PreferencesDataSource
    ): ContainsPreferencesRepository {
        return ContainsPreferencesRepositoryImpl(
            standardPreferencesDataSource = standardPreferencesDataSource
        )
    }

    @Singleton
    @Provides
    fun getPreferencesRepository(
        @StandardPreferencesDataSource standardPreferencesDataSource: PreferencesDataSource
    ): GetPreferencesRepository {
        return GetPreferencesRepositoryImpl(
            standardPreferencesDataSource = standardPreferencesDataSource
        )
    }

    @Singleton
    @Provides
    fun removePreferencesRepository(
        @StandardPreferencesDataSource standardPreferencesDataSource: PreferencesDataSource
    ): RemovePreferencesRepository {
        return RemovePreferencesRepositoryImpl(
            standardPreferencesDataSource = standardPreferencesDataSource
        )
    }

    @Singleton
    @Provides
    fun savePreferencesRepository(
        @StandardPreferencesDataSource standardPreferencesDataSource: PreferencesDataSource
    ): SavePreferencesRepository {
        return SavePreferencesRepositoryImpl(
            standardPreferencesDataSource = standardPreferencesDataSource
        )
    }
}
