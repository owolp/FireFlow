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

package dev.zitech.fireflow.onboarding.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dev.zitech.fireflow.common.presentation.validator.Validator
import dev.zitech.fireflow.core.dispatcher.AppDispatchers
import dev.zitech.fireflow.onboarding.data.source.RawResourceSource
import dev.zitech.fireflow.onboarding.data.source.ResourceSource
import dev.zitech.fireflow.onboarding.di.annotation.ValidatorClientId
import dev.zitech.fireflow.onboarding.di.annotation.ValidatorClientSecret
import dev.zitech.fireflow.onboarding.di.annotation.ValidatorPat
import dev.zitech.fireflow.onboarding.di.annotation.ValidatorServerAddress
import dev.zitech.fireflow.onboarding.domain.repository.UsernameRepository
import dev.zitech.fireflow.onboarding.domain.repository.UsernameRepositoryImpl
import dev.zitech.fireflow.onboarding.domain.validator.ClientIdValidator
import dev.zitech.fireflow.onboarding.domain.validator.ClientSecretValidator
import dev.zitech.fireflow.onboarding.domain.validator.PatValidator
import dev.zitech.fireflow.onboarding.domain.validator.ServerAddressValidator
import javax.inject.Singleton

internal interface OnboardingModule {

    @InstallIn(SingletonComponent::class)
    @Module
    interface SingletonBinds {

        @Singleton
        @Binds
        fun usernameRepository(usernameRepositoryImpl: UsernameRepositoryImpl): UsernameRepository
    }

    @InstallIn(ViewModelComponent::class)
    @Module
    interface ViewModelComponentModule {

        @ValidatorClientId
        @ViewModelScoped
        @Binds
        fun clientIdValidator(clientIdValidator: ClientIdValidator): Validator<String>

        @ValidatorClientSecret
        @ViewModelScoped
        @Binds
        fun clientSecretValidator(clientSecretValidator: ClientSecretValidator): Validator<String>

        @ValidatorPat
        @ViewModelScoped
        @Binds
        fun patValidator(patValidator: PatValidator): Validator<String>

        @ValidatorServerAddress
        @ViewModelScoped
        @Binds
        fun serverAddressValidator(
            serverAddressValidator: ServerAddressValidator
        ): Validator<String>
    }

    @InstallIn(SingletonComponent::class)
    @Module
    object SingletonProvides {

        @Singleton
        @Provides
        fun rawResourceSource(
            appDispatchers: AppDispatchers,
            @ApplicationContext applicationContext: Context
        ): ResourceSource = RawResourceSource(appDispatchers, applicationContext.resources)
    }
}
