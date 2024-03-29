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

package dev.zitech.fireflow.common.domain.usecase.profile

import dev.zitech.fireflow.common.domain.model.profile.FireflyProfile
import dev.zitech.fireflow.common.domain.repository.profile.FireflyProfileRepository
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

/**
 * Use case for retrieving the Firefly profile.
 *
 * @property fireflyProfileRepository The repository for accessing Firefly profile data.
 */
class GetFireflyProfileUseCase @Inject constructor(
    private val fireflyProfileRepository: FireflyProfileRepository
) {
    /**
     * Invokes the use case to retrieve the Firefly profile.
     *
     * @return An [OperationResult] with the Firefly profile data.
     */
    suspend operator fun invoke(): OperationResult<FireflyProfile> =
        fireflyProfileRepository.getFireflyProfile()
}
