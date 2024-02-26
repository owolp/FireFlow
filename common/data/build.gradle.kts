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

apply(from = "$rootDir/config/dependencies/di-dependencies.gradle")
apply(from = "$rootDir/config/dependencies/kotlin-dependencies.gradle")
apply(from = "$rootDir/config/dependencies/room-dependencies.gradle")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(BuildPlugins.KOTLIN_ANDROID)
    id(BuildPlugins.LIBRARY)
    kotlin(BuildPlugins.KAPT)
    alias(libs.plugins.kover)
}

android {
    namespace = "dev.zitech.fireflow.common.data"
}

dependencies {
    implementation(projects.core)
    implementation(projects.common.domain)

    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)

    implementation(libs.squareup.moshi.moshi)
    kapt(libs.squareup.moshi.kotlin.codegen)
    implementation(libs.squareup.retrofit2.retrofit)
    implementation(libs.squareup.retrofit2.converter.moshi)
    implementation(platform(libs.squareup.okhttp3.bom))
    implementation("com.squareup.okhttp3:okhttp")

    devImplementation(libs.chuckerteam.chucker)
    devImplementation(libs.facebook.flipper.flipper)
    devApi(libs.facebook.flipper.network.plugin)
    devImplementation("com.squareup.okhttp3:logging-interceptor")

    fossImplementation(libs.acra.http)

    playImplementation(platform(libs.google.firebase.bom))
    playImplementation("com.google.firebase:firebase-analytics-ktx")
    playImplementation("com.google.firebase:firebase-config-ktx")
    playImplementation("com.google.firebase:firebase-crashlytics-ktx")
    playImplementation("com.google.firebase:firebase-perf-ktx")

    galleryImplementation(libs.huawei.agconnect.crash)
    galleryImplementation(libs.huawei.agconnect.apms)
    galleryImplementation(libs.huawei.agconnect.remoteconfig)
    galleryImplementation(libs.huawei.hms.hianalytics)
}
