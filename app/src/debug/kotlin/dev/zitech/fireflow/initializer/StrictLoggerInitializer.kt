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
import dev.zitech.fireflow.common.domain.model.featureflag.DevFeature
import dev.zitech.fireflow.common.domain.usecase.featureflag.IsFeatureEnabledUseCase
import dev.zitech.fireflow.core.scope.AppScopes
import dev.zitech.fireflow.di.DebugInitializerEntryPoint
import dev.zitech.fireflow.framework.performance.StrictLogger
import javax.inject.Inject
import kotlinx.coroutines.launch

internal class StrictLoggerInitializer : Initializer<Unit> {

    @Inject
    lateinit var appScopes: AppScopes

    @Inject
    lateinit var isFeatureEnabledUseCase: IsFeatureEnabledUseCase

    override fun create(context: Context) {
        DebugInitializerEntryPoint.resolve(context).inject(this)

        appScopes.singleton.launch {
            if (isFeatureEnabledUseCase(DevFeature.STRICT_MODE)) {
                StrictLogger.init()
            }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(DependencyGraphInitializer::class.java)
}
