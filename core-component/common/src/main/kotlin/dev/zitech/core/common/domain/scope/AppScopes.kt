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

package dev.zitech.core.common.domain.scope

import dev.zitech.core.common.domain.dispatcher.AppDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AppScopes {
    val singleton: CoroutineScope
    fun singletonLaunch(func: suspend CoroutineScope.() -> Unit)
}

internal class AppScopesImpl @Inject constructor(
    appDispatchers: AppDispatchers
) : AppScopes {
    override val singleton = CoroutineScope(SupervisorJob() + appDispatchers.default)

    override fun singletonLaunch(func: suspend CoroutineScope.() -> Unit) {
        singleton.launch { func() }
    }
}
