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

package dev.zitech.authenticator.data.remote.source

import dev.zitech.authenticator.data.remote.mapper.AccessTokenResponseMapper
import dev.zitech.authenticator.data.remote.mapper.RefreshTokenResponseMapper
import dev.zitech.authenticator.data.remote.service.OAuthService
import dev.zitech.authenticator.domain.model.Token
import dev.zitech.core.common.domain.model.LegacyDataResult
import javax.inject.Inject

internal class OAuthRemoteSource @Inject constructor(
    private val oAuthService: OAuthService,
    private val accessTokenResponseMapper: AccessTokenResponseMapper,
    private val refreshTokenResponseMapper: RefreshTokenResponseMapper
) {

    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): LegacyDataResult<Token> = try {
        LegacyDataResult.Success(
            accessTokenResponseMapper.toDomain(
                oAuthService.postAccessToken(
                    clientId = clientId,
                    clientSecret = clientSecret,
                    code = code
                )
            )
        )
    } catch (exception: Exception) {
        LegacyDataResult.Error(cause = exception)
    }

    suspend fun getRefreshedToken(
        clientId: String,
        clientSecret: String,
        refreshToken: String
    ): LegacyDataResult<Token> = try {
        LegacyDataResult.Success(
            refreshTokenResponseMapper.toDomain(
                oAuthService.postRefreshToken(
                    clientId = clientId,
                    clientSecret = clientSecret,
                    refreshToken = refreshToken
                )
            )
        )
    } catch (exception: Exception) {
        LegacyDataResult.Error(cause = exception)
    }
}
