/*
 * Copyright (C) 2022 Zitech
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

package dev.zitech.fireflow.initializer

import android.content.Context
import androidx.startup.Initializer
import dev.zitech.core.common.framework.logger.AppConfigProvider
import dev.zitech.core.common.framework.logger.Logger

internal class LoggerInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        Logger.init(
            isDebug = AppConfigProvider.isDebugMode()
        )
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
