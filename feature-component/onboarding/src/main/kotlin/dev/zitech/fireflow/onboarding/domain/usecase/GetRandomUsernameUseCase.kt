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

package dev.zitech.fireflow.onboarding.domain.usecase

import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.onboarding.domain.repository.UsernameRepository
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Use case for generating a random username.
 *
 * This use case is responsible for generating a random username using adjectives
 * and nouns obtained from the [UsernameRepository]. It combines a random adjective
 * and noun to create a username. It interacts with the [UsernameRepository] to
 * retrieve the necessary parts.
 *
 * @property usernameRepository The repository for managing usernames.
 */
internal class GetRandomUsernameUseCase @Inject constructor(
    private val usernameRepository: UsernameRepository
) {

    /**
     * Invokes the use case to generate a random username.
     *
     * This suspend function is invoked to generate a random username by combining
     * a random adjective and noun obtained from the [UsernameRepository]. It
     * asynchronously retrieves the adjectives and nouns and then calls the
     * [getUsername] function to generate the username.
     *
     * @return An [OperationResult] representing the result of the operation,
     *         containing the generated username if successful, or an error if unsuccessful.
     */
    suspend operator fun invoke(): OperationResult<String> = coroutineScope {
        val adjectives = async { usernameRepository.getAdjectives() }
        val nouns = async { usernameRepository.getNouns() }
        getUsername(adjectives.await(), nouns.await())
    }

    /**
     * Generates a username based on the provided adjectives and nouns.
     *
     * This private function takes the retrieved [adjectives] and [nouns] and
     * generates a username by combining a random adjective and noun. It returns
     * an [OperationResult] that contains the generated username if both the
     * adjectives and nouns retrieval operations were successful, or an error
     * if either of the operations failed.
     *
     * @param adjectives The retrieved list of adjectives.
     * @param nouns The retrieved list of nouns.
     * @return An [OperationResult] representing the result of the operation,
     *         containing the generated username if successful, or an error if unsuccessful.
     */
    private fun getUsername(
        adjectives: OperationResult<List<String>>,
        nouns: OperationResult<List<String>>
    ): OperationResult<String> = when {
        adjectives is OperationResult.Success && nouns is OperationResult.Success -> {
            val firstName = adjectives.data.random()
            val lastName = nouns.data.random()
            OperationResult.Success("${firstName}_$lastName")
        }
        adjectives is OperationResult.Failure -> OperationResult.Failure(adjectives.error)
        nouns is OperationResult.Failure -> OperationResult.Failure(nouns.error)
        else -> OperationResult.Failure(Error.OperationNotSupported())
    }
}
