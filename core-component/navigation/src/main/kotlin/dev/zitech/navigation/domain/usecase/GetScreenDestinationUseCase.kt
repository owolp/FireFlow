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

package dev.zitech.navigation.domain.usecase

import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.persistence.domain.model.exception.NullCurrentUserAccountException
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.GetUserAccountsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GetScreenDestinationUseCase @Inject constructor(
    private val getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase
) {

    sealed class Destination {
        object Accounts : Destination()
        object Error : Destination()
        object Current : Destination()
        object Welcome : Destination()
    }

    operator fun invoke(): Flow<Destination> = flow {
        getCurrentUserAccountUseCase()
            .onEach { result ->
                when (result) {
                    is DataResult.Success -> emit(Destination.Current)
                    is DataResult.Error -> {
                        when (result.cause) {
                            NullCurrentUserAccountException -> {
                                handleNullCurrentUserAccount()
                            }
                            else -> emit(Destination.Error)
                        }
                    }
                }
            }.collect()
    }

    private suspend fun FlowCollector<Destination>.handleNullCurrentUserAccount() {
        when (val result = getUserAccountsUseCase().first()) {
            is DataResult.Success -> {
                if (result.value.isNotEmpty()) {
                    emit(Destination.Accounts)
                } else {
                    emit(Destination.Welcome)
                }
            }
            is DataResult.Error -> emit(Destination.Error)
        }
    }
}
