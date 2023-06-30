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

package dev.zitech.fireflow.core.error

import dev.zitech.fireflow.core.R

typealias FireFlowError = Error

/**
 * Sealed class for representing errors in the application.
 *
 * @property debugText The debug text providing additional information about the error.
 * @property uiResId The resource ID of the error message to be displayed in the user interface.
 */
sealed class Error(
    val debugText: String,
    val uiResId: Int
) {

    data class FailedToFetch(
        val key: Any? = null,
        private val type: Type
    ) : Error(
        "Failed to fetch remote config for key:$key",
        R.string.failed_to_fetched
    ) {
        enum class Type {
            INIT,
            STRING,
            BOOLEAN,
            DOUBLE,
            LONG
        }
    }

    /**
     * Represents a fatal error, which is a specific type of [Error].
     *
     * @property throwable The [Throwable] associated with the error.
     * @property type The type of the fatal error.
     */
    data class Fatal(
        val throwable: Throwable,
        val type: Type
    ) : Error(
        when (type) {
            Type.DISK -> "Disk exception:${throwable.message}"
            Type.NETWORK -> "Network exception:${throwable.message}"
            Type.OS -> "OS exception:${throwable.message}"
        },
        when (type) {
            Type.DISK -> R.string.disk_exception
            Type.NETWORK -> R.string.network_exception
            Type.OS -> R.string.os_exception
        }
    ) {
        /**
         * Represents the type of a fatal error in the [Fatal] data class.
         */
        enum class Type {
            /**
             * Indicates a disk-related fatal error.
             */
            DISK,

            /**
             * Indicates a network-related fatal error.
             */
            NETWORK,

            /**
             * Indicates an operating system-related fatal error.
             */
            OS
        }

        /**
         * Converts the fatal error to a [UserVisible] error.
         *
         * @return The corresponding [UserVisible] error.
         */
        fun toUserVisible() = UserVisible(this.throwable.message)
    }

    data class OperationNotSupported(val info: String? = null) : Error(
        "Operation not supported. ${info.orEmpty()}",
        R.string.empty
    )

    data class TokenFailed(
        private val message: String?
    ) : Error(
        message.orEmpty(),
        R.string.token_failed
    )

    data class UserVisible(
        val message: String?
    ) : Error(
        message.orEmpty(),
        R.string.empty
    )

    data class UserWithServerAlreadyExists(
        val email: String,
        val serverAddress: String
    ) : Error(
        "User with specific email and server address already exists",
        R.string.user_with_email_and_server_address_exists
    )

    object AuthenticationProblem : Error(
        "The authentication type provided is not valid",
        R.string.authentication_problem
    )

    object BuildTypeUnsupported : Error(
        "",
        R.string.empty
    )

    object LocalUserTypeNotSupported : Error(
        "Local user type not supported",
        R.string.empty
    )

    object NoBrowserInstalled : Error(
        "No supported browser installed",
        R.string.no_browser_installed
    )

    object NullCurrentUser : Error(
        "Null current user",
        R.string.null_current_user
    )

    object NullUser : Error(
        "Null user",
        R.string.null_user
    )

    object NullUserByState : Error(
        "Null user by state",
        R.string.null_user_by_state
    )
}
