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

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val secretProperties = AppSecret.retrieveSecretProperties(project)

android {
    val signingConfigDebug = "debug"
    val signingConfigDev = "dev"
    val signingConfigFirebase = "firebase"
    val signingConfigPlay = "play"

    compileSdk = AppVersioning.COMPILE_SDK

    defaultConfig {
        applicationId = "dev.zitech.fireflow"
        minSdk = AppVersioning.MIN_SDK
        targetSdk = AppVersioning.TARGET_SDK

        versionName = AppVersioning.retrieveVersionName()
        versionCode = AppVersioning.retrieveVersionCode()
    }

    signingConfigs {
        getByName(signingConfigDebug) {
            storeFile = project.rootProject.file("config/keystore/debug.keystore")
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storePassword = "android"
        }

        create(signingConfigDev) {
            storeFile = project.rootProject.file("config/keystore/dev.keystore")
            keyAlias = "${secretProperties["dev_signing_key_alias"]}"
            keyPassword = "${secretProperties["dev_signing_key_password"]}"
            storePassword = "${secretProperties["dev_signing_keystore_password"]}"
        }

        create(signingConfigFirebase) {
            storeFile = project.rootProject.file("config/keystore/firebase.keystore")
            keyAlias = "${secretProperties["firebase_signing_key_alias"]}"
            keyPassword = "${secretProperties["firebase_signing_key_password"]}"
            storePassword = "${secretProperties["firebase_signing_keystore_password"]}"
        }

        create(signingConfigPlay) {
            storeFile = project.rootProject.file("config/keystore/play.keystore")
            keyAlias = "${secretProperties["play_signing_key_alias"]}"
            keyPassword = "${secretProperties["play_signing_key_password"]}"
            storePassword = "${secretProperties["play_signing_keystore_password"]}"
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "${project.rootProject.file("config/proguard/proguard-rules.pro")}"
            )
        }
    }

    flavorDimensions.add("default")

    productFlavors {
        create("dev") {
            signingConfig = signingConfigs.getByName(signingConfigDev)
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-d"
        }

        create("firebase") {
            signingConfig = signingConfigs.getByName(signingConfigFirebase)
            applicationIdSuffix = ".firebase"
            versionNameSuffix = "-b"
        }

        create("play") {
            signingConfig = signingConfigs.getByName(signingConfigPlay)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    sourceSets {
        map { it.java.srcDir("src/${it.name}/kotlin") }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.costraintlayout)
    implementation(libs.google.material)

    testImplementation(libs.junit)
}
