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

package dev.zitech.fireflow.common.data.source.about

import dev.zitech.fireflow.common.data.remote.rest.mapper.user.UserResponseMapper
import dev.zitech.fireflow.common.data.remote.rest.result.mapToOperationResult
import dev.zitech.fireflow.common.data.remote.rest.service.AboutService
import dev.zitech.fireflow.common.domain.model.profile.FireflyProfile
import dev.zitech.fireflow.core.result.OperationResult
import javax.inject.Inject

internal class AboutRemoteSource @Inject constructor(
    private val aboutService: AboutService,
    private val userResponseMapper: UserResponseMapper
) : AboutSource {

    override suspend fun getFireflyProfile(): OperationResult<FireflyProfile> =
        aboutService.getUser().mapToOperationResult(userResponseMapper::toDomain)
}
