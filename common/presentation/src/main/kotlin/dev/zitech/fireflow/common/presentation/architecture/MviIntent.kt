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

/**
 * The `MviIntent` interface represents an intention to perform an action in the Model-View-Intent (MVI) architecture.
 *
 * Intents can be thought of as user actions that trigger state changes in the ViewModel. Examples of intents might
 * include a button click, a change in the device's configuration, or other user interaction.
 *
 * Implementations of this interface should be data classes or objects to provide a convenient and consistent way of
 * passing data along with the intent.
 */
interface MviIntent
