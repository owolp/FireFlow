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

package dev.zitech.core.persistence.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zitech.core.common.framework.dispatcher.AppDispatchers
import dev.zitech.core.common.framework.strings.StringsProvider
import dev.zitech.core.persistence.data.database.FireFlowDatabase
import dev.zitech.core.persistence.data.database.dao.UserAccountDao
import dev.zitech.core.persistence.data.database.mapper.UserAccountMapper
import dev.zitech.core.persistence.data.database.repository.DatabaseKeyRepositoryImpl
import dev.zitech.core.persistence.data.database.repository.UserAccountRepositoryImpl
import dev.zitech.core.persistence.data.preferences.repository.ContainsPreferencesRepositoryImpl
import dev.zitech.core.persistence.data.preferences.repository.GetPreferencesRepositoryImpl
import dev.zitech.core.persistence.data.preferences.repository.RemovePreferencesRepositoryImpl
import dev.zitech.core.persistence.data.preferences.repository.SavePreferencesRepositoryImpl
import dev.zitech.core.persistence.di.annotation.DevelopmentPreferencesDataSource
import dev.zitech.core.persistence.di.annotation.SecuredPreferencesDataSource
import dev.zitech.core.persistence.di.annotation.StandardPreferencesDataSource
import dev.zitech.core.persistence.domain.model.PreferenceType
import dev.zitech.core.persistence.domain.repository.ContainsPreferencesRepository
import dev.zitech.core.persistence.domain.repository.DatabaseKeyRepository
import dev.zitech.core.persistence.domain.repository.GetPreferencesRepository
import dev.zitech.core.persistence.domain.repository.RemovePreferencesRepository
import dev.zitech.core.persistence.domain.repository.SavePreferencesRepository
import dev.zitech.core.persistence.domain.repository.UserAccountRepository
import dev.zitech.core.persistence.framework.database.DatabaseFactory
import dev.zitech.core.persistence.framework.database.user.UserAccountDatabaseSource
import dev.zitech.core.persistence.framework.database.user.UserAccountDatabaseSourceImpl
import dev.zitech.core.persistence.framework.preference.PreferencesDataSource
import dev.zitech.core.persistence.framework.preference.PreferencesFactory
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking

@InstallIn(SingletonComponent::class)
@Module
internal object PersistenceSingletonProvidesModule {

    @DevelopmentPreferencesDataSource
    @Singleton
    @Provides
    fun developmentPreferencesDataSource(
        appDispatchers: AppDispatchers,
        @ApplicationContext applicationContext: Context
    ): PreferencesDataSource = PreferencesFactory.createsPreferences(
        appDispatchers,
        applicationContext,
        PreferenceType.DEVELOPMENT
    )

    @SecuredPreferencesDataSource
    @Singleton
    @Provides
    fun securedPreferencesDataSource(
        appDispatchers: AppDispatchers,
        @ApplicationContext applicationContext: Context
    ): PreferencesDataSource = PreferencesFactory.createsPreferences(
        appDispatchers,
        applicationContext,
        PreferenceType.SECURED
    )

    @StandardPreferencesDataSource
    @Singleton
    @Provides
    fun standardPreferencesDataSource(
        appDispatchers: AppDispatchers,
        @ApplicationContext applicationContext: Context
    ): PreferencesDataSource = PreferencesFactory.createsPreferences(
        appDispatchers,
        applicationContext,
        PreferenceType.STANDARD
    )

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

    @Singleton
    @Provides
    fun userAccountDatabaseSource(
        fireFlowDatabase: FireFlowDatabase,
        userAccountMapper: UserAccountMapper
    ): UserAccountDatabaseSource =
        UserAccountDatabaseSourceImpl(
            userAccountDao = fireFlowDatabase.userAccountDao(),
            userAccountMapper = userAccountMapper
        )

    @Singleton
    @Provides
    fun databaseFactory(
        @ApplicationContext applicationContext: Context,
        databaseKeyRepository: DatabaseKeyRepository
    ): FireFlowDatabase = runBlocking {
        DatabaseFactory(
            context = applicationContext,
            databaseKeyRepository = databaseKeyRepository
        ).createRoomDatabase("fireflow")
    }

    @Singleton
    @Provides
    fun userAccountDao(fireFlowDatabase: FireFlowDatabase): UserAccountDao =
        fireFlowDatabase.userAccountDao()

    @Singleton
    @Provides
    fun userAccountRepository(
        userAccountDatabaseSource: UserAccountDatabaseSource,
        stringsProvider: StringsProvider
    ): UserAccountRepository = UserAccountRepositoryImpl(
        userAccountDatabaseSource = userAccountDatabaseSource,
        stringsProvider = stringsProvider
    )
}

@InstallIn(SingletonComponent::class)
@Module
internal interface PersistenceSingletonBindsModule {

    @Singleton
    @Binds
    fun databaseKeyRepository(
        databaseKeyRepositoryImpl: DatabaseKeyRepositoryImpl
    ): DatabaseKeyRepository
}
