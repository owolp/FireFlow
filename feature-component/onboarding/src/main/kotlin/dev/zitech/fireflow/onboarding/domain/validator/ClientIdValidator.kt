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

package dev.zitech.fireflow.onboarding.domain.validator

import dev.zitech.fireflow.common.presentation.validator.Validator
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Validator for validating the client ID.
 */
internal class ClientIdValidator @Inject constructor() : Validator<String> {

    companion object {
        /**
         * Regular expression pattern used to validate client IDs.
         *
         * Pattern breakdown:
         * - `^`: Matches the start of the string.
         * - `[1-9]`: Matches a single digit between 1 and 9 (excluding 0) as the first character.
         * - `\\d*`: Matches zero or more digits (0-9) following the first character.
         * - `\$`: Matches the end of the string.
         *
         * This pattern ensures that the client ID is a positive number with no leading zeros.
         *
         * Example matches:
         * - "1"
         * - "123"
         * - "987654321"
         *
         * Example non-matches:
         * - "0" (starts with 0)
         * - "0123" (starts with 0)
         * - "abc" (contains non-digit characters)
         * - "-123" (negative number)
         */
        private const val NUMBER_REGEX = "^[1-9]\\d*\$"
    }

    /**
     * Validates the client ID.
     *
     * @param input The client ID to validate.
     * @return `true` if the client ID is valid, `false` otherwise.
     */
    override fun invoke(input: String): Boolean =
        Pattern.compile(NUMBER_REGEX).matcher(input).matches()
}
