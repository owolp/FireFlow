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

package dev.zitech.fireflow.common.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.zitech.fireflow.common.domain.usecase.user.GetCurrentUserUseCase
import dev.zitech.fireflow.common.presentation.connectivity.NetworkConnectivityProvider
import dev.zitech.fireflow.common.presentation.connectivity.NetworkConnectivityProviderImpl
import dev.zitech.fireflow.core.dispatcher.AppDispatchers
import dev.zitech.fireflow.core.logger.ErrorTree
import dev.zitech.fireflow.presentation.logger.ErrorTreeImpl
import javax.inject.Singleton

internal interface PresentationModule {

    @InstallIn(SingletonComponent::class)
    @Module
    interface SingletonBinds {

        @Singleton
        @Binds
        fun errorTree(errorTreeImpl: ErrorTreeImpl): ErrorTree
    }

    @InstallIn(SingletonComponent::class)
    @Module
    object SingletonProvides {

        @Singleton
        @Provides
        fun networkConnectivityProvider(
            appDispatchers: AppDispatchers,
            getCurrentUserUseCase: GetCurrentUserUseCase
        ): NetworkConnectivityProvider = NetworkConnectivityProviderImpl(
            appDispatchers,
            getCurrentUserUseCase
        )
    }
}
