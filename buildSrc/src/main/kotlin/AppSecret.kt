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

import java.io.File
import java.util.Properties
import org.gradle.api.Project

object AppSecret {

    fun retrieveSecretProperties(project: Project): Properties {
        val secretProperties = Properties()

        val secretsPropertiesFile: File = project.rootProject.file("secrets.properties")
        if (secretsPropertiesFile.exists()) {
            secretProperties.load(secretsPropertiesFile.inputStream())
        }

        return secretProperties
    }
}
