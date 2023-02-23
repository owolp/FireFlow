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

package dev.zitech.core.common.domain.cache

import androidx.annotation.VisibleForTesting
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.common.framework.time.TimeHelper
import java.math.BigInteger

open class InMemoryCache<T : Any>(
    private val cacheRepository: CacheRepository,
    private val lifetimeMillis: Int = Int.MAX_VALUE
) : Cache {

    private val tag = Logger.tag(this::class.java)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var cacheExpirationTimeMillis: BigInteger? = null

    override fun invalidate() {
        Logger.i(tag, "invalidate cache")
        clearExpirationTime()
        data = null
        cacheRepository.removeCache(this)
    }

    var data: T? = null
        set(value) {
            field = value
            if (value != null) {
                Logger.i(tag, "set cache")
                Logger.d(tag, "set cache $value")
                setExpirationTime()
                cacheRepository.addCache(this)
            } else {
                Logger.i(tag, "clear cache")
                clearExpirationTime()
                cacheRepository.removeCache(this)
            }
        }
        get() {
            Logger.d(tag, "get cache $field")
            checkCacheExpired()
            return field
        }

    private fun checkCacheExpired() {
        cacheExpirationTimeMillis?.let { expirationTime ->
            if (TimeHelper.getCurrentTime().toBigInteger() > expirationTime) {
                invalidate()
            }
        }
    }

    private fun setExpirationTime() {
        if (lifetimeMillis < 0) {
            throw IllegalStateException(
                "lifetimeMillis not supported, " +
                    "must be a positive number=$lifetimeMillis"
            )
        }
        cacheExpirationTimeMillis = (TimeHelper.getCurrentTime() + lifetimeMillis).toBigInteger()
    }

    private fun clearExpirationTime() {
        cacheExpirationTimeMillis = null
    }
}
