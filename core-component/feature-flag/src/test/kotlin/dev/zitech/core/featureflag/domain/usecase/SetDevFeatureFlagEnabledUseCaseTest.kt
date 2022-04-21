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

package dev.zitech.core.featureflag.domain.usecase

import dev.zitech.core.common.DataFactory
import dev.zitech.core.persistence.domain.model.PreferenceType
import dev.zitech.core.persistence.domain.repository.SavePreferencesRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SetDevFeatureFlagEnabledUseCaseTest {

    private val savePreferencesRepository = mockk<SavePreferencesRepository>()

    private lateinit var sut: SetDevFeatureFlagEnabledUseCase

    @BeforeEach
    fun setUp() {
        sut = SetDevFeatureFlagEnabledUseCase(
            savePreferencesRepository = savePreferencesRepository
        )
    }

    @Test
    fun invoke() = runBlocking {
        val preferenceType = PreferenceType.DEVELOPMENT
        val key = DataFactory.createRandomString()
        val value = DataFactory.createRandomBoolean()

        coEvery {
            savePreferencesRepository.saveBoolean(
                preferenceType = preferenceType,
                key = key,
                value = value
            )
        } just Runs

        sut(
            key = key,
            value = value
        )

        coVerify {
            savePreferencesRepository.saveBoolean(
                preferenceType = preferenceType,
                key = key,
                value = value
            )
        }
    }
}