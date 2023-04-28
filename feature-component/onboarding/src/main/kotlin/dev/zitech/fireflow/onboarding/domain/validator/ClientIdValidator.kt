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

internal class ClientIdValidator @Inject constructor() : Validator<String> {

    companion object {
        /*
            The number must start with a digit between 1 and 9 and
            then it might be followed by digit(s)
         */
        private const val NUMBER_REGEX = "^[1-9]\\d*\$"
    }

    override fun invoke(input: String): Boolean =
        Pattern.compile(NUMBER_REGEX).matcher(input).matches()
}
