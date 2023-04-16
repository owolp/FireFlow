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

package dev.zitech.fireflow.common.data.local.database.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zitech.fireflow.common.data.local.database.common.CommonDatabase
import dev.zitech.fireflow.common.data.local.database.common.dao.UserAccountDao
import dev.zitech.fireflow.common.data.local.database.factory.DatabaseFactory
import dev.zitech.fireflow.common.data.local.database.factory.FireFlowDatabase
import dev.zitech.fireflow.common.data.source.preferences.PreferencesDataSource
import dev.zitech.fireflow.core.concurrency.SingleRunner
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking

internal interface DatabaseModule {

    @InstallIn(SingletonComponent::class)
    @Module
    object SingletonProvides {

        @Singleton
        @Provides
        fun commonDatabase(
            databaseFactory: DatabaseFactory
        ): CommonDatabase = runBlocking {
            databaseFactory.createDatabase(FireFlowDatabase.Common)
        }

        @Singleton
        @Provides
        fun databaseFactory(
            @ApplicationContext applicationContext: Context,
            @dev.zitech.fireflow.common.data.source.di.annotation.SecuredPreferencesDataSource
            securedPreferencesDataSource: PreferencesDataSource
        ): DatabaseFactory = DatabaseFactory(
            applicationContext,
            securedPreferencesDataSource,
            SingleRunner()
        )

        @Singleton
        @Provides
        fun userAccountDao(commonDatabase: CommonDatabase): UserAccountDao =
            commonDatabase.userAccountDao()
    }
}
