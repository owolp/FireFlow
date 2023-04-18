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

package dev.zitech.fireflow.common.data.remote.rest.retrofit.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zitech.fireflow.common.data.remote.rest.factory.InterceptorFactory
import dev.zitech.fireflow.common.data.remote.rest.retrofit.RetrofitFactory
import dev.zitech.fireflow.common.data.remote.rest.retrofit.authenticator.RefreshTokenAuthenticator
import dev.zitech.fireflow.common.data.remote.rest.retrofit.di.annotation.InterceptorAuthentication
import dev.zitech.fireflow.common.data.remote.rest.retrofit.interceptor.AuthenticationInterceptor
import dev.zitech.fireflow.common.data.remote.rest.retrofit.model.RetrofitModel
import dev.zitech.fireflow.common.data.remote.rest.retrofit.model.RetrofitModelImpl
import dev.zitech.fireflow.common.data.remote.rest.retrofit.model.ServiceModel
import dev.zitech.fireflow.common.data.remote.rest.retrofit.model.ServiceModelImpl
import dev.zitech.fireflow.core.concurrency.ControlledRunner
import javax.inject.Singleton
import okhttp3.Authenticator
import okhttp3.Interceptor

internal interface RetrofitModule {

    @InstallIn(SingletonComponent::class)
    @Module
    interface SingletonBinds {

        @InterceptorAuthentication
        @Singleton
        @Binds
        fun authenticationInterceptor(
            authenticationInterceptor: AuthenticationInterceptor
        ): Interceptor

        @Singleton
        @Binds
        fun refreshTokenAuthenticator(
            refreshTokenAuthenticator: RefreshTokenAuthenticator
        ): Authenticator

        @Singleton
        @Binds
        fun serviceModel(
            serviceModelImpl: ServiceModelImpl
        ): ServiceModel
    }

    @InstallIn(SingletonComponent::class)
    @Module
    object SingletonProvides {

        @Singleton
        @Provides
        fun interceptorFactory(
            @ApplicationContext context: Context,
            @InterceptorAuthentication authenticationInterceptor: Interceptor
        ) = InterceptorFactory(
            context,
            authenticationInterceptor
        )

        @Singleton
        @Provides
        fun retrofitModel(
            retrofitFactory: RetrofitFactory
        ): RetrofitModel = RetrofitModelImpl(
            ControlledRunner(),
            retrofitFactory
        )
    }
}
