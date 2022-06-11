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

package dev.zitech.core.persistence.domain.model.cache

import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.persistence.domain.repository.cache.CacheRepository
import java.math.BigInteger

open class InMemoryCache<T : Any>(
    private val cacheRepository: CacheRepository,
    private val lifetimeMillis: Int = Int.MAX_VALUE
) : Cache {

    private var cacheExpirationTimeMillis: BigInteger? = null

    override fun invalidate() {
        Logger.d(this::class.java.simpleName, "invalidate cache")
        data = null
        cacheRepository.removeCache(this)
    }

    var data: T? = null
        set(value) {
            field = value
            if (value != null) {
                setExpirationTime()
                cacheRepository.addCache(this)
            }
        }
        get() {
            checkCacheExpired()
            return field
        }

    private fun checkCacheExpired() {
        cacheExpirationTimeMillis?.let { expirationTime ->
            if (System.currentTimeMillis().toBigInteger() > expirationTime) {
                invalidate()
            }
        }
    }

    private fun setExpirationTime() {
        cacheExpirationTimeMillis = (System.currentTimeMillis() + lifetimeMillis).toBigInteger()
    }
}
