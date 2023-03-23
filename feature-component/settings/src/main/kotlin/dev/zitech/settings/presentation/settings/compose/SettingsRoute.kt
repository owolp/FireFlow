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
import dev.zitech.settings.presentation.settings.viewmodel.PerformanceChecked
import dev.zitech.settings.presentation.settings.viewmodel.PerformanceErrorHandled
import dev.zitech.settings.presentation.settings.viewmodel.PersonalizedAdsChecked
import dev.zitech.settings.presentation.settings.viewmodel.PersonalizedAdsErrorHandled
import dev.zitech.settings.presentation.settings.viewmodel.RestartApplicationClicked
import dev.zitech.settings.presentation.settings.viewmodel.SettingsViewModel
import dev.zitech.settings.presentation.settings.viewmodel.ThemeDismissed
import dev.zitech.settings.presentation.settings.viewmodel.ThemePreferenceClicked
import dev.zitech.settings.presentation.settings.viewmodel.ThemeSelected

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    navigateToAccounts: () -> Unit,
    navigateToError: (error: Error) -> Unit,
    navigateToWelcome: () -> Unit,
    restartApplication: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
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
                analyticsChecked = { checked ->
                    viewModel.receiveIntent(AnalyticsChecked(checked))
                },
                personalizedAdsChecked = { checked ->
                    viewModel.receiveIntent(PersonalizedAdsChecked(checked))
                },
                performanceChecked = { checked ->
                    viewModel.receiveIntent(PerformanceChecked(checked))
                },
                crashReporterChecked = { checked ->
                    viewModel.receiveIntent(CrashReporterChecked(checked))
                },
                themePreferenceClicked = {
                    viewModel.receiveIntent(ThemePreferenceClicked)
                },
                languagePreferenceClicked = {
                    viewModel.receiveIntent(LanguagePreferenceClicked)
                },
                logOutClicked = {
                    viewModel.receiveIntent(LogOutClicked)
                },
                restartApplicationClicked = {
                    viewModel.receiveIntent(RestartApplicationClicked(restartApplication))
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
                themeSelected = { itemSelected ->
                    viewModel.receiveIntent(ThemeSelected(itemSelected))
                },
                themeDismissed = {
                    viewModel.receiveIntent(ThemeDismissed)
                },
                languageSelected = { itemSelected ->
                    viewModel.receiveIntent(LanguageSelected(itemSelected))
                },
                languageDismissed = {
                    viewModel.receiveIntent(LanguageDismissed)
                },
                confirmLogOutDismissed = {
                    viewModel.receiveIntent(ConfirmLogOutDismissed)
                },
                confirmLogOutClicked = {
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
