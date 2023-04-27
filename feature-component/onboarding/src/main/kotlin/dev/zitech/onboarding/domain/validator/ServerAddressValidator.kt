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

package dev.zitech.onboarding.domain.validator

import dev.zitech.fireflow.common.presentation.validator.Validator
import java.util.regex.Pattern
import javax.inject.Inject

internal class ServerAddressValidator @Inject constructor() : Validator<String> {

    companion object {
        /*
            The URL must start with either http or https
            and then followed by :// and
            then it might contain www. and
            then followed by subdomain of length (1, 256)
            e.g. http://www.example.com, https://192.168.1.1
         */
        private const val ADDRESS_REGEX = "((http|https)://)(www.)?[a-zA-Z0-9@:%._+~#?&//=]{1,256}"
    }

    override fun invoke(input: String): Boolean =
        Pattern.compile(ADDRESS_REGEX).matcher(input).matches()
}
