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

package dev.zitech.fireflow.core.dispatcher

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Provides dispatchers for coroutines in the application.
 */
interface AppDispatchers {

    /**
     * The default dispatcher for CPU-intensive operations.
     */
    val default: CoroutineDispatcher

    /**
     * The main dispatcher for UI-related operations.
     */
    val main: CoroutineDispatcher

    /**
     * The dispatcher for IO operations.
     */
    val io: CoroutineDispatcher
}

/**
 * Implementation of the [AppDispatchers] interface.
 *
 * @property default The default dispatcher.
 * @property main The main dispatcher.
 * @property io The IO dispatcher.
 */
internal class AppDispatchersImpl @Inject constructor() : AppDispatchers {

    override val default: CoroutineDispatcher = Dispatchers.Default
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
}
