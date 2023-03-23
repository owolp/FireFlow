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

package dev.zitech.settings.presentation.settings.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.domain.navigation.DeepLinkScreenDestination
import dev.zitech.core.common.domain.navigation.LogInState
import dev.zitech.ds.atoms.loading.FireFlowProgressIndicators
import dev.zitech.settings.presentation.settings.viewmodel.AnalyticsChecked
import dev.zitech.settings.presentation.settings.viewmodel.AnalyticsErrorHandled
import dev.zitech.settings.presentation.settings.viewmodel.ConfirmLogOutClicked
import dev.zitech.settings.presentation.settings.viewmodel.ConfirmLogOutDismissed
import dev.zitech.settings.presentation.settings.viewmodel.CrashReporterChecked
import dev.zitech.settings.presentation.settings.viewmodel.CrashReporterErrorHandled
import dev.zitech.settings.presentation.settings.viewmodel.LanguageDismissed
import dev.zitech.settings.presentation.settings.viewmodel.LanguagePreferenceClicked
import dev.zitech.settings.presentation.settings.viewmodel.LanguageSelected
import dev.zitech.settings.presentation.settings.viewmodel.LogOutClicked
import dev.zitech.settings.presentation.settings.viewmodel.OnRestartApplicationClicked
import dev.zitech.settings.presentation.settings.viewmodel.OnThemeSelected
import dev.zitech.settings.presentation.settings.viewmodel.PerformanceChecked
import dev.zitech.settings.presentation.settings.viewmodel.PerformanceErrorHandled
import dev.zitech.settings.presentation.settings.viewmodel.PersonalizedAdsChecked
import dev.zitech.settings.presentation.settings.viewmodel.PersonalizedAdsErrorHandled
import dev.zitech.settings.presentation.settings.viewmodel.SettingsViewModel
import dev.zitech.settings.presentation.settings.viewmodel.ThemeDismissed
import dev.zitech.settings.presentation.settings.viewmodel.ThemePreferenceClicked

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToAccounts: () -> Unit,
    navigateToError: (error: Error) -> Unit,
    navigateToWelcome: () -> Unit,
    restartApplication: () -> Unit
) {
    val screenState by viewModel.state.collectAsStateWithLifecycle()
    val logInState by viewModel.logInState.collectAsStateWithLifecycle()

    when (val state = logInState) {
        LogInState.InitScreen -> {
            FireFlowProgressIndicators.Magnifier(
                modifier = Modifier.fillMaxSize()
            )
        }
        LogInState.Logged -> {
            SettingsScreen(
                modifier = modifier,
                state = screenState,
                onAnalyticsCheckChange = { checked ->
                    viewModel.receiveIntent(AnalyticsChecked(checked))
                },
                onPersonalizedAdsCheckChange = { checked ->
                    viewModel.receiveIntent(PersonalizedAdsChecked(checked))
                },
                onPerformanceCheckChange = { checked ->
                    viewModel.receiveIntent(PerformanceChecked(checked))
                },
                onCrashReporterCheckChange = { checked ->
                    viewModel.receiveIntent(CrashReporterChecked(checked))
                },
                onThemeClick = {
                    viewModel.receiveIntent(ThemePreferenceClicked)
                },
                onLanguageClick = {
                    viewModel.receiveIntent(LanguagePreferenceClicked)
                },
                onLogOutClick = {
                    viewModel.receiveIntent(LogOutClicked)
                },
                onRestartApplication = {
                    viewModel.receiveIntent(OnRestartApplicationClicked(restartApplication))
                },
                analyticsErrorHandled = {
                    viewModel.receiveIntent(AnalyticsErrorHandled)
                },
                crashReporterErrorHandled = {
                    viewModel.receiveIntent(CrashReporterErrorHandled)
                },
                personalizedAdsErrorHandled = {
                    viewModel.receiveIntent(PersonalizedAdsErrorHandled)
                },
                performanceErrorHandled = {
                    viewModel.receiveIntent(PerformanceErrorHandled)
                },
                onThemeSelect = { itemSelected ->
                    viewModel.receiveIntent(OnThemeSelected(itemSelected))
                },
                onThemeDismiss = {
                    viewModel.receiveIntent(ThemeDismissed)
                },
                onLanguageSelect = { itemSelected ->
                    viewModel.receiveIntent(LanguageSelected(itemSelected))
                },
                onLanguageDismiss = {
                    viewModel.receiveIntent(LanguageDismissed)
                },
                onConfirmLogOutDismiss = {
                    viewModel.receiveIntent(ConfirmLogOutDismissed)
                },
                onConfirmLogOutClick = {
                    viewModel.receiveIntent(ConfirmLogOutClicked)
                }
            )
        }
        is LogInState.NotLogged -> {
            LaunchedEffect(Unit) {
                when (val destination = state.destination) {
                    DeepLinkScreenDestination.Accounts -> navigateToAccounts()
                    is DeepLinkScreenDestination.Error ->
                        navigateToError(destination.error)
                    DeepLinkScreenDestination.Welcome -> navigateToWelcome()
                    DeepLinkScreenDestination.Current,
                    DeepLinkScreenDestination.Init -> {
                        // NO_OP
                    }
                }
            }
        }
    }
}
