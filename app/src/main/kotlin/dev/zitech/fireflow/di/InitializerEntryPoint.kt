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

package dev.zitech.fireflow.di

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import dev.zitech.fireflow.initializer.AnalyticsReporterInitializer
import dev.zitech.fireflow.initializer.CrashReporterInitializer
import dev.zitech.fireflow.initializer.FeatureFlagInitializer
import dev.zitech.fireflow.initializer.LoggerInitializer
import dev.zitech.fireflow.initializer.PerformanceReporterInitializer

@InstallIn(SingletonComponent::class)
@EntryPoint
internal interface InitializerEntryPoint {

    companion object {
        @Throws(IllegalStateException::class)
        fun resolve(context: Context): InitializerEntryPoint {
            val applicationContext = context.applicationContext
                ?: throw IllegalStateException("Application Context not found!")

            return EntryPointAccessors.fromApplication(
                context = applicationContext,
                entryPoint = InitializerEntryPoint::class.java
            )
        }
    }

    fun inject(analyticsReporterInitializer: AnalyticsReporterInitializer)
    fun inject(crashReporterInitializer: CrashReporterInitializer)
    fun inject(featureFlagInitializer: FeatureFlagInitializer)
    fun inject(loggerInitializer: LoggerInitializer)
    fun inject(performanceReporterInitializer: PerformanceReporterInitializer)
}
