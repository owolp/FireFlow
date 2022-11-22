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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zitech.ds.atoms.loading.FireFlowProgressIndicators
import dev.zitech.ds.molecules.dialog.FireFlowDialogs
import dev.zitech.settings.presentation.settings.viewmodel.Dialog
import dev.zitech.settings.presentation.settings.viewmodel.Error
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
import dev.zitech.settings.presentation.settings.viewmodel.SelectLanguage
import dev.zitech.settings.presentation.settings.viewmodel.SelectTheme
import dev.zitech.settings.presentation.settings.viewmodel.SettingsState.ViewState.InitScreen
import dev.zitech.settings.presentation.settings.viewmodel.SettingsState.ViewState.Success
import dev.zitech.settings.presentation.settings.viewmodel.SettingsViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    @Suppress("ForbiddenComment")
    when (val event = state.event) {
        is Error -> {
            // TODO: Show error SnackBar with restart button
        }
        is Dialog -> {
            FireFlowDialogs.Alert(
                title = event.title,
                text = event.text,
                onConfirmButtonClick = { /*TODO*/ }
            )
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
        Idle -> {
            // NO_OP
        }
    }

    when (state.viewState) {
        InitScreen -> {
            FireFlowProgressIndicators.Settings()
        }
        Success -> {
            SettingsScreen(
                state = state,
                modifier = modifier,
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
    }
}
