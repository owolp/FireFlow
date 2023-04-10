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

package dev.zitech.fireflow.common.data.repository.crash

import dev.zitech.fireflow.common.data.remote.reporter.crash.CrashReporter
import dev.zitech.fireflow.common.domain.repository.crash.CrashRepository
import dev.zitech.fireflow.core.applicationconfig.AppConfigProvider
import dev.zitech.fireflow.core.applicationconfig.BuildMode
import javax.inject.Inject

internal class CrashRepositoryImpl @Inject constructor(
    private val appConfigProvider: AppConfigProvider,
    private val crashReporter: CrashReporter
) : CrashRepository {

    override fun init() {
        crashReporter.init()
    }

    override fun log(message: String) {
        crashReporter.log(message)
    }

    override fun recordException(throwable: Throwable) {
        crashReporter.recordException(throwable)
    }

    override fun setCustomKey(key: String, value: Any) {
        crashReporter.setCustomKey(key, value)
    }

    override fun setCollectionEnabled(enabled: Boolean) =
        crashReporter.setCollectionEnabled(
            when (appConfigProvider.buildMode) {
                BuildMode.RELEASE -> enabled
                BuildMode.DEBUG -> false
            }
        )
}
