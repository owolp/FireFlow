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

package dev.zitech.fireflow.common.presentation.validator

/**
 * Interface for a generic validator that performs validation on a given input of type [T].
 *
 * Validators implement the [invoke] operator function, which takes an input of type [T] and returns
 * a boolean indicating whether the input is valid or not.
 *
 * @param T The type of the input to be validated.
 */
interface Validator<T> {

    /**
     * Performs validation on the given input and returns a boolean indicating whether the input is valid or not.
     *
     * Implementing classes should provide custom logic to validate the input and return true if it is valid,
     * or false otherwise.
     *
     * @param input The input to be validated.
     * @return `true` if the input is valid, `false` otherwise.
     */
    operator fun invoke(input: T): Boolean
}
