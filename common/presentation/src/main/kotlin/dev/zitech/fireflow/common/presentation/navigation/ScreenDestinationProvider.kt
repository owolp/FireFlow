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

import dev.zitech.fireflow.common.domain.usecase.user.GetCurrentUserUseCase
import dev.zitech.fireflow.common.domain.usecase.user.GetUsersUseCase
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

/**
 * Provides screen destinations for deep linking based on the current user.
 *
 * This class retrieves the current user using the [GetCurrentUserUseCase] and emits
 * a flow of [DeepLinkScreenDestination] representing the appropriate screen destination based on the
 * user state.
 *
 * @param getCurrentUserUseCase The use case to retrieve the current user.
 * @param getUsersUseCase The use case to retrieve a list of users.
 */
class ScreenDestinationProvider @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUsersUseCase: GetUsersUseCase
) {

    /**
     * Provides a flow of [DeepLinkScreenDestination] representing the screen destination for deep linking.
     *
     * This flow emits screen destinations based on the current user state. It sends
     * [DeepLinkScreenDestination.Current] if the current user is available,
     * [DeepLinkScreenDestination.Accounts] if there are other users available,
     * [DeepLinkScreenDestination.Welcome] if there are no users available, and
     * [DeepLinkScreenDestination.Error] if there is an error retrieving the user.
     *
     * @return A flow of [DeepLinkScreenDestination] representing the screen destination.
     */
    operator fun invoke(): Flow<DeepLinkScreenDestination> = channelFlow {
        getCurrentUserUseCase()
            .onEach { result ->
                when (result) {
                    is Success -> send(DeepLinkScreenDestination.Current)
                    is Failure -> {
                        when (result.error) {
                            is Error.NullCurrentUser -> {
                                handleNullCurrentUser()
                            }
                            else -> send(DeepLinkScreenDestination.Error(result.error))
                        }
                    }
                }
            }.collect()
    }

    private suspend fun ProducerScope<DeepLinkScreenDestination>.handleNullCurrentUser() {
        getUsersUseCase().first()
            .onSuccess { users ->
                if (users.isNotEmpty()) {
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
