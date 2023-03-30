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

package plugin

import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("io.gitlab.arturbosch.detekt")
}

val detektVersion = "1.22.0"

detekt {
    toolVersion = detektVersion
}

val detektCheck by tasks.registering(Detekt::class) {
    description = "Runs Detekt on the whole project at once."
    config.setFrom(configFile)
    parallel = true
    setSource(analysisDir)
    include(kotlinFiles)
    include(kotlinScriptFiles)
    exclude(resourceFiles)
    exclude(buildFiles)
    exclude(testFiles)
    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(true)
    }
}

val detektFormat by tasks.registering(Detekt::class) {
    description = "Formats whole project."
    config.setFrom(configFile)
    parallel = true
    autoCorrect = true
    setSource(analysisDir)
    include(kotlinFiles)
    include(kotlinScriptFiles)
    exclude(resourceFiles)
    exclude(buildFiles)
    exclude(testFiles)
    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(true)
    }
}

val analysisDir = file(projectDir)
val configFile = files("${project.rootDir}/config/detekt/detekt.yml")

val kotlinFiles = "**/*.kt"
val kotlinScriptFiles = "**/*.kts"
val resourceFiles = "**/resources/**"
val buildFiles = "**/build/**"
val testFiles = "**/*Test.kt"
