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
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

internal class CrashReporterImpl @Inject constructor(
    @Suppress("UnusedPrivateMember") private val application: Application
) : CrashReporter {

    private val firebaseCrashlytics = Firebase.crashlytics

    override fun init() {
        // NO_OP
    }

    override fun log(message: String) {
        firebaseCrashlytics.log(message)
    }

    override fun recordException(throwable: Throwable) {
        firebaseCrashlytics.recordException(throwable)
    }

    override fun setCollectionEnabled(enabled: Boolean) {
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(enabled)
    }

    override fun setCustomKey(key: String, value: Any) {
        if (value is Boolean) {
            firebaseCrashlytics.setCustomKey(key, value)
        }
        if (value is Double) {
            firebaseCrashlytics.setCustomKey(key, value)
        }
        if (value is Float) {
            firebaseCrashlytics.setCustomKey(key, value)
        }
        if (value is Int) {
            firebaseCrashlytics.setCustomKey(key, value)
        }
        if (value is Long) {
            firebaseCrashlytics.setCustomKey(key, value)
        }
        if (value is String) {
            firebaseCrashlytics.setCustomKey(key, value)
        }
    }
}
