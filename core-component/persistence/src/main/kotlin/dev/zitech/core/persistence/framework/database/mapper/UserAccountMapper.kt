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

package dev.zitech.core.persistence.framework.database.mapper

import dev.zitech.core.common.data.mapper.Mapper
import dev.zitech.core.common.domain.logger.Logger
import dev.zitech.core.persistence.domain.model.database.UserAccount
import dev.zitech.core.persistence.domain.model.database.UserAccount.Theme.DARK
import dev.zitech.core.persistence.domain.model.database.UserAccount.Theme.LIGHT
import dev.zitech.core.persistence.domain.model.database.UserAccount.Theme.SYSTEM
import dev.zitech.core.persistence.framework.database.entity.UserAccountEntity
import javax.inject.Inject

internal class UserAccountMapper @Inject constructor() : Mapper<UserAccountEntity, UserAccount> {

    companion object {
        private const val TAG = "UserAccountMapper"
    }

    override fun invoke(input: UserAccountEntity) = UserAccount(
        id = input.id ?: -1,
        isCurrentUserAccount = input.isCurrentUserAccount,
        theme = getUserTheme(input.theme)
    )

    private fun getUserTheme(theme: Long): UserAccount.Theme =
        when (theme) {
            SYSTEM.id -> SYSTEM
            DARK.id -> DARK
            LIGHT.id -> LIGHT
            else -> {
                Logger.e(TAG, "Theme $theme not supported!")
                SYSTEM
            }
        }
}
