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

internal class GetRandomUsernameUseCase @Inject constructor(
    private val usernameRepository: UsernameRepository
) {

    suspend operator fun invoke(): OperationResult<String> = coroutineScope {
        val adjectives = async { usernameRepository.getAdjectives() }
        val nouns = async { usernameRepository.getNouns() }
        getUsername(adjectives.await(), nouns.await())
    }

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
