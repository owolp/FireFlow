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

package dev.zitech.core.common.framework.logger

import android.util.Log
import dev.zitech.core.common.domain.logger.ErrorTree
import dev.zitech.core.crashreporter.domain.usecase.LogDebugInfoUseCase
import dev.zitech.core.crashreporter.domain.usecase.RecordCrashExceptionUseCase
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException
import kotlin.coroutines.cancellation.CancellationException
import timber.log.Timber

class ErrorTreeImpl @Inject constructor(
    private val logDebugInfoUseCase: LogDebugInfoUseCase,
    private val recordCrashExceptionUseCase: RecordCrashExceptionUseCase
) : ErrorTree {

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    override operator fun invoke(): Timber.Tree =
        object : Timber.Tree() {

            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                var tagValue = tag
                var messageValue = message
                var throwableValue = t

                if (priority < Log.INFO) return

                val stackTraceFilters = arrayOf(
                    ErrorTree::class.java, Timber::class.java
                )

                val logStackTrace = Throwable().stackTrace.filter(stackTraceFilters)

                if (tagValue == null || tagValue.isEmpty()) {
                    tagValue =
                        logStackTrace
                        .extractCallerClassName() + ":" + logStackTrace
                        .extractCallerLineNumber()
                }

                if (!messageValue.contains("#")) {
                    messageValue = logStackTrace.extractCallerMethodName() + "() - " + message
                }

                val priorityTag = when (priority) {
                    Log.INFO -> "I"
                    Log.WARN -> "W"
                    Log.ERROR -> "E"
                    Log.ASSERT -> "A"
                    else -> priority
                }

                logDebugInfoUseCase("$priorityTag/$tagValue $messageValue")

                if (priority == Log.ERROR && throwableValue == null) {
                    throwableValue = Exception(message)
                    throwableValue
                        .setStackTrace(
                            throwableValue.getStackTrace()
                                .filter(stackTraceFilters)
                        )
                }

                if (throwableValue != null && shouldLog(throwableValue)) {
                    recordCrashExceptionUseCase(throwableValue)
                }
            }
        }

    private fun shouldLog(throwable: Throwable): Boolean =
        when (throwable) {
            is UnknownHostException,
            is SocketException,
            is SocketTimeoutException,
            is SSLHandshakeException,
            is SSLException,
            is IOException,
            is InterruptedException,
            is CancellationException -> false
            else -> true
        }
}
