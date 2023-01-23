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

package plugin

val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:0.48.2") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))
val kotlinFiles = "**/*.kt"
val kotlinScriptFiles = "**/*.kts"
val buildFiles = "**/build/**"
val testFiles = "**/*Test.kt"

val ktlintCheck by tasks.creating(JavaExec::class) {
    description = "Runs Ktlint on the whole project at once."
    inputs.files(inputFiles)

    description = "Check Kotlin code style."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf(
        "--reporter=plain",
        "--reporter=html?group_by_file," +
            "output=${project.rootDir}/reports/ktlint/ktlint-check-report.html",
        "--android",
        kotlinFiles,
        kotlinScriptFiles,
        "!$buildFiles",
        "!$testFiles"
    )
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    description = "Formats whole project."
    inputs.files(inputFiles)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf(
        "--android",
        "-F",
        kotlinFiles,
        kotlinScriptFiles,
        "!$buildFiles",
        "!$testFiles"
    )
}
