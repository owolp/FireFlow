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

package dev.zitech.core.common.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zitech.core.common.domain.applicationconfig.AppConfigProvider
import dev.zitech.core.common.domain.dispatcher.AppDispatchers
import dev.zitech.core.common.domain.dispatcher.AppDispatchersImpl
import dev.zitech.core.common.domain.scope.AppScopes
import dev.zitech.core.common.domain.scope.AppScopesImpl
import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.core.common.framework.applicationconfig.AppConfigProviderImpl
import dev.zitech.core.common.framework.strings.StringsProviderImpl
import javax.inject.Singleton

internal interface CommonModule {

    @InstallIn(SingletonComponent::class)
    @Module
    object CommonSingletonProvidesModule {

        @Singleton
        @Provides
        fun stringsProvider(@ApplicationContext applicationContext: Context): StringsProvider =
            StringsProviderImpl(applicationContext)
    }

    @InstallIn(SingletonComponent::class)
    @Module
    interface CommonSingletonBindsModule {

        @Singleton
        @Binds
        fun appDispatchers(appDispatchersImpl: AppDispatchersImpl): AppDispatchers

        @Singleton
        @Binds
        fun appScopes(appScopesImpl: AppScopesImpl): AppScopes

        @Singleton
        @Binds
        fun appConfigProvider(appConfigProviderImpl: AppConfigProviderImpl): AppConfigProvider
    }
}
