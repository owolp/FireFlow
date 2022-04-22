/*
 * Copyright (C) 2022 Zitech Ltd.
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

package dev.zitech.core.persistence.domain.usecase

import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import javax.inject.Inject

class SaveUserAccountUseCase @Inject constructor(
    private val userAccountRepository: UserAccountRepository
) {

    suspend operator fun invoke(isCurrentUserAccount: Boolean): DataResult<Long> =
        userAccountRepository.saveUserAccount(
            isCurrentUserAccount = isCurrentUserAccount
        )
}
