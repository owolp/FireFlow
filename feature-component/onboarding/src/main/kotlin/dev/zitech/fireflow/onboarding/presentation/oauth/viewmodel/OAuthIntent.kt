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

package dev.zitech.fireflow.onboarding.presentation.oauth.viewmodel

import dev.zitech.fireflow.common.presentation.architecture.MviIntent
import dev.zitech.fireflow.core.result.OperationResult
import dev.zitech.fireflow.onboarding.presentation.oauth.model.OAuthAuthentication

internal data class ClientIdChanged(val clientId: String) : OAuthIntent
internal data class ClientSecretChanged(val clientSecret: String) : OAuthIntent
internal data class NavigatedToFireflyResult(val result: OperationResult<Unit>) : OAuthIntent
internal data class OauthCodeReceived(val authentication: OAuthAuthentication) : OAuthIntent
internal data class ServerAddressChanged(val serverAddress: String) : OAuthIntent
internal object AuthenticationCanceled : OAuthIntent
internal object BackClicked : OAuthIntent
internal object LoginClicked : OAuthIntent
internal object FatalErrorHandled : OAuthIntent
internal object NonFatalErrorHandled : OAuthIntent
internal object StepClosedHandled : OAuthIntent
internal object StepCompletedHandled : OAuthIntent
internal sealed interface OAuthIntent : MviIntent
