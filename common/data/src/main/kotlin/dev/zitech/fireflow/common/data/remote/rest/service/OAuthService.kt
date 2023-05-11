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

package dev.zitech.fireflow.common.data.remote.rest.service

import dev.zitech.fireflow.common.data.remote.rest.response.token.AccessTokenResponse
import dev.zitech.fireflow.common.data.remote.rest.response.token.RefreshTokenResponse
import dev.zitech.fireflow.common.data.remote.rest.result.NetworkResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Interface for accessing OAuth services provided by the API.
 */
internal interface OAuthService {

    /**
     * Requests an access token from the API using the provided authorization code.
     *
     * This method performs a POST request to the "oauth/token" endpoint with the necessary parameters
     * to exchange the authorization code for an access token.
     *
     * @param clientId The client ID associated with the OAuth client.
     * @param clientSecret The client secret associated with the OAuth client.
     * @param code The authorization code received during the OAuth authorization process.
     * @param redirectUri The redirect URI used during the OAuth authorization process.
     *                    Defaults to "fireflow://authentication" if not specified.
     * @param grantType The grant type for the token request. Defaults to "authorization_code".
     * @return A [NetworkResponse] containing the access token response.
     */
    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun postAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String = "fireflow://authentication",
        @Field("grant_type") grantType: String = "authorization_code"
    ): NetworkResponse<AccessTokenResponse>

    /**
     * Requests a refreshed access token from the API using a refresh token.
     *
     * This method performs a POST request to the "oauth/token" endpoint with the necessary parameters
     * to refresh the access token using a valid refresh token.
     *
     * @param clientId The client ID associated with the OAuth client.
     * @param clientSecret The client secret associated with the OAuth client.
     * @param grantType The grant type for the token request. Defaults to "refresh_token".
     * @param refreshToken The refresh token used to obtain a new access token.
     * @return A [NetworkResponse] containing the refreshed access token response.
     */
    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun postRefreshToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String
    ): NetworkResponse<RefreshTokenResponse>
}
