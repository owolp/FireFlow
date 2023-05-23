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

package dev.zitech.fireflow.common.domain.model.user

import dev.zitech.fireflow.core.datafactory.DataFactory
import dev.zitech.fireflow.core.error.FireFlowError

sealed class User(
    open val isCurrentUser: Boolean
) {

    data class Local(
        override val isCurrentUser: Boolean
    ) : User(isCurrentUser)

    data class Remote(
        val authenticationType: UserAuthenticationType? = null,
        val email: String? = null,
        val fireflyId: String? = null,
        val role: String? = null,
        override val isCurrentUser: Boolean,
        val serverAddress: String,
        val state: String? = null,
        val type: String? = null,
        val userId: Long
    ) : User(isCurrentUser) {

        fun extractUrlAndPort(): UrlPortFormat {
            val regex = """^(?:https?://)?(?:www\.)?([-\w.]+)(?::(\d+))?$""".toRegex()
            val matchResult = regex.find(serverAddress)

            return matchResult?.destructured?.let { (hostname, port) ->
                try {
                    val parsedPort = port.toIntOrNull() ?: 80
                    UrlPortFormat.Valid(hostname, parsedPort)
                } catch (e: Exception) {
                    UrlPortFormat.Invalid
                }
            } ?: UrlPortFormat.Invalid
        }

        sealed interface UrlPortFormat {
            data class Valid(val hostname: String, val port: Int) : UrlPortFormat
            object Invalid : UrlPortFormat
            data class Error(val fireFlowError: FireFlowError) : UrlPortFormat
        }
    }

    companion object {
        private const val STATE_LENGTH = 10

        /**
         * Generates a random state string.
         *
         * @return A random state string.
         */
        fun getRandomState(): String =
            DataFactory.createRandomString(STATE_LENGTH)
    }
}
