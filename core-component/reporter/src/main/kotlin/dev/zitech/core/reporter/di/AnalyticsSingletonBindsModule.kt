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
import dev.zitech.core.common.domain.logger.ErrorTree
import dev.zitech.core.reporter.analytics.data.repository.AnalyticsRepositoryImpl
import dev.zitech.core.reporter.analytics.domain.reporter.AnalyticsReporter
import dev.zitech.core.reporter.analytics.domain.repository.AnalyticsRepository
import dev.zitech.core.reporter.analytics.framework.AnalyticsReporterImpl
import dev.zitech.core.reporter.crash.data.repository.CrashRepositoryImpl
import dev.zitech.core.reporter.crash.domain.reporter.CrashReporter
import dev.zitech.core.reporter.crash.domain.repository.CrashRepository
import dev.zitech.core.reporter.crash.framework.CrashReporterImpl
import dev.zitech.core.reporter.crash.framework.ErrorTreeImpl
import dev.zitech.core.reporter.performance.data.repository.PerformanceRepositoryImpl
import dev.zitech.core.reporter.performance.domain.reporter.PerformanceReporter
import dev.zitech.core.reporter.performance.domain.repository.PerformanceRepository
import dev.zitech.core.reporter.performance.framework.PerformanceReporterImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface AnalyticsSingletonBindsModule {

    @Singleton
    @Binds
    fun crashReporter(crashReporterImpl: CrashReporterImpl): CrashReporter

    @Singleton
    @Binds
    fun performanceReporter(performanceRepositoryImpl: PerformanceReporterImpl): PerformanceReporter

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
    fun errorTree(errorTreeImpl: ErrorTreeImpl): ErrorTree
}

@InstallIn(SingletonComponent::class)
@Module
internal object AnalyticsSingletonProvidesModule {

    @Singleton
    @Provides
    fun analyticsReporter(
        @ApplicationContext context: Context
    ): AnalyticsReporter = AnalyticsReporterImpl(
        context = context
    )
}
