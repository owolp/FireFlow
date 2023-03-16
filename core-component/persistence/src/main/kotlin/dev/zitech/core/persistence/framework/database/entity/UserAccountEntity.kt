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

package dev.zitech.core.persistence.framework.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_accounts"
)
internal data class UserAccountEntity(
    val accessToken: String? = null,
    val clientId: String? = null,
    val clientSecret: String? = null,
    val email: String? = null,
    val fireflyId: String? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val isCurrentUserAccount: Boolean,
    val oauthCode: String? = null,
    val refreshToken: String? = null,
    val role: String? = null,
    val serverAddress: String,
    val state: String? = null,
    val type: String? = null
)
