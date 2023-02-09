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

package dev.zitech.core.persistence.framework.database.mapper

import dev.zitech.core.common.data.mapper.DomainMapper
import dev.zitech.core.common.data.mapper.EntityMapper
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.framework.database.entity.UserAccountEntity
import javax.inject.Inject

internal class UserAccountMapper @Inject constructor() :
    DomainMapper<UserAccountEntity, UserAccount>,
    EntityMapper<UserAccount, UserAccountEntity> {

    override fun toDomain(input: UserAccountEntity) = UserAccount(
        userId = input.id!!,
        clientId = input.clientId,
        clientSecret = input.clientSecret,
        isCurrentUserAccount = input.isCurrentUserAccount,
        oauthCode = input.oauthCode,
        serverAddress = input.serverAddress,
        state = input.state
    )

    override fun toEntity(input: UserAccount) = UserAccountEntity(
        id = input.userId,
        clientId = input.clientId,
        clientSecret = input.clientSecret,
        isCurrentUserAccount = input.isCurrentUserAccount,
        oauthCode = input.oauthCode,
        serverAddress = input.serverAddress,
        state = input.state
    )
}
