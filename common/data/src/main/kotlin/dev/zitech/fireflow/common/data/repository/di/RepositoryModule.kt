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

package dev.zitech.fireflow.common.data.repository.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.zitech.fireflow.common.data.memory.cache.InMemoryCache
import dev.zitech.fireflow.common.data.repository.cache.CacheRepositoryImpl
import dev.zitech.fireflow.common.data.repository.configurator.ConfiguratorRepositoryImpl
import dev.zitech.fireflow.common.data.repository.featureflag.FeatureFlagRepositoryImpl
import dev.zitech.fireflow.common.data.repository.profile.FireflyProfileRepositoryImpl
import dev.zitech.fireflow.common.data.repository.reporter.analytics.AnalyticsRepositoryImpl
import dev.zitech.fireflow.common.data.repository.reporter.crash.CrashRepositoryImpl
import dev.zitech.fireflow.common.data.repository.reporter.performance.PerformanceRepositoryImpl
import dev.zitech.fireflow.common.data.repository.token.TokenRepositoryImpl
import dev.zitech.fireflow.common.data.repository.user.NetworkDetails
import dev.zitech.fireflow.common.data.repository.user.NetworkDetailsInMemoryCache
import dev.zitech.fireflow.common.data.repository.user.UserAccountRepositoryImpl
import dev.zitech.fireflow.common.data.source.di.annotation.DevFeatureFlagSource as DevFeatureFlagSourceAnnotation
import dev.zitech.fireflow.common.data.source.di.annotation.ProdFeatureFlagSource as ProdFeatureFlagSourceAnnotation
import dev.zitech.fireflow.common.data.source.di.annotation.RemoteFeatureFlagSource as RemoteFeatureFlagSourceAnnotation
import dev.zitech.fireflow.common.data.source.featureflag.FeatureFlagSource
import dev.zitech.fireflow.common.data.source.user.UserAccountSource
import dev.zitech.fireflow.common.domain.repository.authentication.TokenRepository
import dev.zitech.fireflow.common.domain.repository.cache.CacheRepository
import dev.zitech.fireflow.common.domain.repository.configurator.ConfiguratorRepository
import dev.zitech.fireflow.common.domain.repository.featureflag.FeatureFlagRepository
import dev.zitech.fireflow.common.domain.repository.profile.FireflyProfileRepository
import dev.zitech.fireflow.common.domain.repository.reporter.AnalyticsRepository
import dev.zitech.fireflow.common.domain.repository.reporter.CrashRepository
import dev.zitech.fireflow.common.domain.repository.reporter.PerformanceRepository
import dev.zitech.fireflow.common.domain.repository.user.UserAccountRepository
import dev.zitech.fireflow.core.applicationconfig.AppConfigProvider
import javax.inject.Singleton

internal interface RepositoryModule {

    @InstallIn(SingletonComponent::class)
    @Module
    interface SingletonBinds {

        @Singleton
        @Binds
        fun cacheRepository(
            cacheRepositoryImpl: CacheRepositoryImpl
        ): CacheRepository

        @Singleton
        @Binds
        fun fireflyProfileRepository(
            fireflyProfileRepositoryImpl: FireflyProfileRepositoryImpl
        ): FireflyProfileRepository

        @Singleton
        @Binds
        fun configuratorRepository(
            configuratorRepositoryImpl: ConfiguratorRepositoryImpl
        ): ConfiguratorRepository

        @Singleton
        @Binds
        fun analyticsRepository(
            analyticsRepositoryImpl: AnalyticsRepositoryImpl
        ): AnalyticsRepository

        @Singleton
        @Binds
        fun crashRepository(
            crashRepositoryImpl: CrashRepositoryImpl
        ): CrashRepository

        @Singleton
        @Binds
        fun performanceRepository(
            performanceRepositoryImpl: PerformanceRepositoryImpl
        ): PerformanceRepository

        @Singleton
        @Binds
        fun networkDetailsInMemoryCache(
            networkDetailsInMemoryCache: NetworkDetailsInMemoryCache
        ): InMemoryCache<NetworkDetails>

        @Singleton
        @Binds
        fun tokenRepository(
            tokenRepositoryImpl: TokenRepositoryImpl
        ): TokenRepository
    }

    @InstallIn(SingletonComponent::class)
    @Module
    object SingletonProvides {

        @Singleton
        @Provides
        fun featureFlagRepository(
            appConfigProvider: AppConfigProvider,
            @DevFeatureFlagSourceAnnotation devFeatureFlagSource: FeatureFlagSource,
            @ProdFeatureFlagSourceAnnotation prodFeatureFlagSource: FeatureFlagSource,
            @RemoteFeatureFlagSourceAnnotation remoteFeatureFlagSource: FeatureFlagSource
        ): FeatureFlagRepository = FeatureFlagRepositoryImpl(
            appConfigProvider,
            devFeatureFlagSource,
            prodFeatureFlagSource,
            remoteFeatureFlagSource
        )

        @Singleton
        @Provides
        fun userAccountRepository(
            networkDetailsInMemoryCache: InMemoryCache<NetworkDetails>,
            userAccountDatabaseSource: UserAccountSource
        ): UserAccountRepository = UserAccountRepositoryImpl(
            networkDetailsInMemoryCache,
            userAccountDatabaseSource
        )
    }
}
