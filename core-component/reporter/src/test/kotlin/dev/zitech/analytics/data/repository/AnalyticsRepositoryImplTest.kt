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
import dev.zitech.core.reporter.analytics.data.repository.AnalyticsRepositoryImpl
import dev.zitech.analytics.domain.model.AnalyticsEventFactory
import dev.zitech.core.reporter.analytics.domain.model.AnalyticsProvider
import dev.zitech.core.reporter.analytics.domain.repository.AnalyticsRepository
import dev.zitech.analytics.framework.analytics.FakeAnalyticsReporter
import dev.zitech.core.reporter.analytics.framework.AnalyticsProviderSourceImpl
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.domain.model.BuildFlavor
import dev.zitech.core.common.framework.applicationconfig.FakeAppConfigProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AnalyticsRepositoryImplTest {

    private val appConfigProvider = FakeAppConfigProvider()
    private val remoteAnalytics = FakeAnalyticsReporter()
    private val analyticsProviderSource = AnalyticsProviderSourceImpl(
        remoteAnalytics
    )

    private lateinit var sut: AnalyticsRepository

    @BeforeEach
    fun setup() {
        sut = AnalyticsRepositoryImpl(
            appConfigProvider,
            analyticsProviderSource
        )
    }

    @Test
    fun allowPersonalizedAds() {
        // Arrange
        val expectedResult = DataFactory.createRandomBoolean()

        // Act
        sut.allowPersonalizedAds(expectedResult)

        // Assert
        assertThat(remoteAnalytics.allowPersonalizedAdsValue).isEqualTo(expectedResult)
    }

    @Test
    fun setCollectionEnabled() {
        // Arrange
        val expectedResult = DataFactory.createRandomBoolean()

        // Act
        sut.setCollectionEnabled(expectedResult)

        // Assert
        assertThat(remoteAnalytics.setCollectionEnabledValue).isEqualTo(expectedResult)
    }

    @Test
    fun `WHEN buildFlavor Dev THEN do not log event`() {
        // Arrange
        appConfigProvider.setBuildFlavor(BuildFlavor.DEV)
        val analyticsEvent = AnalyticsEventFactory.createAnalyticsEvent()

        // Act
        sut.logEvent(analyticsEvent)

        // Assert
        assertThat(remoteAnalytics.events).isEmpty()
    }

    @Test
    fun `WHEN buildFlavor Foss THEN do not log event`() {
        // Arrange
        appConfigProvider.setBuildFlavor(BuildFlavor.FOSS)
        val analyticsEvent = AnalyticsEventFactory.createAnalyticsEvent()

        // Act
        sut.logEvent(analyticsEvent)

        // Assert
        assertThat(remoteAnalytics.events).isEmpty()
    }

    @Test
    fun `WHEN buildFlavor Play and provider Huawei THEN do not log event`() {
        // Arrange
        appConfigProvider.setBuildFlavor(BuildFlavor.PLAY)
        val analyticsEvent = AnalyticsEventFactory.createAnalyticsEvent(
            providers = listOf(AnalyticsProvider.HUAWEI)
        )

        // Act
        assertThat(remoteAnalytics.events).isEmpty()
        sut.logEvent(analyticsEvent)

        // Assert
        assertThat(remoteAnalytics.events).hasSize(0)
    }

    @Test
    fun `WHEN buildFlavor Play and provider Firebase THEN log event`() {
        // Arrange
        appConfigProvider.setBuildFlavor(BuildFlavor.PLAY)
        val analyticsEvent = AnalyticsEventFactory.createAnalyticsEvent(
            providers = listOf(AnalyticsProvider.FIREBASE)
        )

        // Act
        assertThat(remoteAnalytics.events).isEmpty()
        sut.logEvent(analyticsEvent)

        // Assert
        assertThat(remoteAnalytics.events).hasSize(1)
        assertThat(remoteAnalytics.events[analyticsEvent.name]).isEqualTo(analyticsEvent.params)
    }

    @Test
    fun `WHEN buildFlavor Gallery and provider Firebase THEN do not log event`() {
        // Arrange
        appConfigProvider.setBuildFlavor(BuildFlavor.GALLERY)
        val analyticsEvent = AnalyticsEventFactory.createAnalyticsEvent(
            providers = listOf(AnalyticsProvider.FIREBASE)
        )

        // Act
        assertThat(remoteAnalytics.events).isEmpty()
        sut.logEvent(analyticsEvent)

        // Assert
        assertThat(remoteAnalytics.events).hasSize(0)
    }

    @Test
    fun `WHEN buildFlavor Huawei and provider Gallery THEN log event`() {
        // Arrange
        appConfigProvider.setBuildFlavor(BuildFlavor.GALLERY)
        val analyticsEvent = AnalyticsEventFactory.createAnalyticsEvent(
            providers = listOf(AnalyticsProvider.HUAWEI)
        )

        // Act
        assertThat(remoteAnalytics.events).isEmpty()
        sut.logEvent(analyticsEvent)

        // Assert
        assertThat(remoteAnalytics.events).hasSize(1)
        assertThat(remoteAnalytics.events[analyticsEvent.name]).isEqualTo(analyticsEvent.params)
    }
}