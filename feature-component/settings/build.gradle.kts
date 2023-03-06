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
apply(from = "$rootDir/config/dependencies/test-dependencies.gradle")

plugins {
    id(BuildPlugins.KOTLIN_ANDROID)
    id(BuildPlugins.LIBRARY)
    id(BuildPlugins.JUNIT5)
    kotlin(BuildPlugins.KAPT)
}

android {
    namespace = "dev.zitech.settings"
}

dependencies {
    implementation(projects.coreComponent.core)
    implementation(projects.designSystem)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.jetbrains.kotlin.coroutines.android)

    testImplementation(testFixtures(projects.designSystem))
    testImplementation(testFixtures(projects.coreComponent.persistence))
}

kapt {
    correctErrorTypes = true
}
