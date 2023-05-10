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

import dev.zitech.fireflow.common.domain.model.configurator.BooleanConfig
import dev.zitech.fireflow.common.domain.repository.configurator.ConfiguratorRepository
import javax.inject.Inject

/**
 * Use case for getting the boolean configurations.
 *
 * @property configuratorRepository The repository for accessing configurator data.
 */
class GetBooleanConfigsUseCase @Inject constructor(
    private val configuratorRepository: ConfiguratorRepository
) {

    /**
     * Invokes the use case to retrieve the boolean configurations.
     *
     * @return The list of boolean configurations.
     */
    operator fun invoke(): List<BooleanConfig> =
        configuratorRepository.getBooleanConfigs()
}
