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

package dev.zitech.core.featureflag.data.repository

import com.google.common.truth.Truth.assertThat
import dev.zitech.core.common.DataFactory
import dev.zitech.core.common.framework.logger.AppConfigProvider
import dev.zitech.core.featureflag.data.provider.DevFeatureFlagProvider
import dev.zitech.core.featureflag.data.provider.PRIORITY_MEDIUM
import dev.zitech.core.featureflag.data.provider.PRIORITY_MINIMUM
import dev.zitech.core.featureflag.data.provider.ProdFeatureFlagProvider
import dev.zitech.core.featureflag.domain.model.DevFeature
import dev.zitech.core.featureflag.domain.model.Feature
import dev.zitech.core.featureflag.domain.model.ProdFeature
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class FeatureFlagRepositoryImplTest {

    private val devFeatureFlagProvider = mockk<DevFeatureFlagProvider>()
    private val prodFeatureFlagProvider = mockk<ProdFeatureFlagProvider>()

    private lateinit var sut: FeatureFlagRepositoryImpl

    @BeforeEach
    fun setUp() {
        sut = FeatureFlagRepositoryImpl(
            devFeatureFlagProvider = devFeatureFlagProvider,
            prodFeatureFlagProvider = prodFeatureFlagProvider
        )
    }

    @Nested
    inner class Init {

        @Test
        @DisplayName("WHEN debug THEN add devFeatureFlagProvider")
        fun isDebugModeTrue() {
            mockkObject(AppConfigProvider)
            every { AppConfigProvider.isDebugMode() } returns true

            sut.init()

            assertThat(sut.providers).contains(devFeatureFlagProvider)
            assertThat(sut.providers).doesNotContain(prodFeatureFlagProvider)
            assertThat(sut.providers.size).isEqualTo(1)
        }

        @Test
        @DisplayName("WHEN not debug THEN add prodFeatureFlagProvider")
        fun isDebugModeFalse() {
            mockkObject(AppConfigProvider)
            every { AppConfigProvider.isDebugMode() } returns false

            sut.init()

            assertThat(sut.providers).contains(prodFeatureFlagProvider)
            assertThat(sut.providers).doesNotContain(devFeatureFlagProvider)
            assertThat(sut.providers.size).isEqualTo(1)
        }
    }

    @Nested
    inner class IsFeatureEnabled {

        @Nested
        @DisplayName("RETURN true")
        inner class ReturnTrue {

            @Nested
            @DisplayName("WHEN devFeatureFlagProvider")
            inner class DevFeatureFlagProvider {

                @Test
                @DisplayName("hasFeature true and the feature is enabled")
                fun hasFeatureTrue() = runBlocking {
                    val feature = mockkClass(Feature::class)

                    coEvery { devFeatureFlagProvider.hasFeature(feature) } returns true
                    coEvery { devFeatureFlagProvider.isFeatureEnabled(feature) } returns true

                    sut.providers.add(devFeatureFlagProvider)

                    val result = sut.isFeatureEnabled(feature)

                    assertThat(result).isTrue()
                }

                @Test
                @DisplayName("hasFeature false")
                fun hasFeatureFalse() = runBlocking {
                    val feature = mockkClass(Feature::class)

                    every { feature.defaultValue } returns true
                    coEvery { devFeatureFlagProvider.hasFeature(feature) } returns false

                    sut.providers.add(devFeatureFlagProvider)

                    val result = sut.isFeatureEnabled(feature)

                    assertThat(result).isTrue()
                }

                @Test
                @DisplayName("has higher priority than prodFeatureFlagProvider")
                fun devFeatureFlagProviderHigherThanProdFeatureFlagProvider() = runBlocking {
                    val feature = mockkClass(Feature::class)

                    coEvery { prodFeatureFlagProvider getProperty "priority" } returns PRIORITY_MINIMUM
                    coEvery { devFeatureFlagProvider getProperty "priority" } returns PRIORITY_MEDIUM

                    coEvery { prodFeatureFlagProvider.hasFeature(feature) } returns true
                    coEvery { devFeatureFlagProvider.hasFeature(feature) } returns true

                    coEvery { prodFeatureFlagProvider.isFeatureEnabled(feature) } returns true
                    coEvery { devFeatureFlagProvider.isFeatureEnabled(feature) } returns true

                    sut.providers.add(devFeatureFlagProvider)
                    sut.providers.add(prodFeatureFlagProvider)

                    val result = sut.isFeatureEnabled(feature)

                    assertThat(result).isTrue()
                    coVerify { devFeatureFlagProvider.isFeatureEnabled(feature) }
                    coVerify(exactly = 0) { prodFeatureFlagProvider.isFeatureEnabled(feature) }
                }
            }

            @Nested
            @DisplayName("WHEN prodFeatureFlagProvider")
            inner class ProdFeatureFlagProvider {

                @Test
                @DisplayName("hasFeature true and the feature is enabled")
                fun hasFeatureTrue() = runBlocking {
                    val feature = mockkClass(Feature::class)

                    coEvery { prodFeatureFlagProvider.hasFeature(feature) } returns true
                    coEvery { prodFeatureFlagProvider.isFeatureEnabled(feature) } returns true

                    sut.providers.add(prodFeatureFlagProvider)

                    val result = sut.isFeatureEnabled(feature)

                    assertThat(result).isTrue()
                }

                @Test
                @DisplayName("hasFeature false")
                fun hasFeatureFalse() = runBlocking {
                    val feature = mockkClass(Feature::class)

                    every { feature.defaultValue } returns true
                    coEvery { prodFeatureFlagProvider.hasFeature(feature) } returns false

                    sut.providers.add(prodFeatureFlagProvider)

                    val result = sut.isFeatureEnabled(feature)

                    assertThat(result).isTrue()
                }

                @Test
                @DisplayName("WHEN prodFeatureFlagProvider has higher priority than devFeatureFlagProvider")
                fun prodFeatureFlagProviderHigherThanDevFeatureFlagProvider() = runBlocking {
                    val feature = mockkClass(Feature::class)

                    coEvery { prodFeatureFlagProvider getProperty "priority" } returns PRIORITY_MEDIUM
                    coEvery { devFeatureFlagProvider getProperty "priority" } returns PRIORITY_MINIMUM

                    coEvery { prodFeatureFlagProvider.hasFeature(feature) } returns true
                    coEvery { devFeatureFlagProvider.hasFeature(feature) } returns true

                    coEvery { prodFeatureFlagProvider.isFeatureEnabled(feature) } returns true
                    coEvery { devFeatureFlagProvider.isFeatureEnabled(feature) } returns true

                    sut.providers.add(devFeatureFlagProvider)
                    sut.providers.add(prodFeatureFlagProvider)

                    val result = sut.isFeatureEnabled(feature)

                    assertThat(result).isTrue()
                    coVerify { prodFeatureFlagProvider.isFeatureEnabled(feature) }
                    coVerify(exactly = 0) { devFeatureFlagProvider.isFeatureEnabled(feature) }
                }
            }
        }

        @Nested
        @DisplayName("RETURN false")
        inner class ReturnFalse {

            @Nested
            @DisplayName("WHEN devFeatureFlagProvider")
            inner class DevFeatureFlagProvider {

                @Test
                @DisplayName("hasFeature true and the feature is disabled")
                fun hasFeatureTrue() = runBlocking {
                    val feature = mockkClass(Feature::class)

                    coEvery { devFeatureFlagProvider.hasFeature(feature) } returns true
                    coEvery { devFeatureFlagProvider.isFeatureEnabled(feature) } returns false

                    sut.providers.add(devFeatureFlagProvider)

                    val result = sut.isFeatureEnabled(feature)

                    assertThat(result).isFalse()
                }

                @Test
                @DisplayName("hasFeature false")
                fun hasFeatureFalse() = runBlocking {
                    val feature = mockkClass(Feature::class)

                    every { feature.defaultValue } returns false
                    coEvery { devFeatureFlagProvider.hasFeature(feature) } returns false

                    sut.providers.add(devFeatureFlagProvider)

                    val result = sut.isFeatureEnabled(feature)

                    assertThat(result).isFalse()
                }

                @Test
                @DisplayName("has higher priority than prodFeatureFlagProvider")
                fun devFeatureFlagProviderHigherThanProdFeatureFlagProvider() = runBlocking {
                    val feature = mockkClass(Feature::class)

                    coEvery { prodFeatureFlagProvider getProperty "priority" } returns PRIORITY_MINIMUM
                    coEvery { devFeatureFlagProvider getProperty "priority" } returns PRIORITY_MEDIUM

                    coEvery { prodFeatureFlagProvider.hasFeature(feature) } returns true
                    coEvery { devFeatureFlagProvider.hasFeature(feature) } returns true

                    coEvery { prodFeatureFlagProvider.isFeatureEnabled(feature) } returns false
                    coEvery { devFeatureFlagProvider.isFeatureEnabled(feature) } returns false

                    sut.providers.add(devFeatureFlagProvider)
                    sut.providers.add(prodFeatureFlagProvider)

                    val result = sut.isFeatureEnabled(feature)

                    assertThat(result).isFalse()
                    coVerify { devFeatureFlagProvider.isFeatureEnabled(feature) }
                    coVerify(exactly = 0) { prodFeatureFlagProvider.isFeatureEnabled(feature) }
                }
            }

            @Nested
            @DisplayName("WHEN prodFeatureFlagProvider")
            inner class ProdFeatureFlagProvider {

                @Test
                @DisplayName("hasFeature true and the feature is disabled")
                fun hasFeatureTrue() = runBlocking {
                    val feature = mockkClass(Feature::class)

                    coEvery { prodFeatureFlagProvider.hasFeature(feature) } returns true
                    coEvery { prodFeatureFlagProvider.isFeatureEnabled(feature) } returns false

                    sut.providers.add(prodFeatureFlagProvider)

                    val result = sut.isFeatureEnabled(feature)

                    assertThat(result).isFalse()
                }


                @Test
                @DisplayName("hasFeature false")
                fun hasFeatureFalse() = runBlocking {
                    val feature = mockkClass(Feature::class)

                    every { feature.defaultValue } returns false
                    coEvery { prodFeatureFlagProvider.hasFeature(feature) } returns false

                    sut.providers.add(prodFeatureFlagProvider)

                    val result = sut.isFeatureEnabled(feature)

                    assertThat(result).isFalse()
                }

                @Test
                @DisplayName("has higher priority than devFeatureFlagProvider")
                fun prodFeatureFlagProviderHigherThanDevFeatureFlagProvider() = runBlocking {
                    val feature = mockkClass(Feature::class)

                    coEvery { prodFeatureFlagProvider getProperty "priority" } returns PRIORITY_MEDIUM
                    coEvery { devFeatureFlagProvider getProperty "priority" } returns PRIORITY_MINIMUM

                    coEvery { prodFeatureFlagProvider.hasFeature(feature) } returns true
                    coEvery { devFeatureFlagProvider.hasFeature(feature) } returns true

                    coEvery { prodFeatureFlagProvider.isFeatureEnabled(feature) } returns false
                    coEvery { devFeatureFlagProvider.isFeatureEnabled(feature) } returns false

                    sut.providers.add(devFeatureFlagProvider)
                    sut.providers.add(prodFeatureFlagProvider)

                    val result = sut.isFeatureEnabled(feature)

                    assertThat(result).isFalse()
                    coVerify { prodFeatureFlagProvider.isFeatureEnabled(feature) }
                    coVerify(exactly = 0) { devFeatureFlagProvider.isFeatureEnabled(feature) }
                }
            }
        }
    }

    @Test
    @DisplayName("WHEN getDevFeatures THEN return all values from DevFeature")
    fun getDevFeatures() = runBlocking {
        val result = sut.getDevFeatures()

        DevFeature.values().forEach {
            result.contains(it)
        }
    }

    @Test
    @DisplayName("WHEN setDevFeatureEnabled THEN call devFeatureFlagProvider.setFeatureEnabled")
    fun setTestFeatureEnabled() = runBlocking {
        val feature = mockkClass(Feature::class)
        val enabled = DataFactory.createRandomBoolean()

        coEvery { devFeatureFlagProvider.setFeatureEnabled(feature, enabled) } just Runs

        sut.setDevFeatureEnabled(feature, enabled)

        coVerify { devFeatureFlagProvider.setFeatureEnabled(feature, enabled) }
    }

    @Test
    @DisplayName("WHEN getProdFeatures THEN return all values from ProdFeature")
    fun getProdFeatures() = runBlocking {
        val result = sut.getProdFeatures()

        ProdFeature.values().forEach {
            result.contains(it)
        }
    }
}
