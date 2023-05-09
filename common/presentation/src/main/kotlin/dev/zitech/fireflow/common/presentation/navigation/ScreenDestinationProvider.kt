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

package dev.zitech.fireflow.common.presentation.navigation

import dev.zitech.fireflow.common.domain.usecase.user.GetCurrentUserAccountUseCase
import dev.zitech.fireflow.common.domain.usecase.user.GetUserAccountsUseCase
import dev.zitech.fireflow.common.presentation.navigation.deeplink.DeepLinkScreenDestination
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import dev.zitech.fireflow.core.result.onFailure
import dev.zitech.fireflow.core.result.onSuccess
import javax.inject.Inject
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach

class ScreenDestinationProvider @Inject constructor(
    private val getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase
) {

    operator fun invoke(): Flow<DeepLinkScreenDestination> = channelFlow {
        getCurrentUserAccountUseCase()
            .onEach { result ->
                when (result) {
                    is Success -> send(DeepLinkScreenDestination.Current)
                    is Failure -> {
                        when (result.error) {
                            is Error.NullCurrentUserAccount -> {
                                handleNullCurrentUserAccount()
                            }

                            else -> send(DeepLinkScreenDestination.Error(result.error))
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
            .onFailure { error ->
                send(DeepLinkScreenDestination.Error(error))
            }
    }
}
