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

package dev.zitech.fireflow.common.data.reporter.crash

/**
 * Interface for reporting application crashes and managing crash reporting settings.
 */
interface CrashReporter {

    /**
     * Initializes the crash reporter.
     */
    fun init()

    /**
     * Logs a message to the crash reporter.
     *
     * @param message The message to be logged.
     */
    fun log(message: String)

    /**
     * Records an exception in the crash reporter.
     *
     * @param throwable The throwable representing the exception.
     */
    fun recordException(throwable: Throwable)

    /**
     * Sets whether crash reporting is enabled or disabled.
     *
     * @param enabled `true` to enable crash reporting, `false` to disable it.
     */
    fun setCollectionEnabled(enabled: Boolean)

    /**
     * Sets a custom key-value pair for crash reporting.
     *
     * @param key The key of the custom data.
     * @param value The value of the custom data.
     */
    fun setCustomKey(key: String, value: Any)
}
