/*
 * Copyright (C) 2022 Zitech Ltd.
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

package dev.zitech.core.remoteconfig.domain.usecase

import dev.zitech.core.remoteconfig.domain.model.BooleanConfig
import dev.zitech.core.remoteconfig.domain.model.DoubleConfig
import dev.zitech.core.remoteconfig.domain.model.LongConfig
import dev.zitech.core.remoteconfig.domain.model.StringConfig
import javax.inject.Inject

internal class GetDefaultConfigValuesUseCase @Inject constructor() {

    operator fun invoke(): Map<String, Any> {
        val properties = mutableMapOf<String, Any>()

        BooleanConfig.values().forEach {
            properties[it.key] = it.defaultValue
        }
        DoubleConfig.values().forEach {
            properties[it.key] = it.defaultValue
        }
        LongConfig.values().forEach {
            properties[it.key] = it.defaultValue
        }
        StringConfig.values().forEach {
            properties[it.key] = it.defaultValue
        }

        return properties
    }
}
