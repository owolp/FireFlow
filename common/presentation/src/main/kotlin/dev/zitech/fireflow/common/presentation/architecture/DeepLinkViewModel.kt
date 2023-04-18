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

package dev.zitech.fireflow.common.presentation.architecture

import dev.zitech.fireflow.common.presentation.navigation.state.LogInState
import kotlinx.coroutines.flow.StateFlow

/**
 * The `DeepLinkViewModel` interface represents the ViewModel responsible for handling deep links in the app.
 *
 * A deep link is a hyperlink that takes the user directly to a specific screen within an app, bypassing the
 * home screen.
 *
 * The `logInState` property represents the state related to logging in through a deep link. It is implemented as
 * a `StateFlow` that emits new states whenever the state changes. This property should be observed by the View
 * to update the UI as necessary.
 */
interface DeepLinkViewModel {
    val logInState: StateFlow<LogInState>
}
