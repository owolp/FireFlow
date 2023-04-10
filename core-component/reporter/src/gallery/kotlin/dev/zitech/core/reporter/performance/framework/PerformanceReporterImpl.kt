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

package dev.zitech.core.reporter.performance.framework

import com.huawei.agconnect.apms.APMS
import dev.zitech.core.reporter.performance.domain.reporter.PerformanceReporter
import javax.inject.Inject

@Deprecated("Modules")
internal class PerformanceReporterImpl @Inject constructor() : PerformanceReporter {

    override fun setCollectionEnabled(enabled: Boolean) {
        APMS.getInstance().enableCollection(enabled)
    }
}
