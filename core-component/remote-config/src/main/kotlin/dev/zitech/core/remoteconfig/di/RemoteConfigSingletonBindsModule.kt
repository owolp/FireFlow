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

package dev.zitech.core.remoteconfig.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.zitech.core.remoteconfig.data.repository.ConfigRepositoryImpl
import dev.zitech.core.remoteconfig.domain.repository.ConfigRepository
import dev.zitech.core.remoteconfig.framework.configurator.RemoteConfigurator
import dev.zitech.core.remoteconfig.framework.configurator.RemoteConfiguratorImpl
import dev.zitech.core.remoteconfig.framework.source.ConfigProviderSource
import dev.zitech.core.remoteconfig.framework.source.ConfigProviderSourceImpl
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi

@InstallIn(SingletonComponent::class)
@Module
internal interface RemoteConfigSingletonBindsModule {

    @Singleton
    @Binds
    fun configProviderSource(
        configProviderSourceImpl: ConfigProviderSourceImpl
    ): ConfigProviderSource

    @ExperimentalCoroutinesApi
    @Singleton
    @Binds
    fun remoteConfigurator(
        remoteConfiguratorImpl: RemoteConfiguratorImpl
    ): RemoteConfigurator

    @Singleton
    @Binds
    fun configRepository(
        configRepositoryImpl: ConfigRepositoryImpl
    ): ConfigRepository
}
