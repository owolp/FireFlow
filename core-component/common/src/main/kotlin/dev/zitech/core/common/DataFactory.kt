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

package dev.zitech.core.common

import java.util.UUID
import kotlin.random.Random

object DataFactory {

    fun createRandomString(length: Int? = null): String = if (length != null) {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    } else {
        UUID.randomUUID().toString()
    }

    fun createRandomInt(min: Int = 0, max: Int = Int.MAX_VALUE): Int = Random.nextInt(min, max)

    fun createRandomLong(min: Long = 0, max: Long = Long.MAX_VALUE): Long =
        Random.nextLong(min, max)

    fun createRandomDouble(min: Double = 0.0, max: Double = Double.MAX_VALUE): Double =
        Random.nextDouble(min, max)

    fun createRandomBoolean(): Boolean = Random.nextBoolean()

    fun createException(
        message: String = createRandomString()
    ): Exception = Exception(message)
}
