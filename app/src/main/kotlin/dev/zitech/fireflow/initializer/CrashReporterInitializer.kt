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

package dev.zitech.fireflow.initializer

import android.content.Context
import androidx.startup.Initializer
import dev.zitech.core.reporter.crash.domain.usecase.SetCrashReporterCollectionUseCase
import dev.zitech.fireflow.core.scope.AppScopes
import dev.zitech.fireflow.di.InitializerEntryPoint
import javax.inject.Inject

internal class CrashReporterInitializer : Initializer<Unit> {

    @Inject
    lateinit var appScopes: AppScopes

    @Inject
    lateinit var setCrashReporterCollectionUseCase: SetCrashReporterCollectionUseCase

    override fun create(context: Context) {
        InitializerEntryPoint.resolve(context).inject(this)

        appScopes.singletonLaunch {
            setCrashReporterCollectionUseCase()
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(DependencyGraphInitializer::class.java)
}
