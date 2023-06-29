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

import dev.zitech.fireflow.common.data.local.database.entity.UserEntity
import dev.zitech.fireflow.common.data.mapper.DomainMapper
import dev.zitech.fireflow.common.domain.model.user.User
import dev.zitech.fireflow.common.domain.model.user.UserAuthenticationType
import javax.inject.Inject

/**
 * Mapper class for converting between [UserEntity] and [User].
 */
internal class UserEntityMapper @Inject constructor() :
    DomainMapper<UserEntity, User>,
    EntityMapper<User, UserEntity> {

    /**
     * Converts a [UserEntity] to a [User] domain object.
     *
     * This method takes a [UserEntity] object from the database and converts it into the
     * corresponding [User] domain object. The conversion includes mapping the various properties of
     * the [UserEntity] to the corresponding properties of [User]. The resulting [User] represents
     * the deserialized form of the [UserEntity].
     *
     * @param input The input [UserEntity] to be converted.
     * @return The corresponding [User] domain object.
     */
    override fun toDomain(input: UserEntity): User =
        if (input.serverAddress != null) {
            User.Remote(
                authenticationType = getAuthenticationType(input),
                connectivityNotification = input.connectivityNotification,
                email = input.email,
                fireflyId = input.fireflyId,
                isCurrentUser = input.isCurrentUser,
                role = input.role,
                serverAddress = input.serverAddress,
                state = input.state,
                type = input.type,
                id = input.id!!
            )
        } else {
            User.Local(
                isCurrentUser = input.isCurrentUser,
                userName = input.email.orEmpty(),
                id = input.id!!
            )
        }

    /**
     * Converts a [User] domain object to a [UserEntity].
     *
     * This method takes a [User] domain object and converts it into a corresponding [UserEntity] object.
     * The conversion includes mapping the various properties of the [User] to the corresponding
     * properties of [UserEntity]. The resulting [UserEntity] represents the serialized form of the
     * [User] for storage in the database.
     *
     * @param input The input [User] domain object to be converted.
     * @return The corresponding [UserEntity].
     */
    override fun toEntity(input: User): UserEntity =
        when (input) {
            is User.Local -> {
                UserEntity(
                    email = input.userName,
                    id = input.id,
                    isCurrentUser = input.isCurrentUser
                )
            }
            is User.Remote -> {
                UserEntity(
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
                    connectivityNotification = input.connectivityNotification,
                    email = input.email,
                    fireflyId = input.fireflyId,
                    id = input.id,
                    isCurrentUser = input.isCurrentUser,
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
            }
        }

    /**
     * Determines the appropriate [UserAuthenticationType] based on the input [UserEntity].
     *
     * @param input The input [UserEntity] for extracting the authentication type.
     * @return The corresponding [UserAuthenticationType], or null if no valid type is found.
     */
    private fun getAuthenticationType(input: UserEntity): UserAuthenticationType? =
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
