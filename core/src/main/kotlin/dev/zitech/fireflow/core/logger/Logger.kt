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

package dev.zitech.fireflow.core.logger

import timber.log.Timber

private const val TIMBER_MAX_TAG_LENGTH = 23

@Suppress("TooManyFunctions")
object Logger {

    /**
     * Initializes the logger with the provided [errorTree].
     *
     * @param errorTree The error tree to be used for logging.
     */
    fun init(errorTree: ErrorTree) {
        Timber.plant(errorTree())
    }

    /**
     * Logs a debug message with the given [tag] and [message].
     *
     * @param tag The tag to be used for logging.
     * @param message The message to be logged.
     */
    fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    /**
     * Logs a debug message with the given [tag], [throwable], and [message].
     *
     * @param tag The tag to be used for logging.
     * @param throwable The throwable to be logged.
     * @param message The message to be logged.
     */
    fun d(tag: String, throwable: Throwable?, message: String) {
        Timber.tag(tag).d(throwable, message)
    }

    /**
     * Logs an error message with the given [tag] and [message].
     *
     * @param tag The tag to be used for logging.
     * @param message The message to be logged.
     */
    fun e(tag: String, message: String) {
        Timber.tag(tag).e(message)
    }

    /**
     * Logs an error message with the given [tag], [throwable], and [message].
     *
     * @param tag The tag to be used for logging.
     * @param throwable The throwable to be logged.
     * @param message The message to be logged.
     */
    fun e(tag: String, throwable: Throwable?, message: String) {
        Timber.tag(tag).e(throwable, message)
    }

    /**
     * Logs an error message with the given [tag] and [throwable].
     *
     * @param tag The tag to be used for logging.
     * @param throwable The throwable to be logged.
     */
    fun e(tag: String, throwable: Throwable?) {
        Timber.tag(tag).e(throwable)
    }

    /**
     * Logs an error message with the given [tag], [exception], and [message].
     *
     * @param tag The tag to be used for logging.
     * @param exception The exception to be logged.
     * @param message The message to be logged.
     */
    fun e(tag: String, exception: Exception?, message: String) {
        Timber.tag(tag).e(exception, message)
    }

    /**
     * Logs an error message with the given [tag] and [exception].
     *
     * @param tag The tag to be used for logging.
     * @param exception The exception to be logged.
     */
    fun e(tag: String, exception: Exception?) {
        Timber.tag(tag).e(exception)
    }

    /**
     * Logs an info message with the given [tag] and [message].
     *
     * @param tag The tag to be used for logging.
     * @param message The message to be logged.
     */
    fun i(tag: String, message: String) {
        Timber.tag(tag).i(message)
    }

    /**
     * Logs an info message with the given [tag], [throwable], and [message].
     *
     * @param tag The tag to be used for logging.
     * @param throwable The throwable to be logged.
     * @param message The message to be logged.
     */
    fun i(tag: String, throwable: Throwable?, message: String) {
        Timber.tag(tag).i(throwable, message)
    }

    /**
     * Logs a warning message with the given [tag] and [message].
     *
     * @param tag The tag to be used for logging.
     * @param message The message to be logged.
     */
    fun w(tag: String, message: String) {
        Timber.tag(tag).w(message)
    }

    /**
     * Logs a warning message with the given [tag], [throwable], and [message].
     *
     * @param tag The tag to be used for logging.
     * @param throwable The throwable to be logged.
     * @param message The message to be logged.
     */
    fun w(tag: String, throwable: Throwable?, message: String) {
        Timber.tag(tag).w(throwable, message)
    }

    /**
     * Generates a tag string for the given [clazz].
     *
     * If the simple name of the class exceeds the [TIMBER_MAX_TAG_LENGTH], it will be truncated.
     *
     * @param clazz The class to generate the tag from.
     * @return The generated tag string.
     */
    fun tag(clazz: Class<*>): String {
        val simpleName = clazz.simpleName
        return if (simpleName.length > TIMBER_MAX_TAG_LENGTH) {
            simpleName.substring(0, TIMBER_MAX_TAG_LENGTH)
        } else {
            simpleName
        }
    }
}
