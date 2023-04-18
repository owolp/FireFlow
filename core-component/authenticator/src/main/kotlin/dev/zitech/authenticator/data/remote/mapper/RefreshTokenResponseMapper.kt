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

package dev.zitech.authenticator.data.remote.mapper

import dev.zitech.authenticator.data.remote.model.RefreshTokenResponse
import dev.zitech.authenticator.domain.model.Token
import dev.zitech.core.common.data.mapper.DomainMapper
import javax.inject.Inject

@Deprecated("Modules")
internal class RefreshTokenResponseMapper @Inject constructor() :
    DomainMapper<RefreshTokenResponse, Token> {

    override fun toDomain(input: RefreshTokenResponse) = Token(
        accessToken = input.accessToken,
        refreshToken = input.refreshToken
    )
}
