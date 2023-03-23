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
import dev.zitech.settings.presentation.settings.viewmodel.AnalyticsErrorHandled
import dev.zitech.settings.presentation.settings.viewmodel.CrashReporterErrorHandled
import dev.zitech.settings.presentation.settings.viewmodel.OnAnalyticsCheckChange
import dev.zitech.settings.presentation.settings.viewmodel.OnConfirmLogOutClick
import dev.zitech.settings.presentation.settings.viewmodel.OnConfirmLogOutDismiss
import dev.zitech.settings.presentation.settings.viewmodel.OnCrashReporterCheckChange
import dev.zitech.settings.presentation.settings.viewmodel.OnLanguageDismiss
import dev.zitech.settings.presentation.settings.viewmodel.OnLanguagePreferenceClick
import dev.zitech.settings.presentation.settings.viewmodel.OnLanguageSelect
import dev.zitech.settings.presentation.settings.viewmodel.OnLogOutClick
import dev.zitech.settings.presentation.settings.viewmodel.OnPerformanceCheckChange
import dev.zitech.settings.presentation.settings.viewmodel.OnPersonalizedAdsCheckChange
import dev.zitech.settings.presentation.settings.viewmodel.OnRestartApplication
import dev.zitech.settings.presentation.settings.viewmodel.OnThemeDismiss
import dev.zitech.settings.presentation.settings.viewmodel.OnThemePreferenceClick
import dev.zitech.settings.presentation.settings.viewmodel.OnThemeSelect
import dev.zitech.settings.presentation.settings.viewmodel.PerformanceErrorHandled
import dev.zitech.settings.presentation.settings.viewmodel.PersonalizedAdsErrorHandled
import dev.zitech.settings.presentation.settings.viewmodel.SettingsViewModel

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToAccounts: () -> Unit,
    navigateToError: (error: Error) -> Unit,
    navigateToWelcome: () -> Unit,
    restartApplication: () -> Unit
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
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
                    viewModel.sendIntent(OnAnalyticsCheckChange(checked))
                },
                onPersonalizedAdsCheckChange = { checked ->
                    viewModel.sendIntent(OnPersonalizedAdsCheckChange(checked))
                },
                onPerformanceCheckChange = { checked ->
                    viewModel.sendIntent(OnPerformanceCheckChange(checked))
                },
                onCrashReporterCheckChange = { checked ->
                    viewModel.sendIntent(OnCrashReporterCheckChange(checked))
                },
                onThemeClick = {
                    viewModel.sendIntent(OnThemePreferenceClick)
                },
                onLanguageClick = {
                    viewModel.sendIntent(OnLanguagePreferenceClick)
                },
                onLogOutClick = {
                    viewModel.sendIntent(OnLogOutClick)
                },
                onRestartApplication = {
                    viewModel.sendIntent(OnRestartApplication(restartApplication))
                },
                analyticsErrorHandled = {
                    viewModel.sendIntent(AnalyticsErrorHandled)
                },
                crashReporterErrorHandled = {
                    viewModel.sendIntent(CrashReporterErrorHandled)
                },
                personalizedAdsErrorHandled = {
                    viewModel.sendIntent(PersonalizedAdsErrorHandled)
                },
                performanceErrorHandled = {
                    viewModel.sendIntent(PerformanceErrorHandled)
                },
                onThemeSelect = { itemSelected ->
                    viewModel.sendIntent(OnThemeSelect(itemSelected))
                },
                onThemeDismiss = {
                    viewModel.sendIntent(OnThemeDismiss)
                },
                onLanguageSelect = { itemSelected ->
                    viewModel.sendIntent(OnLanguageSelect(itemSelected))
                },
                onLanguageDismiss = {
                    viewModel.sendIntent(OnLanguageDismiss)
                },
                onConfirmLogOutDismiss = {
                    viewModel.sendIntent(OnConfirmLogOutDismiss)
                },
                onConfirmLogOutClick = {
                    viewModel.sendIntent(OnConfirmLogOutClick)
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
