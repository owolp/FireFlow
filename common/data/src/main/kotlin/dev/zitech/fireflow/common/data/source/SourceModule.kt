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

package dev.zitech.fireflow.common.data.source

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.zitech.fireflow.common.data.source.configurator.ConfiguratorProviderSource
import dev.zitech.fireflow.common.data.source.configurator.ConfiguratorProviderSourceImpl
import javax.inject.Singleton

internal interface SourceModule {

    @InstallIn(SingletonComponent::class)
    @Module
    interface SingletonBinds {

        @Singleton
        @Binds
        fun configuratorProviderSource(configuratorProviderSourceImpl: ConfiguratorProviderSourceImpl): ConfiguratorProviderSource
    }
}
