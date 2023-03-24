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

import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * The `MviState` interface represents the state of the screen in the Model-View-Intent (MVI) architecture.
 *
 * All states in the MVI architecture should be immutable, and changes to the state should be done by creating
 * a new instance of the state with the updated values.
 *
 * Implementations of this interface should be data classes to ensure immutability and provide convenient
 * copying and updating of the state.
 */
interface MviState {
    @Deprecated("Do not use")
    interface Event {
        val uniqueId: String
            get() = UUID.randomUUID().toString()
    }
}

/**
 * Updates the state of this [MutableStateFlow] by applying the given [transform] function to the current state.
 * The [transform] function should return a modified copy of the current state with the desired changes.
 *
 * @param transform The function that takes the current state of type [T] and returns a modified copy of type [T].
 * @throws TypeCastException if the actual type of [T] does not match the reified type parameter.
 */
inline fun <reified T : MviState> MutableStateFlow<T>.updateState(transform: T.() -> T) {
    this.update { currentState -> transform(currentState) }
}
