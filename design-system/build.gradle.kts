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

apply(from = "$rootDir/config/dependencies/compose-dependencies.gradle")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(BuildPlugins.KOTLIN_ANDROID)
    id(BuildPlugins.LIBRARY)
    alias(libs.plugins.kover)
}

android {
    namespace = "dev.zitech.fireflow.ds"

    testFixtures {
        enable = true
    }
}

dependencies {
    implementation(libs.airbnb.android.lottie.compose)
    implementation(libs.google.accompanist.systemuicontroller)
    implementation(libs.google.accompanist.placeholder.material)
}
