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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.zitech.ds.atoms.loading.FireFlowProgressIndicators
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.settings.presentation.settings.viewmodel.OnCrashReporterCheck
import dev.zitech.settings.presentation.settings.viewmodel.OnTelemetryCheck
import dev.zitech.settings.presentation.settings.viewmodel.SettingsViewModel

@ExperimentalLifecycleComposeApi
@ExperimentalMaterial3Api
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
            onTelemetryCheckChanged = { checked ->
                viewModel.sendIntent(OnTelemetryCheck(checked))
            },
            onCrashReporterCheckChanged = { checked ->
                viewModel.sendIntent(OnCrashReporterCheck(checked))
            }
        )
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
@Composable
internal fun Settings_Preview() {
    FireFlowTheme {
        Settings()
    }
}
