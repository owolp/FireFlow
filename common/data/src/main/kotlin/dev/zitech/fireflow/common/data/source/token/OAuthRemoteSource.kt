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

import dev.zitech.fireflow.common.data.remote.rest.mapper.token.AccessTokenResponseMapper
import dev.zitech.fireflow.common.data.remote.rest.mapper.token.RefreshTokenResponseMapper
import dev.zitech.fireflow.common.data.remote.rest.result.mapToWork
import dev.zitech.fireflow.common.data.remote.rest.service.OAuthService
import dev.zitech.fireflow.common.domain.model.authentication.Token
import dev.zitech.fireflow.core.work.OperationResult
import javax.inject.Inject

internal class OAuthRemoteSource @Inject constructor(
    private val accessTokenResponseMapper: AccessTokenResponseMapper,
    private val oAuthService: OAuthService,
    private val refreshTokenResponseMapper: RefreshTokenResponseMapper
) : OAuthSource {

    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): OperationResult<Token> = oAuthService.postAccessToken(
        clientId = clientId,
        clientSecret = clientSecret,
        code = code
    ).mapToWork(accessTokenResponseMapper::toDomain)

    override suspend fun getRefreshedToken(
        clientId: String,
        clientSecret: String,
        refreshToken: String
    ): OperationResult<Token> = oAuthService.postRefreshToken(
        clientId = clientId,
        clientSecret = clientSecret,
        refreshToken = refreshToken
    ).mapToWork(refreshTokenResponseMapper::toDomain)
}
