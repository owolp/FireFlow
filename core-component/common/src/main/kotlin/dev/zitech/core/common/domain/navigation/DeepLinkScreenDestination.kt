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

package dev.zitech.core.common.domain.navigation

import dev.zitech.core.common.domain.error.FireFlowError

@Deprecated("Modules")
sealed class DeepLinkScreenDestination {
    object Accounts : DeepLinkScreenDestination()
    object Current : DeepLinkScreenDestination()
    data class Error(
        val error: FireFlowError
    ) : DeepLinkScreenDestination()

    object Init : DeepLinkScreenDestination()
    object Welcome : DeepLinkScreenDestination()
}
