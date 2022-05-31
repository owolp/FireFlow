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

package dev.zitech.analytics.data.repository

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.reporter.crash.domain.repository.CrashRepository
import dev.zitech.analytics.framework.reporter.FakeCrashReporter
import dev.zitech.core.reporter.crash.data.repository.CrashRepositoryImpl
import io.mockk.mockkClass
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CrashRepositoryImplTest {

    private val crashReporter = FakeCrashReporter()

    private lateinit var sut: CrashRepository

    @BeforeEach
    fun setup() {
        sut = CrashRepositoryImpl(
            crashReporter
        )
    }

    @Test
    fun init() {
        // Act
        sut.init()

        // Assert
        assertThat(crashReporter.initValue).isTrue()
    }

    @Test
    fun log() {
        // Arrange
        val message = "Message"

        // Act
        sut.log(message)

        // Assert
        assertThat(crashReporter.logValue).isEqualTo(message)
    }

    @Test
    fun recordException() {
        // Arrange
        val exception = mockkClass(Exception::class)

        // Act
        sut.recordException(exception)

        // Assert
        assertThat(crashReporter.recordExceptionValue).isEqualTo(exception)
    }

    @Test
    fun setCustomKey() {
        // Arrange
        val key1 = "key1"
        val value1 = "Value1"
        val key2 = "key2"
        val value2 = true

        // Act
        sut.setCustomKey(key1, value1)
        sut.setCustomKey(key2, value2)

        // Assert
        assertThat(crashReporter.setCustomKeyValue[key1]).isEqualTo(value1)
        assertThat(crashReporter.setCustomKeyValue[key2]).isEqualTo(value2)
    }

    @Test
    fun setCrashCollectionEnabled() {
        // Arrange
        val enabled = true

        // Act
        sut.setCollectionEnabled(enabled)

        // Assert
        assertThat(crashReporter.setCrashCollectionEnabledValue).isTrue()
    }
}