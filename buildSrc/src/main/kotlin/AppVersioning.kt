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

object AppVersioning {

    private const val MAJOR = 0
    private const val MINOR = 0
    private const val PATCH = 1

    const val COMPILE_SDK = 32
    const val MIN_SDK = 29
    const val TARGET_SDK = 32

    private const val LOCAL_BUILD_VERSION_CODE = 424242

    fun retrieveVersionName(): String =
        listOf(MAJOR, MINOR, PATCH).joinToString(separator = ".")

    fun retrieveVersionCode(): Int =
        System.getenv("GITHUB_RUN_NUMBER")?.toInt() ?: LOCAL_BUILD_VERSION_CODE
}
