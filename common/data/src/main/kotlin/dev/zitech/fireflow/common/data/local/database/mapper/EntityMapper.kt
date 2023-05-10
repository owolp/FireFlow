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

package dev.zitech.fireflow.common.data.local.database.mapper

/**
 * An interface for mapping data objects (D) to entity objects (E).
 *
 * @param D The type of the data object.
 * @param E The type of the entity object.
 */
interface EntityMapper<in D, out E> {

    /**
     * Maps a data object to an entity object.
     *
     * @param input The input data object to be mapped.
     * @return The corresponding entity object.
     */
    fun toEntity(input: D): E
}
