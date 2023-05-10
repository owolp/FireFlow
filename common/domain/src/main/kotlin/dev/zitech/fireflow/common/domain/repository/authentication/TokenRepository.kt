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

package dev.zitech.fireflow.common.domain.repository.authentication

import dev.zitech.fireflow.common.domain.model.authentication.Token
import dev.zitech.fireflow.core.result.OperationResult

/**
 * Repository interface for accessing and managing access tokens.
 */
interface TokenRepository {

    /**
     * Retrieves an access token using the provided parameters.
     *
     * @param clientId The client ID.
     * @param clientSecret The client secret.
     * @param code The authorization code.
     * @return An [OperationResult] that encapsulates the retrieved access token or an error.
     */
    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): OperationResult<Token>

    /**
     * Retrieves a refreshed access token using the provided parameters.
     *
     * @param clientId The client ID.
     * @param clientSecret The client secret.
     * @param refreshToken The refresh token.
     * @return An [OperationResult] that encapsulates the refreshed access token or an error.
     */
    suspend fun getRefreshedToken(
        clientId: String,
        clientSecret: String,
        refreshToken: String
    ): OperationResult<Token>
}
