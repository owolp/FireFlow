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

package dev.zitech.settings.domain.usecase

import dev.zitech.fireflow.common.domain.repository.cache.CacheRepository
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserAccountUseCase
import dev.zitech.fireflow.common.domain.usecase.user.UpdateCurrentUserAccountUseCase.IsCurrentUserAccount
import dev.zitech.fireflow.core.work.Work
import dev.zitech.fireflow.core.work.WorkError
import dev.zitech.fireflow.core.work.WorkSuccess
import javax.inject.Inject

internal class LogOutCurrentUserUseCase @Inject constructor(
    private val updateCurrentUserAccountUseCase: UpdateCurrentUserAccountUseCase,
    private val cacheRepository: CacheRepository
) {
    suspend operator fun invoke(): Work<Unit> =
        when (
            val result = updateCurrentUserAccountUseCase(
                IsCurrentUserAccount(false)
            )
        ) {
            is WorkError -> WorkError(result.error)
            is WorkSuccess -> {
                cacheRepository.invalidateCaches()
                WorkSuccess(Unit)
            }
        }
}
