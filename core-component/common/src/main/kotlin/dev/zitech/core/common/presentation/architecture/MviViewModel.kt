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

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * An abstract base class for implementing Model-View-Intent (MVI) architecture
 *
 * @param initialState The initial state of the screen.
 */
abstract class MviViewModel<Intent : MviIntent, State : MviState>(
    initialState: State
) : ViewModel() {

    /**
     * The mutable state of the screen.
     */
    protected val mutableState: MutableStateFlow<State> = MutableStateFlow(initialState)

    /**
     * The immutable state of the screen.
     */
    val state: StateFlow<State>
        get() = mutableState.asStateFlow()

    /**
     * Receives an intent from the View layer and updates the ViewModel's state accordingly.
     *
     * @param intent The [MviIntent] that the ViewModel should handle.
     */
    abstract fun receiveIntent(intent: Intent)

    /**
     * Updates the state of the screen using the given [stateUpdate] function.
     *
     * @param stateUpdate A function that takes the current state of the screen and returns a new
     * state based on it.
     */
    protected inline fun updateState(stateUpdate: State.() -> State) {
        mutableState.value = mutableState.value.stateUpdate()
    }
}
