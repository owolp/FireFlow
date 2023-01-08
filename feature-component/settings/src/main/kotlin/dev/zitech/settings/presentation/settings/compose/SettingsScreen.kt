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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.ds.atoms.icon.FireFlowIcons
import dev.zitech.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.ds.molecules.snackbar.rememberSnackbarState
import dev.zitech.ds.molecules.topappbar.FireFlowTopAppBars
import dev.zitech.ds.molecules.topappbar.ScrollBehavior
import dev.zitech.ds.organisms.categoryprefrence.CategoryPreference
import dev.zitech.ds.organisms.categoryprefrence.FireFlowCategoryPreferences
import dev.zitech.ds.templates.scaffold.FireFlowScaffolds
import dev.zitech.ds.theme.FireFlowTheme
import dev.zitech.ds.theme.PreviewFireFlowTheme
import dev.zitech.settings.R
import dev.zitech.settings.presentation.settings.viewmodel.SettingsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    state: SettingsState,
    onAnalyticsCheckChange: (checked: Boolean) -> Unit,
    onPersonalizedAdsCheckChange: (checked: Boolean) -> Unit,
    onPerformanceCheckChange: (checked: Boolean) -> Unit,
    onCrashReporterCheckChange: (checked: Boolean) -> Unit,
    onThemeClick: () -> Unit,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarState = rememberSnackbarState()
    val topAppBarScrollBehavior = FireFlowTopAppBars.topAppBarScrollBehavior(
        ScrollBehavior.ExitUntilCollapsed
    )

    FireFlowScaffolds.Primary(
        modifier = modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        snackbarState = snackbarState,
        topBar = {
            FireFlowTopAppBars.Collapsing.Primary(
                title = stringResource(id = R.string.settings),
                scrollBehavior = topAppBarScrollBehavior
            )
        }
    ) { innerPadding ->
        SettingsScreenContent(
            innerPadding,
            state,
            onAnalyticsCheckChange,
            onPersonalizedAdsCheckChange,
            onPerformanceCheckChange,
            onCrashReporterCheckChange,
            onThemeClick,
            onLanguageClick
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun SettingsScreenContent(
    innerPadding: PaddingValues,
    state: SettingsState,
    onAnalyticsCheckChange: (checked: Boolean) -> Unit,
    onPersonalizedAdsCheckChange: (checked: Boolean) -> Unit,
    onPerformanceCheckChange: (checked: Boolean) -> Unit,
    onCrashReporterCheckChange: (checked: Boolean) -> Unit,
    onThemeClick: () -> Unit,
    onLanguageClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = FireFlowTheme.space.m, end = FireFlowTheme.space.m)
            .padding(innerPadding)
            .consumedWindowInsets(innerPadding),
        verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.l)
    ) {
        item {
            FireFlowCategoryPreferences.Primary(
                categoryName = stringResource(id = R.string.data_choices_category),
                preferences = getDataChoicesPreferences(
                    state = state,
                    onAnalyticsCheckChange = onAnalyticsCheckChange,
                    onPersonalizedAdsCheckChange = onPersonalizedAdsCheckChange,
                    onPerformanceCheckChange = onPerformanceCheckChange,
                    onCrashReporterCheckChange = onCrashReporterCheckChange
                )
            )
        }
        item {
            FireFlowCategoryPreferences.Primary(
                categoryName = stringResource(id = R.string.appearance_category),
                preferences = getAppearancePreferences(
                    state = state,
                    onThemeClick = onThemeClick,
                    onLanguageClick = onLanguageClick
                )
            )
        }
        item {
            FireFlowCategoryPreferences.Primary(
                categoryName = stringResource(id = R.string.about_application_category),
                preferences = getAboutApplicationPreferences(
                    state = state
                )
            )
        }
        item {
            FireFlowSpacers.Vertical(verticalSpace = FireFlowTheme.space.m)
        }
    }
}

@Composable
private fun getAppearancePreferences(
    state: SettingsState,
    onThemeClick: () -> Unit,
    onLanguageClick: () -> Unit
): List<CategoryPreference> {
    val categoryPreferences = mutableListOf<CategoryPreference>()

    categoryPreferences.add(
        CategoryPreference.Icon(
            title = stringResource(id = R.string.appearance_theme),
            icon = FireFlowIcons.Brightness6,
            description = stringResource(id = state.theme.text),
            onClick = Pair(stringResource(id = R.string.cd_appearance_theme_click), onThemeClick)
        )
    )

    categoryPreferences.add(
        CategoryPreference.Icon(
            title = stringResource(id = R.string.appearance_language),
            icon = FireFlowIcons.Language,
            description = stringResource(id = state.language.text),
            onClick = Pair(
                stringResource(id = R.string.cd_appearance_language_click),
                onLanguageClick
            )
        )
    )

    return categoryPreferences
}

@Composable
private fun getDataChoicesPreferences(
    state: SettingsState,
    onAnalyticsCheckChange: (checked: Boolean) -> Unit,
    onPersonalizedAdsCheckChange: (checked: Boolean) -> Unit,
    onPerformanceCheckChange: (checked: Boolean) -> Unit,
    onCrashReporterCheckChange: (checked: Boolean) -> Unit
): List<CategoryPreference> {
    val categoryPreferences = mutableListOf<CategoryPreference>()

    if (state.analytics != null) {
        categoryPreferences.add(
            CategoryPreference.Switch(
                title = stringResource(id = R.string.data_choices_analytics_title),
                icon = FireFlowIcons.Analytics,
                checked = state.analytics,
                onCheckedChanged = onAnalyticsCheckChange,
                cdDescriptionEnabled = stringResource(
                    id = R.string.cd_data_choices_analytics_enabled
                ),
                cdDescriptionDisabled = stringResource(
                    id = R.string.cd_data_choices_analytics_disabled
                ),
                description = stringResource(id = R.string.data_choices_analytics_description)
            )
        )

        if (state.analytics && state.personalizedAds != null) {
            categoryPreferences.add(
                CategoryPreference.Switch(
                    title = stringResource(id = R.string.data_choices_personalized_ads_title),
                    icon = FireFlowIcons.AdsClick,
                    checked = state.personalizedAds,
                    onCheckedChanged = onPersonalizedAdsCheckChange,
                    cdDescriptionEnabled = stringResource(
                        id = R.string.cd_data_choices_personalized_ads_enabled
                    ),
                    cdDescriptionDisabled = stringResource(
                        id = R.string.cd_data_choices_personalized_ads_disabled
                    ),
                    description = stringResource(
                        id = R.string.data_choices_personalized_ads_description
                    )
                )
            )
        }

        if (state.analytics && state.performance != null) {
            categoryPreferences.add(
                CategoryPreference.Switch(
                    title = stringResource(id = R.string.data_choices_performance_title),
                    icon = FireFlowIcons.Speed,
                    checked = state.performance,
                    onCheckedChanged = onPerformanceCheckChange,
                    cdDescriptionEnabled = stringResource(
                        id = R.string.cd_data_choices_performance_enabled
                    ),
                    cdDescriptionDisabled = stringResource(
                        id = R.string.cd_data_choices_performance_disabled
                    ),
                    description = stringResource(id = R.string.data_choices_performance_description)
                )
            )
        }
    }

    categoryPreferences.add(
        CategoryPreference.Switch(
            title = stringResource(id = R.string.data_choices_crash_reporter_title),
            icon = FireFlowIcons.BugReport,
            checked = state.crashReporter,
            onCheckedChanged = onCrashReporterCheckChange,
            cdDescriptionEnabled = stringResource(
                id = R.string.cd_data_choices_crash_reporter_enabled
            ),
            cdDescriptionDisabled = stringResource(
                id = R.string.cd_data_choices_crash_reporter_disabled
            ),
            description = stringResource(id = R.string.data_choices_crash_reporter_description)
        )
    )

    return categoryPreferences
}

@Composable
private fun getAboutApplicationPreferences(
    state: SettingsState
): List<CategoryPreference> {
    val categoryPreferences = mutableListOf<CategoryPreference>()

    categoryPreferences.add(
        CategoryPreference.Icon(
            title = stringResource(id = R.string.about_application_version),
            icon = FireFlowIcons.Info,
            description = state.version
        )
    )

    return categoryPreferences
}

@Preview(
    name = "Settings Screen Light Theme",
    showBackground = true
)
@Preview(
    name = "Settings Screen Dark Theme",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun SettingsScreen_Preview() {
    PreviewFireFlowTheme {
        SettingsScreen(
            state = SettingsState(),
            onAnalyticsCheckChange = {},
            onPersonalizedAdsCheckChange = {},
            onPerformanceCheckChange = {},
            onCrashReporterCheckChange = {},
            onThemeClick = {},
            onLanguageClick = {}
        )
    }
}
