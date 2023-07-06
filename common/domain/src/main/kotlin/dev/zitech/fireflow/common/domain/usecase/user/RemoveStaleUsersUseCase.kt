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

import dev.zitech.fireflow.common.domain.repository.user.UserRepository
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Use case for removing stale users.
 *
 * This use case removes stale users based on different scenarios:
 * - `oauthJob` removes users with incomplete OAuth authentication.
 * - `patJob` removes users with incomplete PAT (Personal Access Token) authentication.
 * - `multipleRegistrationsJob` removes accounts that were attempted to be registered but are found duplicate.
 *
 * @property userRepository The repository for managing users.
 */
class RemoveStaleUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    /**
     * Invokes the use case to remove stale users.
     *
     * @return The result of the operation.
     */
    @Suppress("RedundantAsync", "TooGenericExceptionCaught")
    suspend operator fun invoke(): OperationResult<Unit> = coroutineScope {
        try {
            val oauthJob = async {
                userRepository.removeUsersWithStateAndNoTokenAndIdentifier()
            }.await()
            val patJob = async {
                userRepository.removeUsersWithStateAndTokenAndNoClientIdAndSecretAndIdentifier()
            }.await()
            val multipleRegistrationsJob = async {
                userRepository.removeUsersWithTokenAndNoIdentifier()
            }.await()

            when {
                oauthJob is Success && patJob is Success && multipleRegistrationsJob is Success ->
                    Success(Unit)
                oauthJob is Failure -> Failure(oauthJob.error)
                patJob is Failure -> Failure(patJob.error)
                else -> Failure((multipleRegistrationsJob as Failure).error)
            }
        } catch (e: Exception) {
            Failure(
                Error.Fatal(
                    throwable = e,
                    type = Error.Fatal.Type.OS
                )
            )
        }
    }
}
