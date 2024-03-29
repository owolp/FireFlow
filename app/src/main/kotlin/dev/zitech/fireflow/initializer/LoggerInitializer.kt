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
import dev.zitech.fireflow.core.logger.ErrorTree
import dev.zitech.fireflow.core.logger.Logger
import dev.zitech.fireflow.di.InitializerEntryPoint
import javax.inject.Inject

internal class LoggerInitializer : Initializer<Unit> {

    @Inject
    lateinit var errorTree: ErrorTree

    override fun create(context: Context) {
        InitializerEntryPoint.resolve(context).inject(this)

        Logger.init(errorTree)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(CrashReporterInitializer::class.java, DependencyGraphInitializer::class.java)
}
