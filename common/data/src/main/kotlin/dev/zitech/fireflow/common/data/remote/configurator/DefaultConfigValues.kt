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

package dev.zitech.fireflow.common.data.remote.configurator

import dev.zitech.fireflow.common.domain.model.configurator.BooleanConfig
import dev.zitech.fireflow.common.domain.model.configurator.DoubleConfig
import dev.zitech.fireflow.common.domain.model.configurator.LongConfig
import dev.zitech.fireflow.common.domain.model.configurator.StringConfig

/**
 * Provides default configuration values for various configurations.
 */
internal object DefaultConfigValues {

    /**
     * Retrieves a map of default configuration values.
     *
     * @return A map containing the default configuration values.
     */
    fun getDefaultConfigValues(): Map<String, Any> {
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
