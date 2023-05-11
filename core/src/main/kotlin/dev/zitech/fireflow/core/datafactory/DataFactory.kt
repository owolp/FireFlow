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

package dev.zitech.fireflow.core.datafactory

import java.util.UUID
import kotlin.random.Random

/**
 * Provides utility functions to generate random data.
 */
object DataFactory {

    /**
     * Generates a random string of the specified length.
     *
     * @param length The length of the random string. If not specified, a UUID string is generated.
     * @return The generated random string.
     */
    fun createRandomString(length: Int? = null): String = if (length != null) {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    } else {
        UUID.randomUUID().toString()
    }

    /**
     * Generates a random integer within the specified range.
     *
     * @param min The minimum value (inclusive) of the random integer. Default is 0.
     * @param max The maximum value (exclusive) of the random integer. Default is Int.MAX_VALUE.
     * @return The generated random integer.
     */
    fun createRandomInt(min: Int = 0, max: Int = Int.MAX_VALUE): Int = Random.nextInt(min, max)

    /**
     * Generates a random long within the specified range.
     *
     * @param min The minimum value (inclusive) of the random long. Default is 0.
     * @param max The maximum value (exclusive) of the random long. Default is Long.MAX_VALUE.
     * @return The generated random long.
     */
    fun createRandomLong(min: Long = 0, max: Long = Long.MAX_VALUE): Long =
        Random.nextLong(min, max)

    /**
     * Generates a random double within the specified range.
     *
     * @param min The minimum value (inclusive) of the random double. Default is 0.0.
     * @param max The maximum value (exclusive) of the random double. Default is Double.MAX_VALUE.
     * @return The generated random double.
     */
    fun createRandomDouble(min: Double = 0.0, max: Double = Double.MAX_VALUE): Double =
        Random.nextDouble(min, max)

    /**
     * Generates a random boolean value.
     *
     * @return The generated random boolean.
     */
    fun createRandomBoolean(): Boolean = Random.nextBoolean()

    /**
     * Creates an exception with the specified message.
     *
     * @param message The message for the exception. If not specified, a random string is used.
     * @return The created exception.
     */
    fun createException(
        message: String = createRandomString()
    ): Exception = Exception(message)
}
