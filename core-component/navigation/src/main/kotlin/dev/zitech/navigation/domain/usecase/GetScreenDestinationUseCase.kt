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

package dev.zitech.navigation.domain.usecase

import dev.zitech.core.common.domain.exception.FireFlowException
import dev.zitech.core.common.domain.model.WorkError
import dev.zitech.core.common.domain.model.WorkSuccess
import dev.zitech.core.common.domain.model.onError
import dev.zitech.core.common.domain.model.onSuccess
import dev.zitech.core.common.domain.navigation.DeepLinkScreenDestination
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.GetUserAccountsUseCase
import javax.inject.Inject
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach

class GetScreenDestinationUseCase @Inject constructor(
    private val getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase
) {

    operator fun invoke(): Flow<DeepLinkScreenDestination> = channelFlow {
        getCurrentUserAccountUseCase()
            .onEach { result ->
                when (result) {
                    is WorkSuccess -> send(DeepLinkScreenDestination.Current)
                    is WorkError -> {
                        when (result.fireFlowException) {
                            is FireFlowException.NullCurrentUserAccount -> {
                                handleNullCurrentUserAccount()
                            }
                            else -> send(DeepLinkScreenDestination.Error(result.fireFlowException))
                        }
                    }
                }
            }.collect()
    }

    private suspend fun ProducerScope<DeepLinkScreenDestination>.handleNullCurrentUserAccount() {
        getUserAccountsUseCase().first()
            .onSuccess { userAccounts ->
                if (userAccounts.isNotEmpty()) {
                    send(DeepLinkScreenDestination.Accounts)
                } else {
                    send(DeepLinkScreenDestination.Welcome)
                }
            }
            .onError { exception ->
                send(DeepLinkScreenDestination.Error(exception))
            }
    }
}
