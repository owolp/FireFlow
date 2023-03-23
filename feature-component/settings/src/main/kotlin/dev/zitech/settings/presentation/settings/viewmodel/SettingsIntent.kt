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

internal sealed interface SettingsIntent : MviIntent

internal data class OnAnalyticsCheckChange(val checked: Boolean) : SettingsIntent
internal data class OnPersonalizedAdsCheckChange(val checked: Boolean) : SettingsIntent
internal data class OnPerformanceCheckChange(val checked: Boolean) : SettingsIntent
internal data class OnCrashReporterCheckChange(val checked: Boolean) : SettingsIntent
internal object OnThemePreferenceClick : SettingsIntent
internal data class OnThemeSelect(val id: Int) : SettingsIntent
internal object OnThemeDismiss : SettingsIntent
internal object OnLanguagePreferenceClick : SettingsIntent
internal data class OnLanguageSelect(val id: Int) : SettingsIntent
internal object OnLanguageDismiss : SettingsIntent
internal data class OnRestartApplication(val restart: () -> Unit) : SettingsIntent
internal object OnLogOutClick : SettingsIntent
internal object OnConfirmLogOutClick : SettingsIntent
internal object OnConfirmLogOutDismiss : SettingsIntent
internal object AnalyticsErrorHandled : SettingsIntent
internal object CrashReporterErrorHandled : SettingsIntent
internal object PersonalizedAdsErrorHandled : SettingsIntent
internal object PerformanceErrorHandled : SettingsIntent
