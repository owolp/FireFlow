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

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.zitech.core.common.domain.model.ApplicationLanguage
import dev.zitech.core.common.domain.model.ApplicationTheme
import dev.zitech.ds.atoms.icon.FireFlowIcons
import dev.zitech.ds.atoms.spacer.FireFlowSpacers
import dev.zitech.ds.molecules.dialog.DialogRadioItem
import dev.zitech.ds.molecules.dialog.FireFlowDialogs
import dev.zitech.ds.molecules.snackbar.BottomNotifierMessage
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
    onLogOutClick: () -> Unit,
    onLanguageSelect: (itemSelected: Int) -> Unit,
    onLanguageDismiss: () -> Unit,
    onThemeSelect: (itemSelected: Int) -> Unit,
    onThemeDismiss: () -> Unit,
    onConfirmLogOutClick: () -> Unit,
    onConfirmLogOutDismiss: () -> Unit,
    onRestartApplication: () -> Unit,
    analyticsErrorHandled: () -> Unit,
    crashReporterErrorHandled: () -> Unit,
    personalizedAdsErrorHandled: () -> Unit,
    performanceErrorHandled: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topAppBarScrollBehavior = FireFlowTopAppBars.topAppBarScrollBehavior(
        ScrollBehavior.ExitUntilCollapsed
    )
    val snackbarState = rememberSnackbarState()

    if (state.analyticsError) {
        snackbarState.showMessage(
            getBottomNotifierMessage(
                text = R.string.data_choices_analytics_error,
                onAction = onRestartApplication
            )
        )
        analyticsErrorHandled()
    }
    if (state.selectApplicationLanguage != null) {
        FireFlowDialogs.Radio(
            title = stringResource(R.string.appearance_dialog_language_title),
            radioItems = getDialogLanguages(state.selectApplicationLanguage),
            onItemClick = onLanguageSelect,
            onDismissRequest = onLanguageDismiss
        )
    }
    if (state.selectApplicationTheme != null) {
        FireFlowDialogs.Radio(
            title = stringResource(R.string.appearance_dialog_theme_title),
            radioItems = getDialogThemes(state.selectApplicationTheme),
            onItemClick = onThemeSelect,
            onDismissRequest = onThemeDismiss
        )
    }
    if (state.confirmLogOut) {
        FireFlowDialogs.Alert(
            title = stringResource(R.string.more_dialog_log_out_title),
            text = stringResource(R.string.more_dialog_log_out_text),
            onConfirmButtonClick = onConfirmLogOutClick,
            onDismissRequest = onConfirmLogOutDismiss
        )
    }
    if (state.crashReporterError) {
        snackbarState.showMessage(
            getBottomNotifierMessage(
                text = R.string.data_choices_crash_reporter_error,
                onAction = onRestartApplication
            )
        )
        crashReporterErrorHandled()
    }
    if (state.personalizedAdsError) {
        snackbarState.showMessage(
            getBottomNotifierMessage(
                text = R.string.data_choices_personalized_ads_error,
                onAction = onRestartApplication
            )
        )
        personalizedAdsErrorHandled()
    }
    if (state.performanceError) {
        snackbarState.showMessage(
            getBottomNotifierMessage(
                text = R.string.data_choices_personalized_ads_error,
                onAction = onRestartApplication
            )
        )
        performanceErrorHandled()
    }

    FireFlowScaffolds.Primary(
        modifier = modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        snackbarState = snackbarState,
        topBar = {
            FireFlowTopAppBars.Collapsing.Primary(
                title = stringResource(R.string.settings),
                scrollBehavior = topAppBarScrollBehavior
            )
        }
    ) { innerPadding ->
        SettingsScreenContent(
            innerPadding = innerPadding,
            state = state,
            onAnalyticsCheckChange = onAnalyticsCheckChange,
            onPersonalizedAdsCheckChange = onPersonalizedAdsCheckChange,
            onPerformanceCheckChange = onPerformanceCheckChange,
            onCrashReporterCheckChange = onCrashReporterCheckChange,
            onThemeClick = onThemeClick,
            onLanguageClick = onLanguageClick,
            onLogOutClick = onLogOutClick
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
    onLanguageClick: () -> Unit,
    onLogOutClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = FireFlowTheme.space.gutter)
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding),
        verticalArrangement = Arrangement.spacedBy(FireFlowTheme.space.l)
    ) {
        item {
            FireFlowCategoryPreferences.Primary(
                categoryName = stringResource(R.string.data_choices_category),
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
                categoryName = stringResource(R.string.appearance_category),
                preferences = getAppearancePreferences(
                    state = state,
                    onThemeClick = onThemeClick,
                    onLanguageClick = onLanguageClick
                )
            )
        }
        item {
            FireFlowCategoryPreferences.Primary(
                categoryName = stringResource(R.string.more_category),
                preferences = getMorePreferences(
                    state = state,
                    onLogOutClick = onLogOutClick
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
            title = stringResource(R.string.appearance_theme),
            icon = FireFlowIcons.Brightness6,
            description = stringResource(state.applicationTheme.text),
            onClick = Pair(stringResource(R.string.cd_appearance_theme_click), onThemeClick)
        )
    )

    categoryPreferences.add(
        CategoryPreference.Icon(
            title = stringResource(R.string.appearance_language),
            icon = FireFlowIcons.Language,
            description = stringResource(state.applicationLanguage.text),
            onClick = Pair(
                stringResource(R.string.cd_appearance_language_click),
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
                title = stringResource(R.string.data_choices_analytics_title),
                icon = FireFlowIcons.Analytics,
                checked = state.analytics,
                onCheckedChanged = onAnalyticsCheckChange,
                cdDescriptionEnabled = stringResource(R.string.cd_data_choices_analytics_enabled),
                cdDescriptionDisabled = stringResource(R.string.cd_data_choices_analytics_disabled),
                description = stringResource(R.string.data_choices_analytics_description)
            )
        )

        if (state.analytics && state.personalizedAds != null) {
            categoryPreferences.add(
                CategoryPreference.Switch(
                    title = stringResource(R.string.data_choices_personalized_ads_title),
                    icon = FireFlowIcons.AdsClick,
                    checked = state.personalizedAds,
                    onCheckedChanged = onPersonalizedAdsCheckChange,
                    cdDescriptionEnabled = stringResource(
                        R.string.cd_data_choices_personalized_ads_enabled
                    ),
                    cdDescriptionDisabled = stringResource(
                        R.string.cd_data_choices_personalized_ads_disabled
                    ),
                    description = stringResource(R.string.data_choices_personalized_ads_description)
                )
            )
        }

        if (state.analytics && state.performance != null) {
            categoryPreferences.add(
                CategoryPreference.Switch(
                    title = stringResource(R.string.data_choices_performance_title),
                    icon = FireFlowIcons.Speed,
                    checked = state.performance,
                    onCheckedChanged = onPerformanceCheckChange,
                    cdDescriptionEnabled = stringResource(
                        R.string.cd_data_choices_performance_enabled
                    ),
                    cdDescriptionDisabled = stringResource(
                        R.string.cd_data_choices_performance_disabled
                    ),
                    description = stringResource(R.string.data_choices_performance_description)
                )
            )
        }
    }

    categoryPreferences.add(
        CategoryPreference.Switch(
            title = stringResource(R.string.data_choices_crash_reporter_title),
            icon = FireFlowIcons.BugReport,
            checked = state.crashReporter,
            onCheckedChanged = onCrashReporterCheckChange,
            cdDescriptionEnabled = stringResource(R.string.cd_data_choices_crash_reporter_enabled),
            cdDescriptionDisabled = stringResource(
                R.string.cd_data_choices_crash_reporter_disabled
            ),
            description = stringResource(R.string.data_choices_crash_reporter_description)
        )
    )

    return categoryPreferences
}

@Composable
private fun getMorePreferences(
    state: SettingsState,
    onLogOutClick: () -> Unit
): List<CategoryPreference> {
    val categoryPreferences = mutableListOf<CategoryPreference>()

    categoryPreferences.add(
        CategoryPreference.Icon(
            title = stringResource(R.string.more_version),
            icon = FireFlowIcons.Info,
            description = state.version
        )
    )
    categoryPreferences.add(
        CategoryPreference.Icon(
            title = stringResource(R.string.more_log_out),
            icon = FireFlowIcons.Logout,
            description = state.email,
            onClick = Pair(
                stringResource(R.string.cd_more_log_out_click, state.email),
                onLogOutClick
            )
        )
    )

    return categoryPreferences
}

@Composable
private fun getBottomNotifierMessage(
    text: Int,
    onAction: () -> Unit
) = BottomNotifierMessage(
    text = stringResource(text),
    state = BottomNotifierMessage.State.ERROR,
    duration = BottomNotifierMessage.Duration.SHORT,
    action = BottomNotifierMessage.Action(
        label = stringResource(R.string.action_restart),
        onAction = onAction
    )
)

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
            onLanguageClick = {},
            onLogOutClick = {},
            onRestartApplication = {},
            analyticsErrorHandled = {},
            crashReporterErrorHandled = {},
            personalizedAdsErrorHandled = {},
            performanceErrorHandled = {},
            onLanguageDismiss = {},
            onLanguageSelect = {},
            onThemeDismiss = {},
            onThemeSelect = {},
            onConfirmLogOutClick = {},
            onConfirmLogOutDismiss = {}
        )
    }
}

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
