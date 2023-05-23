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

package dev.zitech.fireflow.core.scope

import dev.zitech.fireflow.core.dispatcher.AppDispatchers
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Interface for managing application-level coroutine scopes.
 */
interface AppScopes {
    val singleton: CoroutineScope

    /**
     * Launches a coroutine in the [singleton] scope.
     *
     * @param coroutineDispatcher The dispatcher for the coroutine. If null, the default dispatcher of [singleton] scope is used.
     * @param func The suspending function to be executed in the [singleton] scope.
     *
     * @return A [Job] object representing the launched coroutine.
     */
    fun singletonLaunch(
        coroutineDispatcher: CoroutineDispatcher? = null,
        func: suspend CoroutineScope.() -> Unit
    ): Job
}

/**
 * Implementation of [AppScopes] interface.
 *
 * @param appDispatchers The [AppDispatchers] instance for specifying the dispatcher for the [singleton] scope.
 */
internal class AppScopesImpl @Inject constructor(
    appDispatchers: AppDispatchers
) : AppScopes {
    override val singleton = CoroutineScope(SupervisorJob() + appDispatchers.default)

    /**
     * Launches a coroutine in the [singleton] scope.
     *
     * @param coroutineDispatcher The dispatcher for the coroutine. If null, the default dispatcher of [singleton] scope is used.
     * @param func The suspending function to be executed in the [singleton] scope.
     *
     * @return A [Job] object representing the launched coroutine.
     */
    override fun singletonLaunch(
        coroutineDispatcher: CoroutineDispatcher?,
        func: suspend CoroutineScope.() -> Unit
    ): Job = if (coroutineDispatcher != null) {
        singleton.launch(coroutineDispatcher) { func() }
    } else {
        singleton.launch { func() }
    }
}
