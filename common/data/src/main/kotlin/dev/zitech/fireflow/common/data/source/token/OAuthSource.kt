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

package dev.zitech.fireflow.common.data.source.token

import dev.zitech.fireflow.common.domain.model.authentication.Token
import dev.zitech.fireflow.core.result.OperationResult

/**
 * Interface for retrieving OAuth tokens.
 */
internal interface OAuthSource {

    /**
     * Retrieves an access token using the provided OAuth credentials and authorization code.
     *
     * @param clientId The OAuth client ID.
     * @param clientSecret The OAuth client secret.
     * @param code The authorization code.
     * @return An [OperationResult] that represents the result of the operation,
     *         containing the retrieved access token if successful, or an error if unsuccessful.
     */
    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): OperationResult<Token>

    /**
     * Retrieves a refreshed access token using the provided OAuth credentials and refresh token.
     *
     * @param clientId The OAuth client ID.
     * @param clientSecret The OAuth client secret.
     * @param refreshToken The refresh token.
     * @return An [OperationResult] that represents the result of the operation,
     *         containing the refreshed access token if successful, or an error if unsuccessful.
     */
    suspend fun getRefreshedToken(
        clientId: String,
        clientSecret: String,
        refreshToken: String
    ): OperationResult<Token>
}
