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

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.plugins.AppPlugin
import com.android.build.gradle.internal.plugins.LibraryPlugin

plugins.apply(BuildPlugins.DETEKT)
plugins.apply(BuildPlugins.GRADLE_VERSION_PLUGIN)
plugins.apply(BuildPlugins.GIT_HOOKS)
plugins.apply(BuildPlugins.KTLINT)

buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.build.gradle)
        classpath(libs.jetbrains.kotlin.gradle)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    project.plugins.applyBaseConfig(project)
}

val clean by tasks.creating(Delete::class) {
    delete(rootProject.buildDir)
}

fun BaseExtension.baseConfig() {
    compileSdkVersion(AppVersioning.COMPILE_SDK)

    defaultConfig.apply {
        minSdk = AppVersioning.MIN_SDK
        targetSdk = AppVersioning.TARGET_SDK
    }

    compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = libs.versions.jvmTarget.toString()
        }
    }

    sourceSets {
        map { it.java.srcDir("src/${it.name}/kotlin") }
    }
}

fun PluginContainer.applyBaseConfig(project: Project) {
    whenPluginAdded {
        when (this) {
            is AppPlugin -> {
                project.extensions
                    .getByType<AppExtension>()
                    .apply {
                        baseConfig()
                    }
            }
            is LibraryPlugin -> {
                project.extensions
                    .getByType<LibraryExtension>()
                    .apply {
                        baseConfig()
                    }
            }
        }
    }
}
