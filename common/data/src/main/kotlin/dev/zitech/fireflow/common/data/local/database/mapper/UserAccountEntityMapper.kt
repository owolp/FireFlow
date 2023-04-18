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

package dev.zitech.fireflow.common.data.local.database.mapper

import dev.zitech.fireflow.common.data.local.database.entity.UserAccountEntity
import dev.zitech.fireflow.common.data.mapper.DomainMapper
import dev.zitech.fireflow.common.domain.model.user.UserAccount
import dev.zitech.fireflow.common.domain.model.user.UserAuthenticationType
import javax.inject.Inject

internal class UserAccountEntityMapper @Inject constructor() :
    DomainMapper<UserAccountEntity, UserAccount>,
    EntityMapper<UserAccount, UserAccountEntity> {

    override fun toDomain(input: UserAccountEntity) = UserAccount(
        authenticationType = getAuthenticationType(input),
        email = input.email,
        fireflyId = input.fireflyId,
        isCurrentUserAccount = input.isCurrentUserAccount,
        role = input.role,
        serverAddress = input.serverAddress,
        state = input.state,
        type = input.type,
        userId = input.id!!
    )

    override fun toEntity(input: UserAccount) = UserAccountEntity(
        accessToken = when (val type = input.authenticationType) {
            is UserAuthenticationType.OAuth -> type.accessToken
            is UserAuthenticationType.Pat -> type.accessToken
            null -> null
        },
        clientId = when (val type = input.authenticationType) {
            is UserAuthenticationType.OAuth -> type.clientId
            is UserAuthenticationType.Pat,
            null -> null
        },
        clientSecret = when (val type = input.authenticationType) {
            is UserAuthenticationType.OAuth -> type.clientSecret
            is UserAuthenticationType.Pat,
            null -> null
        },
        email = input.email,
        fireflyId = input.fireflyId,
        id = input.userId,
        isCurrentUserAccount = input.isCurrentUserAccount,
        oauthCode = when (val type = input.authenticationType) {
            is UserAuthenticationType.OAuth -> type.oauthCode
            is UserAuthenticationType.Pat,
            null -> null
        },
        refreshToken = when (val type = input.authenticationType) {
            is UserAuthenticationType.OAuth -> type.refreshToken
            is UserAuthenticationType.Pat,
            null -> null
        },
        role = input.role,
        serverAddress = input.serverAddress,
        state = input.state,
        type = input.type
    )

    private fun getAuthenticationType(input: UserAccountEntity): UserAuthenticationType? =
        when {
            input.clientId != null && input.clientSecret != null -> {
                UserAuthenticationType.OAuth(
                    accessToken = input.accessToken,
                    clientId = input.clientId,
                    clientSecret = input.clientSecret,
                    oauthCode = input.oauthCode
                )
            }

            input.accessToken != null -> {
                UserAuthenticationType.Pat(
                    accessToken = input.accessToken
                )
            }

            else -> null
        }
}
