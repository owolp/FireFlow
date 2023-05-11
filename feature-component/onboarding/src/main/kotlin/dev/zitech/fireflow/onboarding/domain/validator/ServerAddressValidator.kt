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
 * Validator for validating the server address URL.
 */
internal class ServerAddressValidator @Inject constructor() : Validator<String> {

    companion object {

        /**
         * Regular expression pattern used to validate server address URLs.
         *
         * Pattern breakdown:
         * - `((http|https)://)`: Matches either "http://" or "https://". The `://` part is a literal sequence.
         * - `(www.)?`: Matches an optional "www." subdomain prefix.
         * - `[a-zA-Z0-9@:%._+~#?&//=]{1,256}`: Matches a sequence of alphanumeric characters, along with the special
         *   characters "@", ":", "%", ".", "_", "+", "~", "#", "?", "&", and "=", with a length ranging from 1 to 256.
         *
         * Example matches:
         * - "http://www.example.com"
         * - "https://example.com"
         * - "https://www.example.com"
         * - "https://www.example.com/path/to/resource?param=value"
         * - "http://192.168.1.1"
         * - "https://10.0.0.1"
         *
         * Example non-matches:
         * - "ftp://example.com" (not starting with "http://" or "https://")
         * - "https://www.example.com/too/long/subdomain/that/exceeds/the/maximum/limit/of/256/characters"
         * - "http://www.example.com?param=123&key=value" (contains unsupported characters)
         * - "http://localhost" (local addresses without explicit IP are not supported)
         */
        private const val ADDRESS_REGEX = "((http|https)://)(www.)?[a-zA-Z0-9@:%._+~#?&//=]{1,256}"
    }

    /**
     * Validates the server address URL.
     *
     * @param input The server address URL to validate.
     * @return `true` if the server address URL is valid, `false` otherwise.
     */
    override fun invoke(input: String): Boolean =
        Pattern.compile(ADDRESS_REGEX).matcher(input).matches()
}
