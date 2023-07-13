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

package dev.zitech.fireflow.settings.presentation.settings.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.fireflow.common.domain.model.application.ApplicationLanguage
import dev.zitech.fireflow.common.domain.model.application.ApplicationTheme
import dev.zitech.fireflow.common.presentation.navigation.deeplink.DeepLinkScreenDestination
import dev.zitech.fireflow.core.error.Error
import dev.zitech.fireflow.ds.molecules.dialog.DialogRadioItem
import dev.zitech.fireflow.ds.molecules.dialog.FireFlowDialogs
import dev.zitech.fireflow.ds.molecules.snackbar.BottomNotifierMessage
import dev.zitech.fireflow.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.fireflow.settings.R
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.AnalyticsChecked
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.AnalyticsErrorHandled
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.ConfirmDeleteAllDataClicked
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.ConfirmDeleteAllDataDismissed
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.ConfirmLogOutClicked
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.ConfirmLogOutDismissed
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.ConnectivityChecked
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.CrashReporterChecked
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.CrashReporterErrorHandled
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.DeleteAllDataClicked
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.FatalErrorHandled
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.LanguageDismissed
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.LanguagePreferenceClicked
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.LanguageSelected
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.LogOutClicked
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.PerformanceChecked
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.PerformanceErrorHandled
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.PersonalizedAdsChecked
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.PersonalizedAdsErrorHandled
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.RestartApplicationClicked
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.SettingsViewModel
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.ThemeDismissed
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.ThemePreferenceClicked
import dev.zitech.fireflow.settings.presentation.settings.viewmodel.ThemeSelected

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
    val snackbarState = rememberSnackbarState()

    if (screenState.analyticsError) {
        snackbarState.showMessage(
            getBottomNotifierMessage(
                text = R.string.data_choices_analytics_error,
                onActionClicked = {
                    viewModel.receiveIntent(RestartApplicationClicked(restartApplication))
                }
            )
        )
        viewModel.receiveIntent(AnalyticsErrorHandled)
    }
    screenState.selectApplicationLanguage?.let { applicationLanguage ->
        FireFlowDialogs.Radio(
            title = stringResource(R.string.appearance_dialog_language_title),
            radioItems = getDialogLanguages(applicationLanguage),
            onItemClick = { itemSelected ->
                viewModel.receiveIntent(LanguageSelected(itemSelected))
            },
            onDismissRequest = {
                viewModel.receiveIntent(LanguageDismissed)
            }
        )
    }
    screenState.selectApplicationTheme?.let { applicationTheme ->
        FireFlowDialogs.Radio(
            title = stringResource(R.string.appearance_dialog_theme_title),
            radioItems = getDialogThemes(applicationTheme),
            onItemClick = { itemSelected ->
                viewModel.receiveIntent(ThemeSelected(itemSelected))
            },
            onDismissRequest = {
                viewModel.receiveIntent(ThemeDismissed)
            }
        )
    }
    if (screenState.confirmLogOut) {
        FireFlowDialogs.Alert(
            title = stringResource(R.string.more_dialog_log_out_title),
            text = stringResource(R.string.more_dialog_log_out_text),
            onConfirmButtonClick = {
                viewModel.receiveIntent(ConfirmLogOutClicked)
            },
            onDismissRequest = {
                viewModel.receiveIntent(ConfirmLogOutDismissed)
            }
        )
    }
    if (screenState.confirmDeleteAll) {
        FireFlowDialogs.Alert(
            title = stringResource(R.string.more_dialog_delete_data_title),
            text = stringResource(R.string.more_dialog_delete_data_text),
            onConfirmButtonClick = {
                viewModel.receiveIntent(ConfirmDeleteAllDataClicked)
            },
            onDismissRequest = {
                viewModel.receiveIntent(ConfirmDeleteAllDataDismissed)
            }
        )
    }
    if (screenState.crashReporterError) {
        snackbarState.showMessage(
            getBottomNotifierMessage(
                text = R.string.data_choices_crash_reporter_error,
                onActionClicked = {
                    viewModel.receiveIntent(RestartApplicationClicked(restartApplication))
                }
            )
        )
        viewModel.receiveIntent(CrashReporterErrorHandled)
    }
    screenState.fatalError?.let { fireFlowError ->
        navigateToError(fireFlowError)
        viewModel.receiveIntent(FatalErrorHandled)
    }
    if (screenState.personalizedAdsError) {
        snackbarState.showMessage(
            getBottomNotifierMessage(
                text = R.string.data_choices_personalized_ads_error,
                onActionClicked = {
                    viewModel.receiveIntent(RestartApplicationClicked(restartApplication))
                }
            )
        )
        viewModel.receiveIntent(PersonalizedAdsErrorHandled)
    }
    if (screenState.performanceError) {
        snackbarState.showMessage(
            getBottomNotifierMessage(
                text = R.string.data_choices_personalized_ads_error,
                onActionClicked = {
                    viewModel.receiveIntent(RestartApplicationClicked(restartApplication))
                }
            )
        )
        viewModel.receiveIntent(PerformanceErrorHandled)
    }

    val deepLinkScreenDestination = screenState.deepLinkScreenDestination
    if (deepLinkScreenDestination == null) {
        SettingsScreen(
            modifier = modifier,
            state = screenState,
            snackbarState = snackbarState,
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
            deleteAllDataClicked = {
                viewModel.receiveIntent(DeleteAllDataClicked)
            },
            connectivityChecked = { checked ->
                viewModel.receiveIntent(ConnectivityChecked(checked))
            }
        )
    } else {
        LaunchedEffect(Unit) {
            when (deepLinkScreenDestination) {
                DeepLinkScreenDestination.Accounts -> navigateToAccounts()
                is DeepLinkScreenDestination.Error ->
                    navigateToError(deepLinkScreenDestination.error)

                DeepLinkScreenDestination.Welcome -> navigateToWelcome()
                DeepLinkScreenDestination.Current,
                DeepLinkScreenDestination.Init -> {
                    // NO_OP
                }
            }
        }
    }
}

@Composable
private fun getBottomNotifierMessage(
    text: Int,
    onActionClicked: () -> Unit
) = BottomNotifierMessage(
    text = stringResource(text),
    state = BottomNotifierMessage.State.ERROR,
    duration = BottomNotifierMessage.Duration.SHORT,
    action = BottomNotifierMessage.Action(
        label = stringResource(R.string.action_restart),
        onAction = onActionClicked
    )
)

@Composable
private fun getDialogLanguages(applicationLanguage: ApplicationLanguage): List<DialogRadioItem> =
    ApplicationLanguage.values().sortedBy { it.id }
        .map {
            DialogRadioItem(
                id = it.id,
                text = stringResource(it.text),
                selected = applicationLanguage.id == it.id,
                enabled = true
            )
        }

@Composable
private fun getDialogThemes(applicationTheme: ApplicationTheme): List<DialogRadioItem> =
    ApplicationTheme.values().sortedBy { it.id }
        .map {
            DialogRadioItem(
                id = it.id,
                text = stringResource(it.text),
                selected = applicationTheme.id == it.id,
                enabled = true
            )
        }
