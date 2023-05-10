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

package dev.zitech.fireflow.common.domain.repository.cache

import dev.zitech.fireflow.common.domain.model.cache.Cache

/**
 * Repository interface for managing caches.
 */
interface CacheRepository {

    /**
     * Adds a cache to the repository.
     *
     * @param cache The cache to add.
     */
    fun addCache(cache: Cache)

    /**
     * Invalidates all the caches in the repository.
     */
    fun invalidateCaches()

    /**
     * Removes a cache from the repository.
     *
     * @param cache The cache to remove.
     */
    fun removeCache(cache: Cache)
}
