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

package dev.zitech.core.common.domain.exception

import androidx.annotation.StringRes
import dev.zitech.core.common.R

sealed class FireFlowException(
    @StringRes val uiResId: Int,
    val text: String
) {

    object BuildTypeUnsupported : FireFlowException(R.string.empty, "")

    object NoBrowserInstalledException : FireFlowException(
        R.string.no_browser_installed,
        "No supported browser installed"
    )

    data class Fatal(
        val throwable: Throwable,
        private val type: Type
    ) : FireFlowException(
        when (type) {
            Type.DISK -> R.string.disk_exception
            Type.NETWORK -> R.string.network_exception
            Type.OS -> R.string.os_exception
        },
        when (type) {
            Type.DISK -> "Disk exception:${throwable.message}"
            Type.NETWORK -> "Network exception:${throwable.message}"
            Type.OS -> "OS exception:${throwable.message}"
        }
    ) {
        enum class Type {
            DISK,
            NETWORK,
            OS
        }
    }

    data class FailedToFetch(
        val key: Any? = null,
        private val type: Type
    ) : FireFlowException(
        R.string.failed_to_fetched,
        "Failed to fetch remote config for key:$key"
    ) {
        enum class Type {
            INIT,
            STRING,
            BOOLEAN,
            DOUBLE,
            LONG
        }
    }

    object NullCurrentUserAccount : FireFlowException(
        R.string.null_current_user_account,
        "Null current user account"
    )

    object NullUserAccount : FireFlowException(
        R.string.null_user_account,
        "Null user account"
    )

    object NullUserAccountByState : FireFlowException(
        R.string.null_user_account_by_state,
        "Null user account by state"
    )

    data class TokenRefreshFailed(
        private val message: String?
    ) : FireFlowException(
        R.string.token_expired,
        "message=$message"
    )

    data class UserVisible(
        private val message: String?
    ) : FireFlowException(
        R.string.empty,
        message.orEmpty()
    )
}
