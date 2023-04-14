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

data class UserAccount(
    val authenticationType: UserAuthenticationType? = null,
    val email: String? = null,
    val fireflyId: String? = null,
    val isCurrentUserAccount: Boolean,
    val role: String? = null,
    val serverAddress: String,
    val state: String? = null,
    val type: String? = null,
    val userId: Long
) {

    companion object {
        private const val STATE_LENGTH = 10

        fun getRandomState(): String =
            DataFactory.createRandomString(STATE_LENGTH)
    }
}
