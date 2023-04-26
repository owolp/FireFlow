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

package dev.zitech.onboarding.presentation.navigation

import dev.zitech.fireflow.common.presentation.navigation.destination.FireFlowNavigationDestination

private const val DESTINATION = "onboarding"

object OAuthDestination : FireFlowNavigationDestination {
    override val route: String = "oauth_route"
    override val destination: String = DESTINATION
    const val code = "code"
    const val state = "state"
}

object PatDestination : FireFlowNavigationDestination {
    override val route: String = "pat_route"
    override val destination: String = DESTINATION
}

object WelcomeDestination : FireFlowNavigationDestination {
    override val route: String = "welcome_route"
    override val destination: String = DESTINATION
}
