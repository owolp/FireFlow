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

package dev.zitech.fireflow.common.data.remote.rest.response.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class UserResponse(
    @Json(name = "data")
    val data: GetUserDataResponse
)

@JsonClass(generateAdapter = true)
internal class GetUserDataResponse(
    @Json(name = "attributes")
    val attributes: GetUserDataAttributesResponse,
    @Json(name = "id")
    val id: String,
    @Json(name = "type")
    val type: String
)

@JsonClass(generateAdapter = true)
internal class GetUserDataAttributesResponse(
    @Json(name = "email")
    val email: String,
    @Json(name = "role")
    val role: String
)
