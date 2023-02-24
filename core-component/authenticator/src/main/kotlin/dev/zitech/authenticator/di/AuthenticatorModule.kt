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

package dev.zitech.authenticator.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.zitech.authenticator.data.remote.source.OAuthSource
import dev.zitech.authenticator.data.repository.TokenRepositoryImpl
import dev.zitech.authenticator.di.annotation.InterceptorAuthentication
import dev.zitech.authenticator.domain.repository.TokenRepository
import dev.zitech.authenticator.framework.authenticator.RefreshTokenAuthenticator
import dev.zitech.authenticator.framework.interceptor.AuthenticationInterceptor
import dev.zitech.authenticator.framework.source.OAuthRemoteSource
import javax.inject.Singleton
import okhttp3.Authenticator
import okhttp3.Interceptor

internal interface AuthenticatorModule {

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
        fun tokenRepository(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository

        @Singleton
        @Binds
        fun oAuthRemoteSource(oAuthRemoteSource: OAuthRemoteSource): OAuthSource
    }
}
