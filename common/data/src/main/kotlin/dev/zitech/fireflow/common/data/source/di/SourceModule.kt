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

package dev.zitech.fireflow.common.data.source.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zitech.fireflow.common.data.source.configurator.ConfiguratorProviderSource
import dev.zitech.fireflow.common.data.source.configurator.ConfiguratorProviderSourceImpl
import dev.zitech.fireflow.common.data.source.di.annotation.DevFeatureFlagSource as DevFeatureFlagSourceAnnotation
import dev.zitech.fireflow.common.data.source.di.annotation.DevelopmentPreferencesDataSource
import dev.zitech.fireflow.common.data.source.di.annotation.ProdFeatureFlagSource as ProdFeatureFlagSourceAnnotation
import dev.zitech.fireflow.common.data.source.di.annotation.RemoteFeatureFlagSource as RemoteFeatureFlagSourceAnnotation
import dev.zitech.fireflow.common.data.source.di.annotation.SecuredPreferencesDataSource
import dev.zitech.fireflow.common.data.source.di.annotation.StandardPreferencesDataSource
import dev.zitech.fireflow.common.data.source.featureflag.DevFeatureFlagSource
import dev.zitech.fireflow.common.data.source.featureflag.FeatureFlagSource
import dev.zitech.fireflow.common.data.source.featureflag.ProdFeatureFlagSource
import dev.zitech.fireflow.common.data.source.featureflag.RemoteFeatureFlagSource
import dev.zitech.fireflow.common.data.source.preferences.PreferencesDataSource
import dev.zitech.fireflow.common.data.source.preferences.PreferencesFactory
import dev.zitech.fireflow.common.data.source.user.UserAccountDatabaseSource
import dev.zitech.fireflow.common.data.source.user.UserAccountSource
import dev.zitech.fireflow.common.domain.model.preferences.PreferenceType
import dev.zitech.fireflow.core.dispatcher.AppDispatchers
import javax.inject.Singleton

internal interface SourceModule {

    @InstallIn(SingletonComponent::class)
    @Module
    interface SingletonBinds {

        @Singleton
        @Binds
        fun configuratorProviderSource(configuratorProviderSourceImpl: ConfiguratorProviderSourceImpl): ConfiguratorProviderSource

        @ProdFeatureFlagSourceAnnotation
        @Singleton
        @Binds
        fun prodFeatureFlagSource(
            prodFeatureFlagSource: ProdFeatureFlagSource
        ): FeatureFlagSource

        @RemoteFeatureFlagSourceAnnotation
        @Singleton
        @Binds
        fun remoteFeatureFlagSource(
            remoteFeatureFlagSource: RemoteFeatureFlagSource
        ): FeatureFlagSource

        @Singleton
        @Binds
        fun userAccountDatabaseSource(
            userAccountDatabaseSource: UserAccountDatabaseSource
        ): UserAccountSource
    }

    @InstallIn(SingletonComponent::class)
    @Module
    object SingletonProvides {

        @DevFeatureFlagSourceAnnotation
        @Singleton
        @Provides
        fun devFeatureFlagSource(
            @DevelopmentPreferencesDataSource developmentPreferencesDataSource: PreferencesDataSource
        ): FeatureFlagSource = DevFeatureFlagSource(
            developmentPreferencesDataSource
        )

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
    }
}
