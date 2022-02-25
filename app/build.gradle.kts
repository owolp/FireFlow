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

import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val secretProperties = retrieveSecretProperties()

android {
    val signingConfigDebug = "debug"
    val signingConfigFirebase = "firebase"
    val signingConfigPlay = "play"

    compileSdk = 32

    defaultConfig {
        applicationId = "dev.zitech.fireflow"
        minSdk = 29
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        getByName(signingConfigDebug) {
            storeFile = project.rootProject.file("config/keystore/debug.keystore")
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storePassword = "android"
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
            signingConfig = signingConfigs.getByName(signingConfigDebug)
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets {
        map { it.java.srcDir("src/${it.name}/kotlin") }
    }
}

dependencies {
    implementation(Dependencies.AndroidX.AppCompat.APP_COMPAT)
    implementation(Dependencies.AndroidX.Core.CORE_KTX)
    implementation(Dependencies.AndroidX.ConstraintLayout.CONSTRAINTLAYOUT)
    implementation(Dependencies.Google.Android.Material.MATERIAL)

    testImplementation(Dependencies.JUnit.J_UNIT)
}

fun retrieveSecretProperties(): Properties {
    val secretProperties = Properties()

    val secretsPropertiesFile: File = project.rootProject.file("secrets.properties")
    if (secretsPropertiesFile.exists()) {
        secretProperties.load(secretsPropertiesFile.inputStream())
    }

    return secretProperties
}
