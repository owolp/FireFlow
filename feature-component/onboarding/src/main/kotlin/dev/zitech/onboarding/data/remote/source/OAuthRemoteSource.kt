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

package dev.zitech.onboarding.data.remote.source

import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.network.data.service.OAuthService
import dev.zitech.onboarding.data.remote.mapper.PostTokenResponseMapper
import dev.zitech.onboarding.domain.model.Token
import javax.inject.Inject

internal class OAuthRemoteSource @Inject constructor(
    private val oAuthService: OAuthService,
    private val postTokenResponseMapper: PostTokenResponseMapper
) {

    suspend fun getToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): DataResult<Token> = try {
        DataResult.Success(
            postTokenResponseMapper.toDomain(
                oAuthService.postToken(clientId, clientSecret, code)
            )
        )
    } catch (exception: Exception) {
        DataResult.Error(cause = exception)
    }
}
