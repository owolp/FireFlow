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

package dev.zitech.analytics.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zitech.analytics.data.repository.AnalyticsRepositoryImpl
import dev.zitech.analytics.domain.repository.AnalyticsRepository
import dev.zitech.analytics.framework.analytics.RemoteAnalytics
import dev.zitech.analytics.framework.analytics.RemoteAnalyticsImpl
import dev.zitech.analytics.framework.source.AnalyticsProviderSource
import dev.zitech.analytics.framework.source.AnalyticsProviderSourceImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface AnalyticsSingletonBindsModule {

    @Singleton
    @Binds
    fun analyticsProviderSource(
        analyticsProviderSourceImpl: AnalyticsProviderSourceImpl
    ): AnalyticsProviderSource

    @Singleton
    @Binds
    fun remoteAnalytics(remoteAnalyticsImpl: RemoteAnalyticsImpl): RemoteAnalytics

    @Singleton
    @Binds
    fun analyticsRepository(
        analyticsRepositoryImpl: AnalyticsRepositoryImpl
    ): AnalyticsRepository
}

@InstallIn(SingletonComponent::class)
@Module
internal object AnalyticsSingletonProvidesModule {

    @Singleton
    @Provides
    fun remoteAnalytics(
        @ApplicationContext context: Context
    ) = RemoteAnalyticsImpl(
        context = context
    )
}
