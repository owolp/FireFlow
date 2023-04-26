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

package dev.zitech.fireflow.common.data.remote.rest.service

import dev.zitech.fireflow.common.data.remote.rest.response.user.UserResponse
import dev.zitech.fireflow.common.data.remote.rest.result.NetworkResult
import retrofit2.http.GET

internal interface AboutService {

    @GET("api/v1/about/user")
    suspend fun getUser(): NetworkResult<UserResponse>
}