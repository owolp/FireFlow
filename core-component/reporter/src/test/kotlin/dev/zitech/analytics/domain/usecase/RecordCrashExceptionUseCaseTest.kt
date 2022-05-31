///*
// * Copyright (C) 2022 Zitech Ltd.
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <https://www.gnu.org/licenses/>.
// */
//
//package dev.zitech.analytics.domain.usecase
//
//import com.google.common.truth.Truth.assertThat
//import dev.zitech.core.common.DataFactory
//import dev.zitech.core.reporter.crash.data.repository.CrashRepositoryImpl
//import dev.zitech.core.reporter.crash.domain.usecase.RecordCrashExceptionUseCase
//import dev.zitech.core.reporter.crash.framework.FakeCrashReporter
//import org.junit.jupiter.api.Test
//
//internal class RecordCrashExceptionUseCaseTest {
//
//    private val crashReporter = FakeCrashReporter()
//    private val crashReporterRepository = CrashRepositoryImpl(crashReporter)
//
//    @Test
//    fun invoke() {
//        // Arrange
//        val exception = DataFactory.createException()
//        val sut = RecordCrashExceptionUseCase(crashReporterRepository)
//
//        // Act
//        sut(exception)
//
//        // Assert
//        assertThat(crashReporter.recordExceptionValue).isEqualTo(exception)
//    }
//}