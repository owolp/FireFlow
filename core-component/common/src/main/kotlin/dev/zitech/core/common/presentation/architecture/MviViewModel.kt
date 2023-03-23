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

package dev.zitech.core.common.presentation.architecture

import kotlinx.coroutines.flow.StateFlow

/**
 * The `MviViewModel` interface defines the contract for a ViewModel in the Model-View-Intent (MVI) architecture.
 *
 * @param Intent The type of intent this ViewModel can receive.
 * @param State The type of state this ViewModel can emit.
 */
interface MviViewModel<Intent : MviIntent, State : MviState> {

    /**
     * The `state` property is a [StateFlow] that emits the current state of the screen.
     */
    val state: StateFlow<State>

    /**
     * The `receiveIntent` method is called by the View layer to signal an intent to the ViewModel.
     *
     * @param intent The [MviIntent] that the ViewModel should handle.
     */
    fun receiveIntent(intent: Intent)
}
