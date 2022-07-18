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

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.core.persistence.data.cache.UserAccountInMemoryCache
import dev.zitech.core.persistence.data.repository.database.UserAccountRepositoryImpl
import dev.zitech.core.persistence.di.DatabaseSingletonProvidesModule
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import dev.zitech.core.persistence.domain.source.database.UserAccountDatabaseSource
import dev.zitech.core.persistence.framework.database.FireFlowDatabase
import dev.zitech.core.persistence.framework.database.dao.UserAccountDao
import dev.zitech.core.persistence.framework.database.mapper.UserAccountMapper
import dev.zitech.core.persistence.framework.database.source.UserAccountDatabaseSourceImpl
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseSingletonProvidesModule::class]
)
@Module
class FakeDatabaseSingletonProvidesModule {

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
    fun database(): FireFlowDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        FireFlowDatabase::class.java
    )
        .allowMainThreadQueries()
        .build()

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
