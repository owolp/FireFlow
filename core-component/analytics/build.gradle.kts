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

plugins {
    id(BuildPlugins.KOTLIN_ANDROID)
    id(BuildPlugins.LIBRARY)
    id(BuildPlugins.JUNIT5)
    kotlin(BuildPlugins.KAPT)
}

dependencies {
    implementation(projects.coreComponent.common)
    implementation(projects.coreComponent.persistence)

    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.jetbrains.kotlin.coroutines.android)

    playImplementation(platform(libs.google.firebase.bom))
    playImplementation("com.google.firebase:firebase-analytics-ktx")

    galleryImplementation(libs.huawei.hms.hianalytics)

    testImplementation(testFixtures(projects.coreComponent.common))
    testImplementation(libs.cash.turbine)
    testImplementation(libs.google.truth)
    testImplementation(libs.mockk.mockk)
    testImplementation(libs.jetbrains.kotlin.coroutines.test)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

kapt {
    correctErrorTypes = true
}
