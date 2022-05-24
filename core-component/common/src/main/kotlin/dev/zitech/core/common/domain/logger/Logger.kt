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

package dev.zitech.core.common.domain.logger

import timber.log.Timber

@Suppress("TooManyFunctions")
object Logger {

    fun init(errorTree: ErrorTree) {
        Timber.plant(errorTree())
    }

    fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    fun d(tag: String, throwable: Throwable?, message: String) {
        Timber.tag(tag).d(throwable, message)
    }

    fun e(tag: String, message: String) {
        Timber.tag(tag).e(message)
    }

    fun e(tag: String, throwable: Throwable?, message: String) {
        Timber.tag(tag).e(throwable, message)
    }

    fun e(tag: String, throwable: Throwable?) {
        Timber.tag(tag).e(throwable)
    }

    fun e(tag: String, exception: Exception?, message: String) {
        Timber.tag(tag).e(exception, message)
    }

    fun e(tag: String, exception: Exception?) {
        Timber.tag(tag).e(exception)
    }

    fun i(tag: String, message: String) {
        Timber.tag(tag).i(message)
    }

    fun i(tag: String, throwable: Throwable?, message: String) {
        Timber.tag(tag).i(throwable, message)
    }

    fun w(tag: String, message: String) {
        Timber.tag(tag).w(message)
    }

    fun w(tag: String, throwable: Throwable?, message: String) {
        Timber.tag(tag).w(throwable, message)
    }
}
