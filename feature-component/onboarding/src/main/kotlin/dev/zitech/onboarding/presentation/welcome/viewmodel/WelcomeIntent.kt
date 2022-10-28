/*
 * Copyright (C) 2022 Zitech Ltd.
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

import dev.zitech.core.common.presentation.architecture.MviIntent

internal sealed interface WelcomeIntent : MviIntent

internal object OnContinueWithOauthClick : WelcomeIntent
internal object OnContinueWithPatClick : WelcomeIntent
internal object OnDemoClick : WelcomeIntent
internal object NavigatedToOath : WelcomeIntent
internal object NavigatedToPat : WelcomeIntent
internal object NavigatedToDemo : WelcomeIntent
