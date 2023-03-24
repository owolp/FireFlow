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

package dev.zitech.onboarding.presentation.welcome.viewmodel

import dev.zitech.core.common.domain.model.Work
import dev.zitech.core.common.presentation.architecture.MviIntent

internal data class NavigatedToFireflyResult(val result: Work<Unit>) : WelcomeIntent
internal object BackClicked : WelcomeIntent
internal object ContinueWithOauthClicked : WelcomeIntent
internal object ContinueWithPatClicked : WelcomeIntent
internal object DemoHandled : WelcomeIntent
internal object DemoPositiveClicked : WelcomeIntent
internal object DemoWarningDismissed : WelcomeIntent
internal object FatalErrorHandled : WelcomeIntent
internal object FireflyClicked : WelcomeIntent
internal object GetStartedClicked : WelcomeIntent
internal object NonFatalErrorHandled : WelcomeIntent
internal object OAuthHandled : WelcomeIntent
internal object PatHandled : WelcomeIntent
internal object QuitAppHandled : WelcomeIntent
internal sealed interface WelcomeIntent : MviIntent
