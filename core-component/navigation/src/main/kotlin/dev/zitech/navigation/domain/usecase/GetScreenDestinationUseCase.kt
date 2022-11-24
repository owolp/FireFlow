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
import dev.zitech.core.common.domain.navigation.DeepLinkScreenDestination
import dev.zitech.core.persistence.domain.model.exception.NullCurrentUserAccountException
import dev.zitech.core.persistence.domain.usecase.database.GetCurrentUserAccountUseCase
import dev.zitech.core.persistence.domain.usecase.database.GetUserAccountsUseCase
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetScreenDestinationUseCase @Inject constructor(
    private val getCurrentUserAccountUseCase: GetCurrentUserAccountUseCase,
    private val getUserAccountsUseCase: GetUserAccountsUseCase
) {

    operator fun invoke(): Flow<DeepLinkScreenDestination> = channelFlow {
        getCurrentUserAccountUseCase()
            .onEach { result ->
                when (result) {
                    is DataResult.Success -> send(DeepLinkScreenDestination.Current)
                    is DataResult.Error -> {
                        when (result.cause) {
                            NullCurrentUserAccountException -> {
                                handleNullCurrentUserAccount()
                            }
                            else -> send(DeepLinkScreenDestination.Error)
                        }
                    }
                }
            }.collect()
    }

    private suspend fun ProducerScope<DeepLinkScreenDestination>.handleNullCurrentUserAccount() {
        when (val result = getUserAccountsUseCase().first()) {
            is DataResult.Success -> {
                if (result.value.isNotEmpty()) {
                    send(DeepLinkScreenDestination.Accounts)
                } else {
                    send(DeepLinkScreenDestination.Welcome)
                }
            }
            is DataResult.Error -> send(DeepLinkScreenDestination.Error)
        }
    }
}
