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

package dev.zitech.fireflow.common.data.repository.token

import dev.zitech.fireflow.common.data.source.token.OAuthSource
import dev.zitech.fireflow.common.domain.model.authentication.Token
import dev.zitech.fireflow.common.domain.repository.authentication.TokenRepository
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

internal class TokenRepositoryImpl @Inject constructor(
    private val oAuthRemoteSource: OAuthSource
) : TokenRepository {

    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): OperationResult<Token> =
        oAuthRemoteSource.getAccessToken(clientId, clientSecret, code)

    override suspend fun getRefreshedToken(
        clientId: String,
        clientSecret: String,
        refreshToken: String
    ): OperationResult<Token> =
        oAuthRemoteSource.getRefreshedToken(clientId, clientSecret, refreshToken)
}
