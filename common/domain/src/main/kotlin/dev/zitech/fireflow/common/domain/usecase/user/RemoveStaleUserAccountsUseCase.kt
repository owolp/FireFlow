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

import dev.zitech.fireflow.common.domain.repository.user.UserAccountRepository
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.work.OperationResult
import dev.zitech.fireflow.core.work.OperationResult.Failure
import dev.zitech.fireflow.core.work.OperationResult.Success
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class RemoveStaleUserAccountsUseCase @Inject constructor(
    private val userAccountRepository: UserAccountRepository
) {

    @Suppress("RedundantAsync", "TooGenericExceptionCaught")
    suspend operator fun invoke(): OperationResult<Unit> = coroutineScope {
        try {
            val jobOAuth = async {
                userAccountRepository.removeUserAccountsWithStateAndNoToken()
            }.await()
            val jobPat = async {
                userAccountRepository.removeUserAccountsWithStateAndTokenAndNoClientIdAndSecret()
            }.await()

            if (jobOAuth is Success && jobPat is Success) {
                Success(Unit)
            } else if (jobOAuth is Failure) {
                Failure(jobOAuth.error)
            } else {
                Failure((jobPat as Failure).error)
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
