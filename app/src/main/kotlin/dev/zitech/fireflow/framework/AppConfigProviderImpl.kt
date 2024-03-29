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

package dev.zitech.fireflow.framework

import dev.zitech.fireflow.BuildConfig
import dev.zitech.fireflow.core.applicationconfig.AppConfigProvider
import dev.zitech.fireflow.core.applicationconfig.BuildFlavor
import dev.zitech.fireflow.core.applicationconfig.BuildMode
import javax.inject.Inject

internal class AppConfigProviderImpl @Inject constructor() : AppConfigProvider {

    override val buildMode: BuildMode =
        if (BuildConfig.DEBUG) {
            BuildMode.DEBUG
        } else {
            BuildMode.RELEASE
        }

    override val buildFlavor: BuildFlavor =
        when (BuildConfig.FLAVOR) {
            "dev" -> BuildFlavor.DEV
            "foss" -> BuildFlavor.FOSS
            "gallery" -> BuildFlavor.GALLERY
            "play" -> BuildFlavor.PLAY
            else -> throw IllegalStateException("Build Config not found!")
        }

    override val version: String
        get() = BuildConfig.VERSION_NAME
}
