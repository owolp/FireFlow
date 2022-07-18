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

package dev.zitech.settings.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.zitech.core.persistence.data.repository.preferences.ContainsPreferencesRepositoryImpl
import dev.zitech.core.persistence.data.repository.preferences.GetPreferencesRepositoryImpl
import dev.zitech.core.persistence.data.repository.preferences.RemovePreferencesRepositoryImpl
import dev.zitech.core.persistence.data.repository.preferences.SavePreferencesRepositoryImpl
import dev.zitech.core.persistence.di.PreferencesSingletonProvidesModule
import dev.zitech.core.persistence.di.annotation.DevelopmentPreferencesDataSource
import dev.zitech.core.persistence.di.annotation.SecuredPreferencesDataSource
import dev.zitech.core.persistence.di.annotation.StandardPreferencesDataSource
import dev.zitech.core.persistence.domain.repository.preferences.ContainsPreferencesRepository
import dev.zitech.core.persistence.domain.repository.preferences.GetPreferencesRepository
import dev.zitech.core.persistence.domain.repository.preferences.RemovePreferencesRepository
import dev.zitech.core.persistence.domain.repository.preferences.SavePreferencesRepository
import dev.zitech.core.persistence.domain.source.preferences.PreferencesDataSource
import dev.zitech.settings.di.fake.FakePreferencesDataSource
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PreferencesSingletonProvidesModule::class]
)
@Module
internal object FakePreferencesSingletonProvidesModule {

    @DevelopmentPreferencesDataSource
    @Singleton
    @Provides
    fun developmentPreferencesDataSource(): PreferencesDataSource = FakePreferencesDataSource

    @SecuredPreferencesDataSource
    @Singleton
    @Provides
    fun securedPreferencesDataSource(): PreferencesDataSource = FakePreferencesDataSource

    @StandardPreferencesDataSource
    @Singleton
    @Provides
    fun standardPreferencesDataSource(): PreferencesDataSource = FakePreferencesDataSource

    @Singleton
    @Provides
    fun containsPreferencesRepository(
        @DevelopmentPreferencesDataSource developmentPreferencesDataSource: PreferencesDataSource,
        @SecuredPreferencesDataSource securedPreferencesDataSource: PreferencesDataSource,
        @StandardPreferencesDataSource standardPreferencesDataSource: PreferencesDataSource
    ): ContainsPreferencesRepository = ContainsPreferencesRepositoryImpl(
        developmentPreferencesDataSource = developmentPreferencesDataSource,
        securedPreferencesDataSource = securedPreferencesDataSource,
        standardPreferencesDataSource = standardPreferencesDataSource
    )

    @Singleton
    @Provides
    fun getPreferencesRepository(
        @DevelopmentPreferencesDataSource developmentPreferencesDataSource: PreferencesDataSource,
        @SecuredPreferencesDataSource securedPreferencesDataSource: PreferencesDataSource,
        @StandardPreferencesDataSource standardPreferencesDataSource: PreferencesDataSource
    ): GetPreferencesRepository = GetPreferencesRepositoryImpl(
        developmentPreferencesDataSource = developmentPreferencesDataSource,
        securedPreferencesDataSource = securedPreferencesDataSource,
        standardPreferencesDataSource = standardPreferencesDataSource
    )

    @Singleton
    @Provides
    fun removePreferencesRepository(
        @DevelopmentPreferencesDataSource developmentPreferencesDataSource: PreferencesDataSource,
        @SecuredPreferencesDataSource securedPreferencesDataSource: PreferencesDataSource,
        @StandardPreferencesDataSource standardPreferencesDataSource: PreferencesDataSource
    ): RemovePreferencesRepository = RemovePreferencesRepositoryImpl(
        developmentPreferencesDataSource = developmentPreferencesDataSource,
        securedPreferencesDataSource = securedPreferencesDataSource,
        standardPreferencesDataSource = standardPreferencesDataSource
    )

    @Singleton
    @Provides
    fun savePreferencesRepository(
        @DevelopmentPreferencesDataSource developmentPreferencesDataSource: PreferencesDataSource,
        @SecuredPreferencesDataSource securedPreferencesDataSource: PreferencesDataSource,
        @StandardPreferencesDataSource standardPreferencesDataSource: PreferencesDataSource
    ): SavePreferencesRepository = SavePreferencesRepositoryImpl(
        developmentPreferencesDataSource = developmentPreferencesDataSource,
        securedPreferencesDataSource = securedPreferencesDataSource,
        standardPreferencesDataSource = standardPreferencesDataSource
    )
}
