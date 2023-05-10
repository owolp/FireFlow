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

import dev.zitech.fireflow.common.domain.model.user.UserAccount
import dev.zitech.fireflow.common.domain.repository.user.UserAccountRepository
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

/**
 * Use case for retrieving a user account by its state.
 *
 * @property userAccountRepository The repository for managing user accounts.
 */
class GetUserAccountByStateUseCase @Inject constructor(
    private val userAccountRepository: UserAccountRepository
) {
    /**
     * Invokes the use case to retrieve a user account by its state.
     *
     * @param state The state of the user account.
     * @return An [OperationResult] with the user account corresponding to the state.
     */
    suspend operator fun invoke(state: String): OperationResult<UserAccount> =
        userAccountRepository.getUserAccountByState(state)
}
