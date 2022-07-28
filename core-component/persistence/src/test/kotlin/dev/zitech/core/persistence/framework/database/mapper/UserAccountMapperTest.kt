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

package dev.zitech.core.persistence.framework.database.mapper

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.data.mapper.Mapper
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.framework.database.entity.UserAccountEntity
import dev.zitech.core.persistence.framework.database.entity.UserAccountEntityFactory
import javax.inject.Named
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class UserAccountMapperTest {

    private lateinit var sut: Mapper<UserAccountEntity, UserAccount>

    @BeforeEach
    fun setup() {
        sut = UserAccountMapper()
    }

    @Nested
    @Named("Id")
    inner class Id {

        @Test
        @DisplayName("WHEN input id is not null THEN return correct value")
        fun notNull() {
            // Arrange
            val id = DataFactory.createRandomLong()
            val input = UserAccountEntityFactory.createUserAccountEntity(
                id = id
            )

            // Act
            val result = sut(input)

            // Assert
            assertThat(result.id).isEqualTo(id)
        }

        @Test
        @DisplayName("WHEN input id is null THEN return correct value")
        fun isNull() {
            // Arrange
            val input = UserAccountEntityFactory.createUserAccountEntity(
                id = null
            )

            // Act
            val result = sut(input)

            // Assert
            assertThat(result.id).isEqualTo(-1)
        }
    }

    @Test
    fun isCurrentUserAccount() {
        // Arrange
        val isCurrentUserAccount = DataFactory.createRandomBoolean()
        val input = UserAccountEntityFactory.createUserAccountEntity(
            isCurrentUserAccount = isCurrentUserAccount
        )

        // Act
        val result = sut(input)

        // Assert
        assertThat(result.isCurrentUserAccount).isEqualTo(isCurrentUserAccount)
    }

    @Nested
    @Named("Theme")
    inner class Theme {

        @Test
        @DisplayName("WHEN input theme is System Id THEN return SYSTEM")
        fun system() {
            // Arrange
            val themeId = ApplicationTheme.SYSTEM.id
            val input = UserAccountEntityFactory.createUserAccountEntity(
                theme = themeId
            )

            // Act
            val result = sut(input)

            // Assert
            assertThat(result.theme).isEqualTo(ApplicationTheme.SYSTEM)
        }

        @Test
        @DisplayName("WHEN input theme is Dark Id THEN return DARK")
        fun dark() {
            // Arrange
            val themeId = ApplicationTheme.DARK.id
            val input = UserAccountEntityFactory.createUserAccountEntity(
                theme = themeId
            )

            // Act
            val result = sut(input)

            // Assert
            assertThat(result.theme).isEqualTo(ApplicationTheme.DARK)
        }

        @Test
        @DisplayName("WHEN input theme is Light Id THEN return LIGHT")
        fun light() {
            // Arrange
            val themeId = ApplicationTheme.LIGHT.id
            val input = UserAccountEntityFactory.createUserAccountEntity(
                theme = themeId
            )

            // Act
            val result = sut(input)

            // Assert
            assertThat(result.theme).isEqualTo(ApplicationTheme.LIGHT)
        }

        @Test
        @DisplayName("WHEN input theme is other Id THEN return SYSTEM")
        fun other() {
            // Arrange
            val themeId = DataFactory.createRandomInt(min = 10)
            val input = UserAccountEntityFactory.createUserAccountEntity(
                theme = themeId
            )

            // Act
            val result = sut(input)

            // Assert
            assertThat(result.theme).isEqualTo(ApplicationTheme.SYSTEM)
        }
    }
}