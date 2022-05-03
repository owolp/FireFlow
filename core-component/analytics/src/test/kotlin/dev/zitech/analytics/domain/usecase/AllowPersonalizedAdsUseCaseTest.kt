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

package dev.zitech.analytics.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.zitech.analytics.data.repository.AnalyticsRepositoryImpl
import dev.zitech.analytics.framework.analytics.FakeRemoteAnalytics
import dev.zitech.analytics.framework.source.AnalyticsProviderSourceImpl
import dev.zitech.core.common.DataFactory
import org.junit.Before
import org.junit.Test

internal class AllowPersonalizedAdsUseCaseTest {

    private val remoteAnalytics = FakeRemoteAnalytics()
    private val analyticsProviderSource = AnalyticsProviderSourceImpl(
        remoteAnalytics
    )
    private val analyticsRepository = AnalyticsRepositoryImpl(
        analyticsProviderSource
    )

    private lateinit var sut: AllowPersonalizedAdsUseCase

    @Before
    fun setup() {
        sut = AllowPersonalizedAdsUseCase(analyticsRepository)
    }

    @Test
    fun invoke() {
        // Arrange
        val expectedResult = DataFactory.createRandomBoolean()

        // Act
        sut(expectedResult)

        // Assert
        assertThat(remoteAnalytics.allowPersonalizedAdsValue).isEqualTo(expectedResult)
    }
}