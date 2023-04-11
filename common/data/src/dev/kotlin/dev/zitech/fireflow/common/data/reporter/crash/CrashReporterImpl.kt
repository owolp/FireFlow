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

import android.app.Application
import javax.inject.Inject

@Suppress("UnusedPrivateMember", "UNUSED_PARAMETER")
internal class CrashReporterImpl @Inject constructor(
    private val application: Application
) : CrashReporter {

    override fun init() {
        // NO_OP
    }

    override fun log(message: String) {
        // NO_OP
    }

    override fun recordException(throwable: Throwable) {
        // NO_OP
    }

    override fun setCollectionEnabled(enabled: Boolean) {
        // NO_OP
    }

    override fun setCustomKey(key: String, value: Any) {
        // NO_OP
    }
}
