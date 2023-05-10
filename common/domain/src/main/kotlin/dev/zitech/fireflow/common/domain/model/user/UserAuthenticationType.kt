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

/**
 * Sealed class representing the type of user authentication.
 *
 * @param accessToken The access token associated with the user authentication.
 */
sealed class UserAuthenticationType(open val accessToken: String?) {
    /**
     * OAuth authentication type.
     *
     * @param accessToken The access token associated with the OAuth authentication.
     * @param clientId The client ID used for authentication.
     * @param clientSecret The client secret used for authentication.
     * @param oauthCode The OAuth code for authentication.
     * @param refreshToken The refresh token for OAuth authentication.
     */
    data class OAuth(
        override val accessToken: String? = null,
        val clientId: String,
        val clientSecret: String,
        val oauthCode: String? = null,
        val refreshToken: String? = null
    ) : UserAuthenticationType(accessToken)

    /**
     * Personal Access Token (PAT) authentication type.
     *
     * @param accessToken The access token associated with the PAT authentication.
     */
    data class Pat(
        override val accessToken: String
    ) : UserAuthenticationType(accessToken)
}
