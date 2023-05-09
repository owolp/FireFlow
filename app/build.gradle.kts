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
apply(from = "$rootDir/config/dependencies/di-dependencies.gradle")
apply(from = "$rootDir/config/dependencies/kotlin-dependencies.gradle")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(BuildPlugins.APPLICATION)
    id(BuildPlugins.AGPCONNECT)
    id(BuildPlugins.GOOGLE_SERVICES)
    id(BuildPlugins.FIREBASE_CRASHLYTICS)
    id(BuildPlugins.FIREBASE_PERFORMANCE)
    id(BuildPlugins.DAGGER)
    id(BuildPlugins.KOTLIN_ANDROID)
    kotlin(BuildPlugins.KAPT)
    alias(libs.plugins.kover)
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
    implementation(projects.common.data)
    implementation(projects.common.domain)
    implementation(projects.common.presentation)
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
    implementation(libs.jakewharton.process.phoenix)

    debugImplementation(libs.squareup.leakcanary.android)
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

dependencies {
    kover(projects.common.data)
    kover(projects.common.domain)
    kover(projects.common.presentation)
    kover(projects.core)
    kover(projects.designSystem)
    kover(projects.featureComponent.authentication)
    kover(projects.featureComponent.dashboard)
    kover(projects.featureComponent.onboarding)
    kover(projects.featureComponent.settings)
}

kover {
    excludeJavaCode()
}

koverReport {

    filters {
        excludes {
            classes(
                "*.databinding.*",
                "*.BuildConfig",
                "*.di.*",
                "*hilt*",
                "*Hilt*",
                "*_Factory*",
                "*_Impl*",
                "*JsonAdapter*"
            )
        }
    }

    androidReports("devDebug") {
        html {
            onCheck = false
            setReportDir(file("${project.rootDir}/reports/kover/"))
        }
    }
}
