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

package dev.zitech.settings.presentation.settings.viewmodel

import dev.zitech.core.common.presentation.architecture.MviIntent

internal data class AnalyticsChecked(val checked: Boolean) : SettingsIntent
internal data class CrashReporterChecked(val checked: Boolean) : SettingsIntent
internal data class LanguageSelected(val id: Int) : SettingsIntent
internal data class PerformanceChecked(val checked: Boolean) : SettingsIntent
internal data class PersonalizedAdsChecked(val checked: Boolean) : SettingsIntent
internal data class RestartApplicationClicked(val restart: () -> Unit) : SettingsIntent
internal data class ThemeSelected(val id: Int) : SettingsIntent
internal object AnalyticsErrorHandled : SettingsIntent
internal object ConfirmLogOutClicked : SettingsIntent
internal object ConfirmLogOutDismissed : SettingsIntent
internal object CrashReporterErrorHandled : SettingsIntent
internal object LanguageDismissed : SettingsIntent
internal object LanguagePreferenceClicked : SettingsIntent
internal object LogOutClicked : SettingsIntent
internal object PerformanceErrorHandled : SettingsIntent
internal object PersonalizedAdsErrorHandled : SettingsIntent
internal object ThemeDismissed : SettingsIntent
internal object ThemePreferenceClicked : SettingsIntent
internal sealed interface SettingsIntent : MviIntent
