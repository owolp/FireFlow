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

package dev.zitech.fireflow.initializer

import android.content.Context
import androidx.startup.Initializer
import dev.zitech.core.common.domain.scope.AppScopes
import dev.zitech.core.crashreporter.domain.usecase.InitializeCrashReporterUseCase
import dev.zitech.core.crashreporter.domain.usecase.SetCrashReporterCollectionUseCase
import dev.zitech.core.persistence.domain.usecase.preferences.GetCrashReporterCollectionValueUseCase
import dev.zitech.fireflow.di.InitializerEntryPoint
import javax.inject.Inject

internal class CrashReporterInitializer : Initializer<Unit> {

    @Inject
    lateinit var appScopes: AppScopes

    @Inject
    lateinit var initializeCrashReporterUseCase: InitializeCrashReporterUseCase

    @Inject
    lateinit var setCrashReporterCollectionUseCase: SetCrashReporterCollectionUseCase

    @Inject
    lateinit var getCrashReporterCollectionValueUseCase: GetCrashReporterCollectionValueUseCase

    override fun create(context: Context) {
        InitializerEntryPoint.resolve(context).inject(this)

        appScopes.singletonLaunch {
            initializeCrashReporterUseCase()
            setCrashReporterCollectionUseCase(
                getCrashReporterCollectionValueUseCase()
            )
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(DependencyGraphInitializer::class.java)
}
