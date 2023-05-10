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
import dev.zitech.fireflow.common.domain.model.user.UserAuthenticationType as UserAccountAuthenticationType
import dev.zitech.fireflow.common.domain.repository.user.UserAccountRepository
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.first

/**
 * Use case for updating the current user account.
 *
 * @property userAccountRepository The repository for managing user accounts.
 */
class UpdateCurrentUserAccountUseCase @Inject constructor(
    private val userAccountRepository: UserAccountRepository
) {
    /**
     * Invokes the use case to update the current user account with the specified fields.
     *
     * @param fields The fields to be updated in the current user account.
     *
     * @return The result of the operation.
     */
    suspend operator fun invoke(vararg fields: Field): OperationResult<Unit> =
        when (val result = userAccountRepository.getCurrentUserAccount().first()) {
            is Failure -> Failure(result.error)
            is Success -> handleResultSuccess(result.data, fields)
        }

    private suspend fun handleResultSuccess(
        currentUserAccount: UserAccount,
        fields: Array<out Field>
    ): OperationResult<Unit> {
        val updatedCurrentUserAccount = fields.fold(currentUserAccount) { acc, field ->
            when (field) {
                is AuthenticationType -> acc.copy(authenticationType = field.value)
                is Email -> acc.copy(email = field.value)
                is FireflyId -> acc.copy(fireflyId = field.value)
                is IsCurrentUserAccount -> acc.copy(isCurrentUserAccount = field.value)
                is Role -> acc.copy(role = field.value)
                is ServerAddress -> acc.copy(serverAddress = field.value)
                is State -> acc.copy(state = field.value)
                is Type -> acc.copy(type = field.value)
                is UserId -> acc.copy(userId = field.value)
            }
        }

        return userAccountRepository.updateUserAccount(updatedCurrentUserAccount)
    }

    /**
     * Represents a field that can be updated in the current user account.
     */
    sealed interface Field

    data class AuthenticationType(val value: UserAccountAuthenticationType?) : Field
    data class Email(val value: String?) : Field
    data class FireflyId(val value: String?) : Field
    data class IsCurrentUserAccount(val value: Boolean) : Field
    data class Role(val value: String?) : Field
    data class ServerAddress(val value: String) : Field
    data class State(val value: String?) : Field
    data class Type(val value: String?) : Field
    data class UserId(val value: Long) : Field
}
