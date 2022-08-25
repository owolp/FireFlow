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

import java.util.regex.Pattern

private const val CALLER_STACK_INDEX = 0

typealias StackTrace = Array<StackTraceElement?>

internal fun StackTrace.extractCallerClassName(): String? {
    return if (this.size > CALLER_STACK_INDEX) {
        this[CALLER_STACK_INDEX]?.extractClassName()
    } else {
        null
    }
}

/**
 * Extract the class name without any anonymous class suffixes and prefixes
 * (e.g., {@code package.name.Foo$1test2test3test}
 * becomes {@code Foo}).
 */
private fun StackTraceElement.extractClassName(): String {
    this.className.removeLambdaSuffix().apply {
        return removePackagePrefix()
    }
}

/**
 * Removes the class name without any package name (e.g., {@code package.name.Foo$2}
 * becomes {@code Foo$2}).
 */
private fun String.removePackagePrefix() =
    substring(this.lastIndexOf('.') + 1)

/**
 * Removes the class name without any anonymous lambda class suffixes (e.g.,
 * {@code package.name.Foo$123} becomes {@code package.name.Foo}, but
 * {@code package.name.Foo$test123} remains {@code package.name.Foo$test123}).
 */
private fun String.removeLambdaSuffix(): String {
    val anonymousClass = Pattern.compile("(\\\$\\d+)+\$")
    val m = anonymousClass.matcher(this)
    if (m.find()) {
        return m.replaceAll("")
    }

    return this
}

internal fun StackTrace.extractCallerMethodName(): String? {
    return if (this.size > CALLER_STACK_INDEX) {
        this[CALLER_STACK_INDEX]?.methodName
    } else {
        null
    }
}

internal fun StackTrace.extractCallerLineNumber(): Int {
    return if (this.size > CALLER_STACK_INDEX) {
        this[CALLER_STACK_INDEX]?.lineNumber ?: -1
    } else {
        -1
    }
}

/**
 * Filters out topClassNames from the top of the stacktrace
 *
 * @param topClassNames top of stacktrace classes to remove
 * @return the filtered stacktrace
 */
@Suppress("LoopWithTooManyJumpStatements")
internal fun StackTrace.filter(topClassNames: Array<Class<out Any>>): Array<StackTraceElement?> {
    var index = 0
    for (element in this) {
        if (element?.className != null) {
            if (topClassNames.any {
                element.className.removeLambdaSuffix().startsWith(it.name.removeLambdaSuffix())
            }
            ) {
                index++
            } else {
                break
            }
        } else {
            break
        }
    }
    // remove the filter related elements
    if (index > 0 && this.size > index) {
        val stackTraceNew = arrayOfNulls<StackTraceElement>(this.size - index)
        System.arraycopy(this, index, stackTraceNew, 0, stackTraceNew.size)
        return stackTraceNew
    }

    return this
}
