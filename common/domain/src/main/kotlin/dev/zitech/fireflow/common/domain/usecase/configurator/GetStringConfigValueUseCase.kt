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

import dev.zitech.fireflow.common.domain.model.configurator.StringConfig
import dev.zitech.fireflow.common.domain.repository.configurator.ConfiguratorRepository
import dev.zitech.fireflow.core.work.WorkError
import dev.zitech.fireflow.core.work.WorkSuccess
import javax.inject.Inject

class GetStringConfigValueUseCase @Inject constructor(
    private val configuratorRepository: ConfiguratorRepository
) {

    suspend operator fun invoke(config: StringConfig): String =
        when (val result = configuratorRepository.getStringValue(config)) {
            is WorkSuccess -> result.data
            is WorkError -> config.defaultValue
        }
}
