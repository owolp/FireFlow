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

package dev.zitech.core.network.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dev.zitech.authenticator.data.remote.service.OAuthService
import dev.zitech.authenticator.di.annotation.InterceptorAuthentication
import dev.zitech.core.common.domain.concurrency.ControlledRunner
import dev.zitech.core.network.data.factory.InterceptorFactory
import dev.zitech.core.network.data.service.AboutService
import dev.zitech.core.network.domain.retrofit.RetrofitModel
import dev.zitech.core.network.domain.retrofit.ServiceModel
import dev.zitech.core.network.framework.retrofit.RetrofitFactory
import dev.zitech.core.network.framework.retrofit.RetrofitModelImpl
import dev.zitech.core.network.framework.retrofit.ServiceModelImpl
import javax.inject.Singleton
import okhttp3.Interceptor

internal interface NetworkModule {

    @InstallIn(SingletonComponent::class)
    @Module
    object SingletonProvides {

        @Singleton
        @Provides
        fun interceptorFactory(
            @ApplicationContext context: Context,
            @InterceptorAuthentication authenticationInterceptor: Interceptor
        ) = InterceptorFactory(
            context = context,
            authenticationInterceptor = authenticationInterceptor
        )

        @Singleton
        @Provides
        fun retrofitModel(
            retrofitFactory: RetrofitFactory
        ): RetrofitModel = RetrofitModelImpl(retrofitFactory, ControlledRunner())

        @Singleton
        @Provides
        fun oAuthService(serviceModel: ServiceModel): OAuthService = serviceModel.oAuthService
    }

    @InstallIn(SingletonComponent::class)
    @Module
    interface SingletonBinds {

        @Singleton
        @Binds
        fun serviceModel(serviceModelImpl: ServiceModelImpl): ServiceModel
    }

    @InstallIn(ViewModelComponent::class)
    @Module
    object ViewModelProvides {

        @ViewModelScoped
        @Provides
        fun aboutService(serviceModel: ServiceModel): AboutService = serviceModel.aboutService
    }
}
