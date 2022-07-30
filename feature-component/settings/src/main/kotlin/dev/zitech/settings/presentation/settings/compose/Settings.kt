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

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.zitech.ds.atoms.loading.FireFlowProgressIndicators
import dev.zitech.ds.molecules.dialog.FireFlowDialogs
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.settings.presentation.settings.viewmodel.Dialog
import dev.zitech.settings.presentation.settings.viewmodel.Error
import dev.zitech.settings.presentation.settings.viewmodel.Idle
import dev.zitech.settings.presentation.settings.viewmodel.OnAnalyticsCheckChange
import dev.zitech.settings.presentation.settings.viewmodel.OnCrashReporterCheckChange
import dev.zitech.settings.presentation.settings.viewmodel.OnPersonalizedAdsCheckChange
import dev.zitech.settings.presentation.settings.viewmodel.OnThemeDismiss
import dev.zitech.settings.presentation.settings.viewmodel.OnThemePreferenceClick
import dev.zitech.settings.presentation.settings.viewmodel.OnThemeSelect
import dev.zitech.settings.presentation.settings.viewmodel.SelectTheme
import dev.zitech.settings.presentation.settings.viewmodel.SettingsViewModel

@ExperimentalLifecycleComposeApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun Settings(
    viewModel: SettingsViewModel = viewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    if (state.value.isLoading) {
        FireFlowProgressIndicators.Settings()
    } else {
        SettingsContent(
            state = state.value,
            onAnalyticsCheckChange = { checked ->
                viewModel.sendIntent(OnAnalyticsCheckChange(checked))
            },
            onPersonalizedAdsCheckChange = { checked ->
                viewModel.sendIntent(OnPersonalizedAdsCheckChange(checked))
            },
            onCrashReporterCheckChange = { checked ->
                viewModel.sendIntent(OnCrashReporterCheckChange(checked))
            },
            onThemeClick = {
                viewModel.sendIntent(OnThemePreferenceClick)
            }
        )
    }

    @Suppress("ForbiddenComment")
    when (val event = state.value.event) {
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
        Idle -> {
            // NO_OP
        }
    }
}

@Preview(
    name = "Settings Light Theme",
    showBackground = true
)
@Preview(
    name = "Settings Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@ExperimentalLifecycleComposeApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
private fun Settings_Preview() {
    FireFlowTheme {
        Settings()
    }
}
