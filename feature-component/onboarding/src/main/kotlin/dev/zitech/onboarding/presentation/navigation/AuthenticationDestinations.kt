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

package dev.zitech.onboarding.presentation.navigation

import android.net.Uri
import dev.zitech.navigation.presentation.model.FireFlowNavigationDestination
import dev.zitech.onboarding.presentation.login.model.LoginType

private const val DESTINATION = "onboarding"

object LoginDestination : FireFlowNavigationDestination {
    const val loginType = "loginType"
    override val route: String = "login_route/{$loginType}"
    override val destination: String = DESTINATION

    fun createNavigationRoute(loginType: LoginType): String {
        val encodedLoginType = Uri.encode(loginType.value)
        return "login_route/$encodedLoginType"
    }
}

object WelcomeDestination : FireFlowNavigationDestination {
    override val route: String = "welcome_route"
    override val destination: String = DESTINATION
}
