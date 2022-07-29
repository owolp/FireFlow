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

apply(from = "$rootDir/config/dependencies/test-dependencies.gradle")

plugins {
    id(BuildPlugins.KOTLIN_ANDROID)
    id(BuildPlugins.LIBRARY)
    id(BuildPlugins.JUNIT5)
    kotlin(BuildPlugins.KAPT)
}

android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments.putAll(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/schemas"
                    )
                )
            }
        }

        testInstrumentationRunner = AndroidConfigs.TEST_INSTRUMENTATION_RUNNER
        testInstrumentationRunnerArguments[
            AndroidConfigs.TEST_INSTRUMENTATION_RUNNER_ARGUMENT_KEY
        ] = AndroidConfigs.TEST_INSTRUMENTATION_RUNNER_ARGUMENT_VALUE
    }

    testFixtures {
        enable = true
    }
}

dependencies {
    implementation(projects.coreComponent.common)

    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.security.crypto)
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.jetbrains.kotlin.coroutines.android)
    implementation(libs.zetetic.sqlcipher)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.cash.turbine)
    androidTestImplementation(libs.google.truth)
    androidTestImplementation(libs.jetbrains.kotlin.coroutines.test)
    androidTestImplementation(libs.junit.jupiter.api)
    androidTestImplementation(libs.mannodermaus.junit5.android.test.core)
    androidTestRuntimeOnly(libs.mannodermaus.junit5.android.test.runner)
}

kapt {
    correctErrorTypes = true
}
