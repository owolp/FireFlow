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
import dev.zitech.fireflow.common.domain.model.user.UserAuthenticationType
import dev.zitech.fireflow.common.domain.repository.user.UserRepository
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.core.result.OperationResult.Failure
import dev.zitech.fireflow.core.result.OperationResult.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.first

/**
 * Use case for updating the current user.
 *
 * @property userRepository The repository for managing users.
 */
class UpdateCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Invokes the use case to update the current user with the specified fields.
     *
     * @param fields The fields to be updated in the current user.
     *
     * @return The result of the operation.
     */
    suspend operator fun invoke(vararg fields: Field): OperationResult<Unit> =
        when (val result = userRepository.getCurrentUser().first()) {
            is Failure -> Failure(result.error)
            is Success -> handleResultSuccess(result.data, fields)
        }

    private suspend fun handleResultSuccess(
        currentUser: User,
        fields: Array<out Field>
    ): OperationResult<Unit> {
        val updatedCurrentUser = when (currentUser) {
            is User.Local -> {
                fields.fold(currentUser) { user, field ->
                    when (field) {
                        is IsCurrentUser -> user.copy(isCurrentUser = field.value)
                        is AuthenticationType,
                        is Email,
                        is FireflyId,
                        is Role,
                        is ServerAddress,
                        is State,
                        is Type,
                        is UserId -> user.copy()
                    }
                }
            }
            is User.Remote -> {
                fields.fold(currentUser) { user, field ->
                    when (field) {
                        is AuthenticationType -> user.copy(authenticationType = field.value)
                        is Email -> user.copy(email = field.value)
                        is FireflyId -> user.copy(fireflyId = field.value)
                        is IsCurrentUser -> user.copy(isCurrentUser = field.value)
                        is Role -> user.copy(role = field.value)
                        is ServerAddress -> user.copy(serverAddress = field.value)
                        is State -> user.copy(state = field.value)
                        is Type -> user.copy(type = field.value)
                        is UserId -> user.copy(id = field.value)
                    }
                }
            }
        }

        return userRepository.updateUser(updatedCurrentUser)
    }

    /**
     * Represents a field that can be updated in the current user.
     */
    sealed interface Field

    data class AuthenticationType(val value: UserAuthenticationType?) : Field
    data class Email(val value: String?) : Field
    data class FireflyId(val value: String?) : Field
    data class IsCurrentUser(val value: Boolean) : Field
    data class Role(val value: String?) : Field
    data class ServerAddress(val value: String) : Field
    data class State(val value: String?) : Field
    data class Type(val value: String?) : Field
    data class UserId(val value: Long) : Field
}
