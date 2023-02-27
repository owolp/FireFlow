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
import dev.zitech.core.common.domain.code.StatusCode

sealed class FireFlowException(
    @StringRes val uiResId: Int,
    val debugMessage: String
) {
    // TODO: To be removed
    @Suppress("ForbiddenComment")
    object Legacy : FireFlowException(R.string.empty, "")

    data class DataError(
        val statusCode: StatusCode,
        val message: String?
    ) : FireFlowException(R.string.network_error, "")

    data class DataException(val throwable: Throwable) : FireFlowException(
        R.string.network_exception,
        ""
    )

    object NullCurrentUserAccount : FireFlowException(R.string.null_current_user_account, "")
    object NullUserAccountByState : FireFlowException(R.string.null_user_account_by_state, "")
    object TokenExpired : FireFlowException(R.string.token_expired, "")
}
