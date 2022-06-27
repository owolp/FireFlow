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

package dev.zitech.core.reporter.crash.framework

import dev.zitech.core.common.domain.logger.ErrorTree
import timber.log.Timber
import javax.inject.Inject

class ErrorTreeImpl @Inject constructor() : ErrorTree {

    override operator fun invoke(): Timber.Tree =
        object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement) =
                "(${element.fileName}:${element.lineNumber})#${element.methodName}"
        }
}
