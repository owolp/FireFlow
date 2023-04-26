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

package dev.zitech.core.reporter.analytics.domain.usecase.event

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.domain.model.BuildFlavor
import dev.zitech.core.common.framework.applicationconfig.FakeAppConfigProvider
import dev.zitech.core.reporter.analytics.data.repository.AnalyticsRepositoryImpl
import dev.zitech.core.reporter.analytics.framework.FakeAnalyticsReporter
import dev.zitech.fireflow.common.domain.model.event.ApplicationLaunchEvent
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ApplicationLaunchAnalyticsEventTest {

    private val appConfigProvider = FakeAppConfigProvider()
    private val remoteAnalytics = FakeAnalyticsReporter()
    private val analyticsRepository = AnalyticsRepositoryImpl(
        appConfigProvider,
        remoteAnalytics
    )

    private lateinit var sut: dev.zitech.fireflow.domain.usecase.ApplicationLaunchAnalyticsEvent

    @BeforeEach
    fun setup() {
        sut = dev.zitech.fireflow.domain.usecase.ApplicationLaunchAnalyticsEvent(
            analyticsRepository
        )
    }

    @Test
    fun `WHEN flavor Gallery THEN log event`() {
        // Arrange
        appConfigProvider.setBuildFlavor(BuildFlavor.GALLERY)

        // Act
        assertThat(remoteAnalytics.events).hasSize(0)
        sut()

        // Assert
        assertThat(remoteAnalytics.events).hasSize(1)
        assertThat(remoteAnalytics.events).containsKey(
            dev.zitech.fireflow.common.domain.model.event.ApplicationLaunchEvent().name
        )
    }

    @Test
    fun `WHEN flavor Play THEN log event`() {
        // Arrange
        appConfigProvider.setBuildFlavor(BuildFlavor.PLAY)

        // Act
        assertThat(remoteAnalytics.events).hasSize(0)
        sut()

        // Assert
        assertThat(remoteAnalytics.events).hasSize(1)
        assertThat(remoteAnalytics.events).containsKey(
            dev.zitech.fireflow.common.domain.model.event.ApplicationLaunchEvent().name
        )
    }
}
