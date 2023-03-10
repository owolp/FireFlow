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

package dev.zitech.core.persistence.domain.usecase.database

import dev.zitech.core.common.domain.model.Work
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.repository.database.UserAccountRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetUserAccountsUseCase @Inject constructor(
    private val userAccountRepository: UserAccountRepository
) {

    operator fun invoke(): Flow<Work<List<UserAccount>>> =
        userAccountRepository.getUserAccounts()
}
