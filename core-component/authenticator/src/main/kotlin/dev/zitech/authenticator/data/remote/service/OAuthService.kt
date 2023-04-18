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

package dev.zitech.authenticator.data.remote.service

import dev.zitech.authenticator.data.remote.model.AccessTokenResponse
import dev.zitech.authenticator.data.remote.model.RefreshTokenResponse
import dev.zitech.core.common.domain.model.NetworkResult
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

@Deprecated("Modules")
interface OAuthService {

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun postAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String = "fireflow://authentication",
        @Field("grant_type") grantType: String = "authorization_code"
    ): NetworkResult<AccessTokenResponse>

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun postRefreshToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String
    ): NetworkResult<RefreshTokenResponse>
}
