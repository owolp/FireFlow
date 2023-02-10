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

package dev.zitech.onboarding.data.remote.mapper

import dev.zitech.core.common.data.mapper.DomainMapper
import dev.zitech.core.network.data.model.PostTokenResponse
import dev.zitech.onboarding.domain.model.Token
import javax.inject.Inject

internal class PostTokenResponseMapper @Inject constructor() :
    DomainMapper<PostTokenResponse, Token> {

    override fun toDomain(input: PostTokenResponse) = Token(
        accessToken = input.refreshToken,
        refreshToken = input.refreshToken
    )
}
