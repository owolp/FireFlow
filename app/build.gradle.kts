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

import com.android.build.api.dsl.ApplicationProductFlavor
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.google.firebase.perf.plugin.FirebasePerfExtension
import com.huawei.agconnect.apms.plugin.APMSExtension

apply(from = "$rootDir/config/dependencies/compose-dependencies.gradle")

plugins {
    id(BuildPlugins.APPLICATION)
    id(BuildPlugins.AGPCONNECT)
    id(BuildPlugins.GOOGLE_SERVICES)
    id(BuildPlugins.FIREBASE_CRASHLYTICS)
    id(BuildPlugins.FIREBASE_PERFORMANCE)
    id(BuildPlugins.DAGGER)
    id(BuildPlugins.KOTLIN_ANDROID)
    kotlin(BuildPlugins.KAPT)
}

android {
    namespace = "dev.zitech.fireflow"

    buildTypes {
        getByName(BuildTypes.DEBUG) {
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
            agcp {
                mappingUpload = false
                debug = true
            }
            configure<APMSExtension> {
                instrumentationEnabled = false
            }
            configure<FirebasePerfExtension> {
                setInstrumentationEnabled(false)
            }
        }
    }

    productFlavors {
        getByName(ProductFlavors.DEV) {
            disableFirebaseCrashlyticsMappingFileUpload()
            disableFirebasePerformance()
            disableAgConnectCrashMappingFileUpload()
            disableAPMS()
        }
        getByName(ProductFlavors.FOSS) {
            disableFirebaseCrashlyticsMappingFileUpload()
            disableFirebasePerformance()
            disableAgConnectCrashMappingFileUpload()
            disableAPMS()
        }
        getByName(ProductFlavors.PLAY) {
            disableAgConnectCrashMappingFileUpload()
            disableAPMS()
        }
        getByName(ProductFlavors.GALLERY) {
            disableFirebaseCrashlyticsMappingFileUpload()
            disableFirebasePerformance()
            agcp {
                mappingUpload = true
                debug = false
                enableAPMS = true
            }
        }
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreComponent.core)
    implementation(projects.designSystem)
    implementation(projects.featureComponent.feature)

    devImplementation(libs.facebook.flipper.flipper)
    devImplementation(libs.facebook.soloader)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material3.windowsizeclass)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.splash)
    implementation(libs.androidx.lifecycle.runtime)
    kapt(libs.androidx.lifecycle.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.startup)
    implementation(libs.google.material)
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.jakewharton.process.phoenix)
    implementation(libs.jetbrains.kotlin.coroutines.android)

    debugImplementation(libs.squareup.leakcanary.android)
}

kapt {
    correctErrorTypes = true
}

fun ApplicationProductFlavor.disableFirebaseCrashlyticsMappingFileUpload() {
    configure<CrashlyticsExtension> {
        mappingFileUploadEnabled = false
    }
}

fun disableAgConnectCrashMappingFileUpload() {
    agcp {
        mappingUpload = false
    }
}

fun ApplicationProductFlavor.disableFirebasePerformance() {
    configure<FirebasePerfExtension> {
        setInstrumentationEnabled(false)
    }
}

fun ApplicationProductFlavor.disableAPMS() {
    configure<APMSExtension> {
        instrumentationEnabled = false
    }
}
