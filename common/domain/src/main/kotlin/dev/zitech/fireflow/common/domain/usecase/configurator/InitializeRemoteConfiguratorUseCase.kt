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

package dev.zitech.fireflow.common.domain.usecase.configurator

import dev.zitech.fireflow.common.domain.repository.configurator.ConfiguratorRepository
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Use case for initializing the remote configurator.
 *
 * @property configuratorRepository The repository for accessing configurator data.
 */
class InitializeRemoteConfiguratorUseCase @Inject constructor(
    private val configuratorRepository: ConfiguratorRepository
) {
    /**
     * Invokes the use case to initialize the remote configurator.
     *
     * @return A flow of [OperationResult] indicating the result of the initialization.
     */
    operator fun invoke(): Flow<OperationResult<Unit>> =
        configuratorRepository.init()
}
