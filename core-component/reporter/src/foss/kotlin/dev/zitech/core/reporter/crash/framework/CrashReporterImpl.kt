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

import android.app.Application
import dev.zitech.core.reporter.crash.domain.reporter.CrashReporter
import javax.inject.Inject
import org.acra.ACRA
import org.acra.ACRA.init
import org.acra.ReportField
import org.acra.config.CoreConfigurationBuilder
import org.acra.config.HttpSenderConfigurationBuilder
import org.acra.data.StringFormat

internal class CrashReporterImpl @Inject constructor(
    private val application: Application
) : CrashReporter {

    private val errorReporter = ACRA.errorReporter

    override fun init() {
        val builder = CoreConfigurationBuilder().apply {
            withReportFormat(StringFormat.JSON)
            reportContent = listOf(
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.PHONE_MODEL,
                ReportField.ANDROID_VERSION,
                ReportField.BRAND,
                ReportField.PRODUCT,
                ReportField.TOTAL_MEM_SIZE,
                ReportField.AVAILABLE_MEM_SIZE,
                ReportField.CUSTOM_DATA,
                ReportField.STACK_TRACE,
                ReportField.INITIAL_CONFIGURATION,
                ReportField.CRASH_CONFIGURATION,
                ReportField.DISPLAY,
                ReportField.USER_COMMENT,
                ReportField.USER_APP_START_DATE,
                ReportField.USER_CRASH_DATE,
                ReportField.USER_EMAIL
            )
            pluginConfigurations = listOf(
                HttpSenderConfigurationBuilder()
                    .withUri("")
                    .build()
            )
        }

        init(application, builder)
    }

    override fun log(message: String) {
        setCustomKey("Event at ${System.currentTimeMillis()}", message)
    }

    override fun recordException(throwable: Throwable) {
        errorReporter.handleSilentException(throwable)
    }

    override fun setCustomKey(key: String, value: Any) {
        errorReporter.putCustomData(key, value.toString())
    }

    override fun setCollectionEnabled(enabled: Boolean) {
        errorReporter.setEnabled(enabled)
    }
}
