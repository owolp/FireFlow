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

package dev.zitech.core.persistence.data.repository.cache

import androidx.annotation.VisibleForTesting
import dev.zitech.core.persistence.domain.model.cache.Cache
import dev.zitech.core.persistence.domain.repository.cache.CacheRepository
import javax.inject.Inject

internal class CacheRepositoryImpl @Inject constructor() : CacheRepository {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val caches = mutableListOf<Cache>()

    override fun addCache(cache: Cache) {
        if (!caches.contains(cache)) {
            caches.add(cache)
        }
    }

    override fun removeCache(cache: Cache) {
        if (caches.contains(cache)) {
            caches.remove(cache)
        }
    }

    override fun invalidateCaches() {
        caches.forEach {
            it.invalidate()
        }
    }
}
