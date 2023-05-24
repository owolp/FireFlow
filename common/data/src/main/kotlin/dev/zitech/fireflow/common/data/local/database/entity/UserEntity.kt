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

package dev.zitech.fireflow.common.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user entity in the database.
 *
 * @param id The unique identifier of the user. Auto-generated if not provided.
 * @param accessToken The access token associated with the user.
 * @param clientId The client ID associated with the user.
 * @param clientSecret The client secret associated with the user.
 * @param email The email address of the user.
 * @param fireflyId The Firefly ID associated with the user.
 * @param isCurrentUser Indicates whether the user is the current active user.
 * @param oauthCode The OAuth code associated with the user.
 * @param refreshToken The refresh token associated with the user.
 * @param role The role assigned to the user.
 * @param serverAddress The server address associated with the user.
 * @param state The state value of the user.
 * @param type The type of the user.
 */
@Entity(tableName = "users")
internal data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val accessToken: String? = null,
    val clientId: String? = null,
    val clientSecret: String? = null,
    val email: String? = null,
    val fireflyId: String? = null,
    val isCurrentUser: Boolean,
    val oauthCode: String? = null,
    val refreshToken: String? = null,
    val role: String? = null,
    val serverAddress: String? = null,
    val state: String? = null,
    val type: String? = null
)
