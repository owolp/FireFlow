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

package dev.zitech.fireflow.common.domain.usecase.user

import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.common.domain.repository.user.UserRepository
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.first

/**
 * Use case for retrieving the access token of the current user.
 *
 * @property userRepository The repository for managing users.
 */
class GetCurrentUserAccessTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Invokes the use case to retrieve the access token of the current user.
     *
     * @return The access token of the current user, or null if an error occurred or the access token is
     * unavailable.
     */
    suspend operator fun invoke(): String? =
        when (val result = userRepository.getCurrentUser().first()) {
            is Success -> {
                when (val user = result.data) {
                    is User.Local -> null
                    is User.Remote -> user.authenticationType?.accessToken
                }
            }
            is Failure -> null
        }
}
