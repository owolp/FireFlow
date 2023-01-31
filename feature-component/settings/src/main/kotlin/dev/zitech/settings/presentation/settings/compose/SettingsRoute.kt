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

package dev.zitech.settings.presentation.settings.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.core.common.domain.navigation.DeepLinkScreenDestination
import dev.zitech.core.common.domain.navigation.LogInState
import dev.zitech.ds.atoms.loading.FireFlowProgressIndicators
import dev.zitech.ds.molecules.dialog.FireFlowDialogs
import dev.zitech.ds.molecules.snackbar.BottomNotifierMessage
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.settings.presentation.settings.viewmodel.ShowError
import dev.zitech.settings.presentation.settings.viewmodel.ErrorHandled
import dev.zitech.settings.presentation.settings.viewmodel.Idle
import dev.zitech.settings.presentation.settings.viewmodel.OnAnalyticsCheckChange
import dev.zitech.settings.presentation.settings.viewmodel.OnCrashReporterCheckChange
import dev.zitech.settings.presentation.settings.viewmodel.OnLanguageDismiss
import dev.zitech.settings.presentation.settings.viewmodel.OnLanguagePreferenceClick
import dev.zitech.settings.presentation.settings.viewmodel.OnLanguageSelect
import dev.zitech.settings.presentation.settings.viewmodel.OnPerformanceCheckChange
import dev.zitech.settings.presentation.settings.viewmodel.OnPersonalizedAdsCheckChange
import dev.zitech.settings.presentation.settings.viewmodel.OnThemeDismiss
import dev.zitech.settings.presentation.settings.viewmodel.OnThemePreferenceClick
import dev.zitech.settings.presentation.settings.viewmodel.OnThemeSelect
import dev.zitech.settings.presentation.settings.viewmodel.RestartApplication
import dev.zitech.settings.presentation.settings.viewmodel.OnRestartApplication
import dev.zitech.settings.presentation.settings.viewmodel.SelectLanguage
import dev.zitech.settings.presentation.settings.viewmodel.SelectTheme
import dev.zitech.settings.presentation.settings.viewmodel.SettingsViewModel

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToAccounts: () -> Unit,
    navigateToError: () -> Unit,
    navigateToWelcome: () -> Unit,
    restartApplication: () -> Unit
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val logInState by viewModel.logInState.collectAsStateWithLifecycle()
    val snackbarState = rememberSnackbarState()

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
                snackbarState = snackbarState,
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
                }
            )
        }
        is LogInState.NotLogged -> {
            LaunchedEffect(Unit) {
                when (state.destination) {
                    DeepLinkScreenDestination.Accounts -> navigateToAccounts()
                    DeepLinkScreenDestination.Error -> navigateToError()
                    DeepLinkScreenDestination.Welcome -> navigateToWelcome()
                    DeepLinkScreenDestination.Current,
                    DeepLinkScreenDestination.Init -> {
                        // NO_OP
                    }
                }
            }
        }
    }

    when (val event = screenState.event) {
        is ShowError -> {
            snackbarState.showMessage(
                BottomNotifierMessage(
                    text = event.message,
                    state = BottomNotifierMessage.State.ERROR,
                    duration = BottomNotifierMessage.Duration.SHORT,
                    action = BottomNotifierMessage.Action(
                        label = event.action,
                        onAction = { viewModel.sendIntent(OnRestartApplication) }
                    )
                )
            )
            viewModel.sendIntent(ErrorHandled)
        }
        is SelectTheme -> {
            FireFlowDialogs.Radio(
                title = event.title,
                radioItems = event.themes,
                onItemClick = { viewModel.sendIntent(OnThemeSelect(it)) },
                onDismissRequest = { viewModel.sendIntent(OnThemeDismiss) }
            )
        }
        is SelectLanguage -> {
            FireFlowDialogs.Radio(
                title = event.title,
                radioItems = event.languages,
                onItemClick = { viewModel.sendIntent(OnLanguageSelect(it)) },
                onDismissRequest = { viewModel.sendIntent(OnLanguageDismiss) }
            )
        }
        RestartApplication -> restartApplication()
        Idle -> {
            // NO_OP
        }
    }
}
