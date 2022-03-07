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
    id(BuildPlugins.APPLICATION)
    id(BuildPlugins.KOTLIN_ANDROID)
}

val secretProperties = AppSecret.retrieveSecretProperties(project)

android {
    val signingConfigDebug = "debug"
    val signingConfigDev = "dev"
    val signingConfigProd = "prod"

    compileSdk = AppVersioning.COMPILE_SDK

    defaultConfig {
        applicationId = "dev.zitech.fireflow"

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

        create(signingConfigProd) {
            storeFile = project.rootProject.file("config/keystore/prod.keystore")
            keyAlias = "${secretProperties["prod_signing_key_alias"]}"
            keyPassword = "${secretProperties["prod_signing_key_password"]}"
            storePassword = "${secretProperties["prod_signing_keystore_password"]}"
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
            versionNameSuffix = "-${AppVersioning.retrieveVersionCode()}"
        }

        create("play") {
            signingConfig = signingConfigs.getByName(signingConfigProd)
            applicationIdSuffix = ".play"
        }

        create("gallery") {
            signingConfig = signingConfigs.getByName(signingConfigProd)
            applicationIdSuffix = ".gallery"
        }

        create("foss") {
            signingConfig = signingConfigs.getByName(signingConfigProd)
            applicationIdSuffix = ".foss"
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.costraintlayout)
    implementation(libs.google.material)

    testImplementation(libs.junit)
}
