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

import dev.zitech.core.common.domain.model.LegacyDataResult
import dev.zitech.core.common.presentation.architecture.MviIntent
import dev.zitech.onboarding.presentation.oauth.model.OAuthAuthentication

internal sealed interface OAuthIntent : MviIntent

internal object OnLoginClick : OAuthIntent
internal object OnBackClick : OAuthIntent
internal data class OnServerAddressChange(val serverAddress: String) : OAuthIntent
internal data class OnClientIdChange(val clientId: String) : OAuthIntent
internal data class OnClientSecretChange(val clientSecret: String) : OAuthIntent
internal data class OnOauthCode(val authentication: OAuthAuthentication) : OAuthIntent
internal object OnAuthenticationCanceled : OAuthIntent
internal data class NavigatedToFireflyResult(val dataResult: LegacyDataResult<Unit>) : OAuthIntent
internal object NavigationHandled : OAuthIntent
internal object ErrorHandled : OAuthIntent
