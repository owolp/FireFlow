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

package dev.zitech.core.network.data.mapper

import dev.zitech.core.common.data.mapper.DomainMapper
import dev.zitech.core.network.data.model.UserResponse
import dev.zitech.core.network.domain.model.FireflyProfile
import javax.inject.Inject

@Deprecated("Modules")
internal class UserResponseMapper @Inject constructor() :
    DomainMapper<UserResponse, FireflyProfile> {

    override fun toDomain(input: UserResponse) = FireflyProfile(
        email = input.data.attributes.email,
        id = input.data.id,
        role = input.data.attributes.role,
        type = input.data.type
    )
}
