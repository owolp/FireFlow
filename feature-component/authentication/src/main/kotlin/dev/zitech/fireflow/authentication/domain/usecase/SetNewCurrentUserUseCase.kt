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

package dev.zitech.fireflow.authentication.domain.usecase

import dev.zitech.fireflow.common.domain.repository.user.UserRepository
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

internal class SetNewCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(userId: Long): OperationResult<Unit> =
        when (val result = userRepository.updateUserCurrentStatus(userId)) {
            is OperationResult.Failure -> OperationResult.Failure(result.error)
            is OperationResult.Success -> {
                if (result.data > NO_AFFECTED_ROWS) {
                    OperationResult.Success(Unit)
                } else {
                    OperationResult.Failure(Error.NullUser)
                }
            }
        }

    private companion object {
        const val NO_AFFECTED_ROWS = 0
    }
}
