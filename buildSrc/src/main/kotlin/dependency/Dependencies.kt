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

package dependency

object Dependencies {

    object Android {
        object Tools {
            object Build {
                const val GRADLE = "com.android.tools.build:gradle:7.1.1"
            }
        }
    }

    object AndroidX {
        object AppCompat {
            const val APP_COMPAT = "androidx.appcompat:appcompat:1.4.1"
        }

        object Core {
            const val CORE_KTX = "androidx.core:core-ktx:1.7.0"
        }

        object ConstraintLayout {
            const val CONSTRAINTLAYOUT = "androidx.constraintlayout:constraintlayout:2.1.3"
        }
    }

    object Google {
        object Android {
            object Material {
                const val MATERIAL = "com.google.android.material:material:1.5.0"
            }
        }
    }

    object Jetbrains {
        object Kotlin {
            const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30"
        }
    }

    object JUnit {
        const val J_UNIT = "junit:junit:4.13.2"
    }

    object Pinterest {
        object Ktlint {
            const val KTLINT = "com.pinterest:ktlint:0.44.0"
        }
    }
}