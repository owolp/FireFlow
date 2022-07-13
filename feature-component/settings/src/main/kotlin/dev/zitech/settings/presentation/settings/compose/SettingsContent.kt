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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.organisms.categoryprefrence.CategoryPreference
import dev.zitech.ds.organisms.categoryprefrence.FireFlowCategoryPreferences
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.settings.R
import dev.zitech.settings.presentation.settings.viewmodel.SettingsState

@ExperimentalMaterial3Api
@Composable
internal fun SettingsContent(
    state: SettingsState,
    modifier: Modifier = Modifier,
    onTelemetryCheckChanged: (checked: Boolean) -> Unit,
    onCrashReporterCheckChanged: (checked: Boolean) -> Unit
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        FireFlowCategoryPreferences.Simple(
            categoryName = stringResource(id = R.string.data_choices_category),
            preferences = getPreferencesList(state, onTelemetryCheckChanged, onCrashReporterCheckChanged)
        )
    }
}

@Composable
private fun getPreferencesList(
    state: SettingsState,
    onTelemetryCheckChanged: (checked: Boolean) -> Unit,
    onCrashReporterCheckChanged: (checked: Boolean) -> Unit
): List<CategoryPreference> {
    val categoryPreferences = mutableListOf<CategoryPreference>()

    if (state.telemetry != null) {
        categoryPreferences.add(
            CategoryPreference.Switch(
                title = stringResource(id = R.string.data_choices_telemetry_title),
                icon = Icons.Outlined.Analytics,
                checked = state.telemetry,
                onCheckedChanged = onTelemetryCheckChanged,
                cdDescriptionEnabled = stringResource(id = R.string.cd_data_choices_telemetry_enabled),
                cdDescriptionDisabled = stringResource(id = R.string.cd_data_choices_telemetry_disabled),
                description = stringResource(id = R.string.data_choices_telemetry_description)
            )
        )
    }

    categoryPreferences.add(
        CategoryPreference.Switch(
            title = stringResource(id = R.string.data_choices_crash_reporter_title),
            icon = Icons.Outlined.BugReport,
            checked = state.crashReporter,
            onCheckedChanged = onCrashReporterCheckChanged,
            cdDescriptionEnabled = stringResource(id = R.string.cd_data_choices_crash_reporter_enabled),
            cdDescriptionDisabled = stringResource(id = R.string.cd_data_choices_crash_reporter_disabled),
            description = stringResource(id = R.string.data_choices_crash_reporter_description)
        )
    )

    return categoryPreferences
}

@Preview(
    name = "Settings Content Light Theme",
    showBackground = true
)
@Preview(
    name = "Settings Content Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@ExperimentalMaterial3Api
@Composable
private fun SettingsContent_Preview() {
    FireFlowTheme {
        SettingsContent(
            state = SettingsState(),
            onTelemetryCheckChanged = {},
            onCrashReporterCheckChanged = {}
        )
    }
}
