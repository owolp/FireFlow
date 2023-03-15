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

package dev.zitech.core.persistence.domain.model.database

data class UserAccount(
    val authenticationType: AuthenticationType? = null,
    val isCurrentUserAccount: Boolean,
    val serverAddress: String,
    val state: String? = null,
    val userId: Long
) {

    sealed class AuthenticationType(
        open val accessToken: String?
    ) {
        data class OAuth(
            override val accessToken: String? = null,
            val clientId: String,
            val clientSecret: String,
            val oauthCode: String? = null,
            val refreshToken: String? = null
        ) : AuthenticationType(accessToken)

        data class Pat(
            override val accessToken: String
        ) : AuthenticationType(accessToken)
    }

    companion object {
        const val STATE_LENGTH = 10
    }
}
