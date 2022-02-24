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

plugins.apply(plugin.BuildPlugins.GRADLE_VERSION_PLUGIN)
plugins.apply(plugin.BuildPlugins.KTLINT)

buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(dependency.Dependencies.Android.Tools.Build.GRADLE)
        classpath(dependency.Dependencies.Jetbrains.Kotlin.KOTLIN_GRADLE_PLUGIN)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val clean by tasks.creating(Delete::class) {
    delete(rootProject.buildDir)
}
