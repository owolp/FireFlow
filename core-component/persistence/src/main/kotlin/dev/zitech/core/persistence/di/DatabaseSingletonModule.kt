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
import dev.zitech.core.common.domain.applicationconfig.AppConfigProvider
import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.core.persistence.data.cache.UserAccountInMemoryCache
import dev.zitech.core.persistence.data.repository.database.DatabaseKeyRepositoryImpl
import dev.zitech.core.persistence.data.repository.database.UserAccountRepositoryImpl
import dev.zitech.core.persistence.domain.repository.database.DatabaseKeyRepository
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import dev.zitech.core.persistence.domain.source.database.UserAccountDatabaseSource
import dev.zitech.core.persistence.framework.database.FireFlowDatabase
import dev.zitech.core.persistence.framework.database.dao.UserAccountDao
import dev.zitech.core.persistence.framework.database.factory.DatabaseFactory
import dev.zitech.core.persistence.framework.database.mapper.UserAccountMapper
import dev.zitech.core.persistence.framework.database.source.UserAccountDatabaseSourceImpl
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

const val DATABASE_NAME = "fireflow"

@InstallIn(SingletonComponent::class)
@Module
object DatabaseSingletonProvidesModule {

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
    fun database(
        @ApplicationContext applicationContext: Context,
        appConfigProvider: AppConfigProvider,
        databaseKeyRepository: DatabaseKeyRepository
    ): FireFlowDatabase = runBlocking {
        DatabaseFactory(
            context = applicationContext,
            appConfigProvider = appConfigProvider,
            databaseKeyRepository = databaseKeyRepository
        ).createRoomDatabase(DATABASE_NAME)
    }

    @Singleton
    @Provides
    fun userAccountDao(fireFlowDatabase: FireFlowDatabase): UserAccountDao =
        fireFlowDatabase.userAccountDao()

    @Singleton
    @Provides
    fun userAccountRepository(
        userAccountInMemoryCache: UserAccountInMemoryCache,
        userAccountDatabaseSource: UserAccountDatabaseSource,
        stringsProvider: StringsProvider
    ): UserAccountRepository = UserAccountRepositoryImpl(
        userAccountInMemoryCache = userAccountInMemoryCache,
        userAccountDatabaseSource = userAccountDatabaseSource,
        stringsProvider = stringsProvider
    )
}

@InstallIn(SingletonComponent::class)
@Module
interface DatabaseSingletonBindsModule {

    @Singleton
    @Binds
    fun databaseKeyRepository(
        databaseKeyRepositoryImpl: DatabaseKeyRepositoryImpl
    ): DatabaseKeyRepository
}
