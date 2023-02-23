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

package dev.zitech.onboarding.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.zitech.core.common.domain.validator.Validator
import dev.zitech.onboarding.di.annotation.ValidatorClientId
import dev.zitech.onboarding.di.annotation.ValidatorClientSecret
import dev.zitech.onboarding.di.annotation.ValidatorPat
import dev.zitech.onboarding.di.annotation.ValidatorServerAddress
import dev.zitech.onboarding.domain.validator.ClientIdValidator
import dev.zitech.onboarding.domain.validator.ClientSecretValidator
import dev.zitech.onboarding.domain.validator.PatValidator
import dev.zitech.onboarding.domain.validator.ServerAddressValidator

internal interface OnboardingModule {

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
}
