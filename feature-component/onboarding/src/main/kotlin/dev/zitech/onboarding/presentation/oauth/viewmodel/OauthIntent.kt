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

package dev.zitech.onboarding.presentation.oauth.viewmodel

import dev.zitech.core.common.domain.model.DataResult
import dev.zitech.core.common.presentation.architecture.MviIntent
import dev.zitech.onboarding.presentation.oauth.model.OauthAuthentication

internal sealed interface OauthIntent : MviIntent

internal object OnLoginClick : OauthIntent
internal object OnBackClick : OauthIntent
internal data class OnServerAddressChange(val serverAddress: String) : OauthIntent
internal data class OnClientIdChange(val clientId: String) : OauthIntent
internal data class OnClientSecretChange(val clientSecret: String) : OauthIntent
internal data class OnOauthCode(val authentication: OauthAuthentication) : OauthIntent
internal data class NavigatedToFireflyResult(val dataResult: DataResult<Unit>) : OauthIntent
internal object NavigationHandled : OauthIntent
internal object ErrorHandled : OauthIntent
