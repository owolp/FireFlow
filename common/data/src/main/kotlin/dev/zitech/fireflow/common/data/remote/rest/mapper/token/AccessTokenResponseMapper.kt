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

package dev.zitech.fireflow.common.data.remote.rest.mapper.token

import dev.zitech.fireflow.common.data.mapper.DomainMapper
import dev.zitech.fireflow.common.data.remote.rest.response.token.AccessTokenResponse
import dev.zitech.fireflow.common.domain.model.authentication.Token
import javax.inject.Inject

internal class AccessTokenResponseMapper @Inject constructor() :
    DomainMapper<AccessTokenResponse, Token> {

    override fun toDomain(input: AccessTokenResponse) = Token(
        accessToken = input.accessToken,
        refreshToken = input.refreshToken
    )
}
