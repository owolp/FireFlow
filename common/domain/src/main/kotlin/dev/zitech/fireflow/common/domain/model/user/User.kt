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

/**
 * Represents a user entity.
 *
 * @property id The unique ID of the user.
 * @property isCurrentUser Indicates whether the user is the current user.
 */
sealed class User(
    open val id: Long,
    open val isCurrentUser: Boolean
) {

    /**
     * Returns the identification string of the user.
     *
     * This function retrieves the identification string of the user based on the type.
     * If the user is of type [Local], the username will be returned.
     * If the user is of type [Remote], the email will be returned. If the email is null, an empty
     * string will be returned.
     *
     * @return The identification string of the user.
     */
    fun retrieveIdentifier(): String = when (this) {
        is Local -> this.username
        is Remote -> this.email.orEmpty()
    }

    /**
     * Returns the initial character of the user.
     *
     * This function retrieves the initial character of the user based on the type.
     * If the user is of type [Local], the first character of the uppercase username will be returned.
     * If the user is of type [Remote], the first character of the uppercase email will be returned.
     * If the email is null, an empty string is considered, and the first character will be returned as well.
     *
     * @return The initial character of the user.
     */
    fun retrieveInitial(): Char = when (this) {
        is Local -> this.username.uppercase().first()
        is Remote -> this.email.orEmpty().uppercase().first()
    }

    /**
     * Returns the server address associated with the user.
     *
     * This function retrieves the server address associated with the user based on the type.
     * If the user is of type [Local], an empty string will be returned.
     * If the user is of type [Remote], the server address will be returned.
     *
     * @return The server address associated with the user.
     */
    fun retrieveServerAddress(): String = when (this) {
        is Local -> ""
        is Remote -> this.serverAddress
    }

    /**
     * Represents a local user.
     *
     * @param isCurrentUser Indicates whether the user is the current user.
     * @param id The user ID.
     * @param username The user name.
     */
    data class Local(
        override val id: Long,
        override val isCurrentUser: Boolean,
        val username: String
    ) : User(id, isCurrentUser)

    /**
     * Represents a remote user.
     * @param isCurrentUser Indicates whether the user is the current user.
     * @param id The user ID.
     * @param authenticationType The authentication type of the user.
     * @param connectivityNotification Indicates whether the user has enabled connectivity notification.
     * @param email The email address of the user.
     * @param fireflyId The Firefly ID of the user.
     * @param role The role of the user.
     * @param isCurrentUser Indicates whether the user is the current user.
     * @param serverAddress The server address associated with the user.
     * @param state The state of the user.
     * @param type The type of the user.
     */
    data class Remote(
        override val id: Long,
        override val isCurrentUser: Boolean,
        val authenticationType: UserAuthenticationType? = null,
        val connectivityNotification: Boolean,
        val email: String? = null,
        val fireflyId: String? = null,
        val role: String? = null,
        val serverAddress: String,
        val state: String? = null,
        val type: String? = null
    ) : User(id, isCurrentUser) {

        /**
         * Extracts the URL and port from the server address.
         *
         * @return The URL and port in the format [UrlPortFormat].
         */
        fun extractUrlAndPort(): UrlPortFormat {
            val regex = """^(?:https?://)?(?:www\.)?([-\w.]+)(?::(\d+))?$""".toRegex()
            val matchResult = regex.find(serverAddress)

            @Suppress("TooGenericExceptionCaught", "SwallowedException")
            return matchResult?.destructured?.let { (hostname, port) ->
                try {
                    val parsedPort = port.toIntOrNull() ?: DEFAULT_URL_PORT
                    UrlPortFormat.Valid(hostname, parsedPort)
                } catch (e: Exception) {
                    UrlPortFormat.Invalid
                }
            } ?: UrlPortFormat.Invalid
        }

        /**
         * Represents the format of URL and port.
         */
        sealed interface UrlPortFormat {
            /**
             * Represents a valid URL and port combination.
             *
             * @param hostname The hostname of the URL.
             * @param port The port number.
             */
            data class Valid(val hostname: String, val port: Int) : UrlPortFormat

            /**
             * Represents an invalid URL and port combination.
             */
            object Invalid : UrlPortFormat

            /**
             * Represents an error in the URL and port format.
             *
             * @param fireFlowError The error encountered.
             */
            data class Error(val fireFlowError: FireFlowError) : UrlPortFormat
        }
    }

    companion object {
        private const val STATE_LENGTH = 10
        private const val DEFAULT_URL_PORT = 80

        /**
         * Generates a random state string.
         *
         * @return A random state string.
         */
        fun getRandomState(): String =
            DataFactory.createRandomString(STATE_LENGTH)
    }
}
