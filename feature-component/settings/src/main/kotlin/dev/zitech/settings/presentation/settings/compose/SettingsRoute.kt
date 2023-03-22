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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.core.common.domain.error.Error
import dev.zitech.core.common.domain.navigation.DeepLinkScreenDestination
import dev.zitech.core.common.domain.navigation.LogInState
import dev.zitech.ds.atoms.loading.FireFlowProgressIndicators
import dev.zitech.ds.molecules.dialog.FireFlowDialogs
import dev.zitech.ds.molecules.snackbar.BottomNotifierMessage
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.settings.R
import dev.zitech.settings.presentation.settings.viewmodel.ConfirmLogOut
import dev.zitech.settings.presentation.settings.viewmodel.ErrorHandled
import dev.zitech.settings.presentation.settings.viewmodel.Idle
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
import dev.zitech.settings.presentation.settings.viewmodel.RestartApplication
import dev.zitech.settings.presentation.settings.viewmodel.SelectLanguage
import dev.zitech.settings.presentation.settings.viewmodel.SelectTheme
import dev.zitech.settings.presentation.settings.viewmodel.SettingsViewModel
import dev.zitech.settings.presentation.settings.viewmodel.ShowError

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
                },
                onLogOutClick = {
                    viewModel.sendIntent(OnLogOutClick)
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

    when (val event = screenState.event) {
        is ShowError -> {
            snackbarState.showMessage(
                BottomNotifierMessage(
                    text = stringResource(event.messageResId),
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
                title = stringResource(R.string.appearance_dialog_theme_title),
                radioItems = event.themes,
                onItemClick = { viewModel.sendIntent(OnThemeSelect(it)) },
                onDismissRequest = { viewModel.sendIntent(OnThemeDismiss) }
            )
        }
        is SelectLanguage -> {
            FireFlowDialogs.Radio(
                title = stringResource(R.string.appearance_dialog_language_title),
                radioItems = event.languages,
                onItemClick = { viewModel.sendIntent(OnLanguageSelect(it)) },
                onDismissRequest = { viewModel.sendIntent(OnLanguageDismiss) }
            )
        }
        RestartApplication -> restartApplication()
        is ConfirmLogOut -> {
            FireFlowDialogs.Alert(
                title = stringResource(R.string.more_dialog_log_out_title),
                text = stringResource(R.string.more_dialog_log_out_text),
                onConfirmButtonClick = { viewModel.sendIntent(OnConfirmLogOutClick) },
                onDismissRequest = { viewModel.sendIntent(OnConfirmLogOutDismiss) }
            )
        }
        Idle -> {
            // NO_OP
        }
    }
}
