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

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin

plugins.apply(BuildPlugins.DETEKT)
plugins.apply(BuildPlugins.GRADLE_VERSION_PLUGIN)
plugins.apply(BuildPlugins.GIT_HOOKS)
plugins.apply(BuildPlugins.KTLINT)

buildscript {

    repositories {
        google()
        mavenCentral()
        maven("https://developer.huawei.com/repo/")
    }

    dependencies {
        classpath(libs.huawei.agconnect.agcp)
        classpath(libs.android.build.gradle)
        classpath(libs.google.dagger.hilt.gradle)
        classpath(libs.google.gms.services)
        classpath(libs.google.firebase.crashlytics.gradle)
        classpath(libs.google.firebase.performance.gradle)
        classpath(libs.jetbrains.kotlin.gradle)
        classpath(libs.mannodermaus.gradle.plugins.android.junit5)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://developer.huawei.com/repo/")
    }
}

subprojects {
    project.plugins.applyConfig(project)
}

val clean by tasks.creating(Delete::class) {
    delete(rootProject.buildDir)
}

fun PluginContainer.applyConfig(project: Project) {
    whenPluginAdded {
        when (this) {
            is AppPlugin -> {
                project.extensions
                    .getByType<AppExtension>()
                    .apply {
                        baseConfig()
                        androidApplication(project)
                    }
            }
            is LibraryPlugin -> {
                project.extensions
                    .getByType<LibraryExtension>()
                    .apply {
                        baseConfig()
                        configureProductFlavors()
                    }
            }
        }
    }
}

fun BaseExtension.baseConfig() {
    compileSdkVersion(AppVersioning.COMPILE_SDK)

    defaultConfig.apply {
        minSdk = AppVersioning.MIN_SDK
        targetSdk = AppVersioning.TARGET_SDK
    }

    compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = libs.versions.jvmTarget.toString()
        }
    }

    sourceSets {
        map { it.java.srcDir("src/${it.name}/kotlin") }
    }

    configureBuildTypes(project)
    configureFlavorDimensions()
}

fun AppExtension.androidApplication(project: Project) {
    val secretProperties = AppSecret.retrieveSecretProperties(project)

    compileSdkVersion(AppVersioning.COMPILE_SDK)

    defaultConfig {
        applicationId = "dev.zitech.fireflow"

        versionName = AppVersioning.retrieveVersionName()
        versionCode = AppVersioning.retrieveVersionCode()
    }

    signingConfigs {
        getByName(SigningConfigs.DEBUG) {
            storeFile = project.rootProject.file("config/keystore/debug.keystore")
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storePassword = "android"
        }

        create(SigningConfigs.DEV) {
            storeFile = project.rootProject.file("config/keystore/dev.keystore")
            keyAlias = "${secretProperties["dev_signing_key_alias"]}"
            keyPassword = "${secretProperties["dev_signing_key_password"]}"
            storePassword = "${secretProperties["dev_signing_keystore_password"]}"
        }

        create(SigningConfigs.PROD) {
            storeFile = project.rootProject.file("config/keystore/prod.keystore")
            keyAlias = "${secretProperties["prod_signing_key_alias"]}"
            keyPassword = "${secretProperties["prod_signing_key_password"]}"
            storePassword = "${secretProperties["prod_signing_keystore_password"]}"
        }
    }

    configureProductFlavors(SigningConfigs.DEV, SigningConfigs.PROD)
}

fun BaseExtension.configureFlavorDimensions() {
    flavorDimensions("default")
}

fun BaseExtension.configureBuildTypes(project: Project) {
    buildTypes {
        getByName(BuildTypes.DEBUG) {
            isDebuggable = true
            isMinifyEnabled = false
        }

        getByName(BuildTypes.RELEASE) {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "${project.rootProject.file("config/proguard/proguard-rules.pro")}"
            )
        }
    }
}

fun AppExtension.configureProductFlavors(
    signingConfigDev: String,
    signingConfigProd: String
) {
    productFlavors {
        create(ProductFlavors.DEV) {
            signingConfig = signingConfigs.getByName(signingConfigDev)
            applicationIdSuffix = ".${ProductFlavors.DEV}"
            versionNameSuffix = "-${AppVersioning.retrieveVersionCode()}"
        }

        create(ProductFlavors.PLAY) {
            signingConfig = signingConfigs.getByName(signingConfigProd)
            applicationIdSuffix = ".${ProductFlavors.PLAY}"
        }

        create(ProductFlavors.GALLERY) {
            signingConfig = signingConfigs.getByName(signingConfigProd)
            applicationIdSuffix = ".${ProductFlavors.GALLERY}"
        }

        create(ProductFlavors.FOSS) {
            signingConfig = signingConfigs.getByName(signingConfigProd)
            applicationIdSuffix = ".${ProductFlavors.FOSS}"
        }
    }
}

fun BaseExtension.configureProductFlavors() {
    productFlavors {
        create(ProductFlavors.DEV) {}

        create(ProductFlavors.PLAY) {}

        create(ProductFlavors.GALLERY) {}

        create(ProductFlavors.FOSS) {}
    }
}
