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

    data class Fatal(
        val throwable: Throwable,
        private val type: Type
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
        enum class Type {
            DISK,
            NETWORK,
            OS
        }
    }

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

    object AuthenticationProblem : Error(
        "The authentication type provided is not valid",
        R.string.authentication_problem
    )

    object BuildTypeUnsupported : Error(
        "",
        R.string.empty
    )

    object NoBrowserInstalled : Error(
        "No supported browser installed",
        R.string.no_browser_installed
    )

    object NullCurrentUserAccount : Error(
        "Null current user account",
        R.string.null_current_user_account
    )

    object NullUserAccount : Error(
        "Null user account",
        R.string.null_user_account
    )

    object NullUserAccountByState : Error(
        "Null user account by state",
        R.string.null_user_account_by_state
    )
}