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
import com.huawei.agconnect.crash.AGConnectCrash
import javax.inject.Inject

internal class CrashReporterImpl @Inject constructor(
    @Suppress("UnusedPrivateMember") private val application: Application
) : CrashReporter {

    private val agConnectCrash = AGConnectCrash.getInstance()

    override fun init() {
        // NO_OP
    }

    override fun log(message: String) {
        agConnectCrash.log(message)
    }

    override fun recordException(throwable: Throwable) {
        agConnectCrash.recordException(throwable)
    }

    override fun setCollectionEnabled(enabled: Boolean) {
        agConnectCrash.enableCrashCollection(enabled)
    }

    override fun setCustomKey(key: String, value: Any) {
        if (value is Boolean) {
            agConnectCrash.setCustomKey(key, value)
        }
        if (value is Double) {
            agConnectCrash.setCustomKey(key, value)
        }
        if (value is Float) {
            agConnectCrash.setCustomKey(key, value)
        }
        if (value is Int) {
            agConnectCrash.setCustomKey(key, value)
        }
        if (value is Long) {
            agConnectCrash.setCustomKey(key, value)
        }
        if (value is String) {
            agConnectCrash.setCustomKey(key, value)
        }
    }
}
