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

package dev.zitech.onboarding.presentation.oauth.viewmodel.resource

import dev.zitech.core.common.domain.strings.StringsProvider
import dev.zitech.onboarding.R
import javax.inject.Inject

internal class OAuthStringsProvider @Inject constructor(
    private val stringsProvider: StringsProvider
) {

    fun getNewAccessTokenUrl(serverAddress: String, clientId: String, state: String): String =
        stringsProvider(R.string.firefly_iii_new_access_token_url, serverAddress, clientId, state)
}
