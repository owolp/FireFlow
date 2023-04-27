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

package dev.zitech.fireflow.common.data.remote.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.zitech.fireflow.common.data.remote.configurator.RemoteConfigurator
import dev.zitech.fireflow.common.data.remote.configurator.RemoteConfiguratorImpl
import dev.zitech.fireflow.common.data.remote.rest.retrofit.model.ServiceModel
import dev.zitech.fireflow.common.data.remote.rest.service.AboutService
import dev.zitech.fireflow.common.data.remote.rest.service.OAuthService
import javax.inject.Singleton

internal interface RemoteModule {

    @InstallIn(SingletonComponent::class)
    @Module
    interface SingletonBinds {

        @Singleton
        @Binds
        fun remoteConfigurator(remoteConfiguratorImpl: RemoteConfiguratorImpl): RemoteConfigurator
    }

    @InstallIn(SingletonComponent::class)
    @Module
    object SingletonProvides {

        @Singleton
        @Provides
        fun aboutService(serviceModel: ServiceModel): AboutService = serviceModel.aboutService

        @Singleton
        @Provides
        fun oAuthService(serviceModel: ServiceModel): OAuthService = serviceModel.oAuthService
    }
}
