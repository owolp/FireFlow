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

package dev.zitech.navigation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.GetUserAccountsUseCase
import dev.zitech.navigation.domain.usecase.GetScreenDestinationUseCase
import javax.inject.Singleton

@Deprecated("Modules")
internal interface NavigationModule {

    @InstallIn(SingletonComponent::class)
    @Module
    object NavigationSingletonProvidesModule {

        @Singleton
        @Provides
        fun getScreenDestinationUseCase(
            getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
            getUserAccountsUseCase: GetUserAccountsUseCase
        ): GetScreenDestinationUseCase =
            GetScreenDestinationUseCase(
                getCurrentUserAccountUseCase,
                getUserAccountsUseCase
            )
    }
}
