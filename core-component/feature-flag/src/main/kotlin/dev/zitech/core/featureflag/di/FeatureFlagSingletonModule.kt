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

package dev.zitech.core.featureflag.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.zitech.core.featureflag.data.provider.DevFeatureFlagProvider
import dev.zitech.core.featureflag.data.provider.ProdFeatureFlagProvider
import dev.zitech.core.featureflag.data.repository.FeatureFlagRepositoryImpl
import dev.zitech.core.featureflag.di.annotation.DevFeatureFlagProvider as DevFeatureFlagProviderAnnotation
import dev.zitech.core.featureflag.di.annotation.ProdFeatureFlagProvider as ProdFeatureFlagProviderAnnotation
import dev.zitech.core.featureflag.domain.provider.FeatureFlagProvider
import dev.zitech.core.featureflag.domain.repository.FeatureFlagRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface FeatureFlagSingletonModule {

    @DevFeatureFlagProviderAnnotation
    @Singleton
    @Binds
    fun devFeatureFlagProvider(
        devFeatureFlagProvider: DevFeatureFlagProvider
    ): FeatureFlagProvider

    @ProdFeatureFlagProviderAnnotation
    @Singleton
    @Binds
    fun prodFeatureFlagProvider(
        prodFeatureFlagProvider: ProdFeatureFlagProvider
    ): FeatureFlagProvider

    @Singleton
    @Binds
    fun FeatureFlagRepository(
        featureFlagRepositoryImpl: FeatureFlagRepositoryImpl
    ): FeatureFlagRepository
}
