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

import com.android.build.api.dsl.ApplicationProductFlavor
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
    id(BuildPlugins.APPLICATION)
    id(BuildPlugins.AGPCONNECT)
    id(BuildPlugins.GOOGLE_SERVICES)
    id(BuildPlugins.FIREBASE_CRASHLYTICS)
    id(BuildPlugins.DAGGER)
    id(BuildPlugins.KOTLIN_ANDROID)
    kotlin(BuildPlugins.KAPT)
}

android {
    buildTypes {
        getByName(BuildTypes.DEBUG) {
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }
    }

    productFlavors {
        getByName(ProductFlavors.DEV) {
            disableCrashlyticsMappingFileUpload()
        }
        getByName(ProductFlavors.FOSS) {
            disableCrashlyticsMappingFileUpload()
        }
        getByName(ProductFlavors.GALLERY) {
            disableCrashlyticsMappingFileUpload()
        }
    }
}

dependencies {
    implementation(projects.coreComponent.core)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.costraintlayout)
    implementation(libs.androidx.lifecycle.runtime)
    kapt(libs.androidx.lifecycle.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.startup)
    implementation(libs.google.material)
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.jetbrains.kotlin.coroutines.android)

    debugImplementation(libs.squareup.leakcanary.android)
}

kapt {
    correctErrorTypes = true
}

fun ApplicationProductFlavor.disableCrashlyticsMappingFileUpload() {
    configure<CrashlyticsExtension> {
        mappingFileUploadEnabled = false
    }
}
